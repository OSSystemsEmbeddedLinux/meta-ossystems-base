#
# Copyright (C) 2026 O.S. Systems Software LTDA.
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.data import skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage

DEFAULTS = '/etc/default/net-persistent-mac'


class NetPersistentMacTest(OERuntimeTestCase):
    """net-persistent-mac: store each interface's MAC once, restore it after."""

    def run_ok(self, cmd):
        status, output = self.target.run(cmd)
        self.assertEqual(status, 0, msg='%s\n%s' % (cmd, output))
        return output

    def config(self):
        out = self.run_ok('. %s; echo "$INTERFACES"; echo "$MAC_DIR"' % DEFAULTS)
        lines = out.splitlines()
        return lines[0].split(), lines[1].strip()

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['net-persistent-mac'])
    @skipIfNotFeature('systemd', 'Test requires systemd in DISTRO_FEATURES')
    def test_net_persistent_mac_unit(self):
        unit = 'net-persistent-mac.service'
        self.assertEqual(self.run_ok('systemctl is-enabled %s' % unit).strip(), 'enabled')
        output = self.run_ok('systemctl show -p Result -p ExecMainStatus %s' % unit)
        props = dict(l.split('=', 1) for l in output.splitlines() if '=' in l)
        self.assertEqual(props.get('Result'), 'success', msg=output)
        self.assertEqual(props.get('ExecMainStatus'), '0', msg=output)

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['net-persistent-mac'])
    def test_net_persistent_mac_stored(self):
        """Every configured interface that exists has its MAC stored, nothing else."""
        interfaces, mac_dir = self.config()
        expected = set()
        for iface in interfaces:
            status, current = self.target.run('cat /sys/class/net/%s/address' % iface)
            if status != 0:
                continue
            expected.add('%s.mac' % iface)
            stored = self.run_ok('cat %s/%s.mac' % (mac_dir, iface)).strip()
            self.assertEqual(stored, current.strip())
        if not expected:
            self.skipTest('none of %s exists on this target' % interfaces)
        present = set(self.run_ok('ls -A %s' % mac_dir).split())
        self.assertEqual(present, expected, msg='unexpected files in %s' % mac_dir)

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['net-persistent-mac'])
    def test_net_persistent_mac_restore(self):
        """A stored MAC is applied to the interface (on a dummy interface)."""
        iface = 'oeqa0'
        wanted = '02:00:5e:10:20:30'
        status, output = self.target.run('ip link add %s type dummy' % iface)
        if status != 0:
            self.skipTest('cannot create a dummy interface: %s' % output)
        _, mac_dir = self.config()
        try:
            self.run_ok('cp %s /tmp/oeqa-npm.default' % DEFAULTS)
            self.run_ok('sed -i "s/^INTERFACES=.*/INTERFACES=\\"%s\\"/" %s' % (iface, DEFAULTS))

            # First run stores the current MAC.
            current = self.run_ok('cat /sys/class/net/%s/address' % iface).strip()
            self.run_ok('net-persistent-mac')
            self.assertEqual(self.run_ok('cat %s/%s.mac' % (mac_dir, iface)).strip(), current)

            # A later run restores the stored one.
            self.run_ok('echo %s > %s/%s.mac' % (wanted, mac_dir, iface))
            self.run_ok('net-persistent-mac')
            self.assertEqual(self.run_ok('cat /sys/class/net/%s/address' % iface).strip(), wanted)
        finally:
            self.target.run('mv /tmp/oeqa-npm.default %s; ip link del %s; rm -f %s/%s.mac'
                            % (DEFAULTS, iface, mac_dir, iface))
