#
# Copyright (C) 2026 O.S. Systems Software LTDA.
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.data import skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage


class FactoryDefaultsTest(OERuntimeTestCase):
    """ossystems-factory-defaults: restore /data from the factory copy once."""

    @classmethod
    def setUpClass(cls):
        td = cls.td
        cls.runtime_dir = td.get('OSSYSTEMS_FACTORY_DEFAULTS_RUNTIME_DIR') or '/data'
        cls.factory_dir = td.get('OSSYSTEMS_FACTORY_DEFAULTS_DIR') or '/usr/share/factory-defaults'
        cls.hooks_dir = td.get('OSSYSTEMS_FACTORY_DEFAULTS_HOOKS_DIR') or '/etc/factory-defaults.d'
        cls.anchor = cls.runtime_dir + '/.factory-defaults'
        if 'systemd' in td.get('DISTRO_FEATURES', '').split():
            cls.program = '/usr/bin/factory-defaults'
        else:
            cls.program = '/etc/init.d/factory-defaults'

    def run_ok(self, cmd):
        status, output = self.target.run(cmd)
        self.assertEqual(status, 0, msg='%s\n%s' % (cmd, output))
        return output

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['ossystems-factory-defaults'])
    def test_factory_defaults_ran_at_boot(self):
        status, output = self.target.run('test -e %s' % self.anchor)
        self.assertEqual(status, 0,
                         msg='%s missing: factory-defaults did not complete at boot' % self.anchor)

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['ossystems-factory-defaults'])
    @skipIfNotFeature('systemd', 'Test requires systemd in DISTRO_FEATURES')
    def test_factory_defaults_unit(self):
        unit = 'ossystems-factory-defaults.service'
        self.assertEqual(self.run_ok('systemctl is-enabled %s' % unit).strip(), 'enabled')
        output = self.run_ok('systemctl show -p Result -p ExecMainStatus %s' % unit)
        props = dict(l.split('=', 1) for l in output.splitlines() if '=' in l)
        self.assertEqual(props.get('Result'), 'success', msg=output)
        self.assertEqual(props.get('ExecMainStatus'), '0', msg=output)

    @OETestDepends(['ossystems_factory_defaults.FactoryDefaultsTest.test_factory_defaults_ran_at_boot'])
    def test_factory_defaults_restore(self):
        """Removing the anchor restores the factory files and runs the hooks, once."""
        probe = 'oeqa-factory-probe'
        hook = self.hooks_dir + '/zz-oeqa'
        marker = '/tmp/oeqa-factory-hook-ran'
        setup = ' && '.join([
            'mkdir -p %s %s' % (self.factory_dir, self.hooks_dir),
            'echo factory > %s/%s' % (self.factory_dir, probe),
            'printf "#!/bin/sh\\ntouch %s\\n" > %s' % (marker, hook),
            'chmod 0755 %s' % hook,
            'rm -f %s %s %s/%s' % (self.anchor, marker, self.runtime_dir, probe),
        ])
        cleanup = 'rm -f %s %s %s/%s %s/%s; touch %s' % (
            hook, marker, self.factory_dir, probe, self.runtime_dir, probe, self.anchor)
        try:
            self.run_ok(setup)
            self.run_ok(self.program)
            self.assertEqual(self.run_ok('cat %s/%s' % (self.runtime_dir, probe)).strip(), 'factory')
            self.run_ok('test -e %s' % marker)
            self.run_ok('test -e %s' % self.anchor)

            # With the anchor in place a second run must leave /data alone.
            self.run_ok('rm -f %s && echo changed > %s/%s' % (marker, self.runtime_dir, probe))
            self.run_ok(self.program)
            self.assertEqual(self.run_ok('cat %s/%s' % (self.runtime_dir, probe)).strip(), 'changed')
            status, _ = self.target.run('test -e %s' % marker)
            self.assertNotEqual(status, 0, msg='hooks ran again although the anchor exists')
        finally:
            self.target.run(cleanup)
