#
# Copyright (C) 2026 O.S. Systems Software LTDA.
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.data import skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage

RUNDIR = '/tmp/oeqa-easysplash'


class EasySplashTest(OERuntimeTestCase):
    """easysplash: units installed and the open/client protocol works headless."""

    def run_ok(self, cmd, timeout=None):
        status, output = self.target.run(cmd, timeout=timeout)
        self.assertEqual(status, 0, msg='%s\n%s' % (cmd, output))
        return output

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['easysplash'])
    @skipIfNotFeature('systemd', 'Test requires systemd in DISTRO_FEATURES')
    def test_easysplash_units(self):
        for unit in ('easysplash-start.service', 'easysplash-quit.service'):
            self.assertEqual(self.run_ok('systemctl is-enabled %s' % unit).strip(), 'enabled')

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['easysplash'])
    def test_easysplash_open_stop(self):
        """Play the installed animation into fakesink, then stop it as a client."""
        status, _ = self.target.run('test -e /usr/lib/easysplash/animation/animation.toml')
        if status != 0:
            self.skipTest('no animation installed')
        try:
            self.run_ok('rm -rf %s && mkdir -p %s' % (RUNDIR, RUNDIR))
            self.run_ok("(easysplash open --video-sink fakesink --runtime-dir %s/run "
                        "--log debug /usr/lib/easysplash/animation > %s/log 2>&1; "
                        "echo $? > %s/rc) > /dev/null 2>&1 &" % (RUNDIR, RUNDIR, RUNDIR))

            # The server binds its socket only after the animation was loaded.
            status, output = self.target.run(
                'for i in $(seq 1 60); do [ -S %s/run/ipc.socket ] && break; '
                '[ -e %s/rc ] && break; sleep 1; done; cat %s/log'
                % (RUNDIR, RUNDIR, RUNDIR), timeout=90)
            self.assertNotEqual(self.target.run('test -e %s/rc' % RUNDIR)[0], 0,
                                msg='easysplash exited before being stopped:\n%s' % output)

            self.run_ok('easysplash client --stop --runtime-dir %s/run' % RUNDIR)
            output = self.run_ok('for i in $(seq 1 60); do [ -e %s/rc ] && break; sleep 1; done; '
                                 'cat %s/log; cat %s/rc' % (RUNDIR, RUNDIR, RUNDIR), timeout=90)
            self.assertEqual(output.splitlines()[-1].strip(), '0', msg=output)
        finally:
            self.target.run('killall easysplash 2>/dev/null; rm -rf %s' % RUNDIR)
