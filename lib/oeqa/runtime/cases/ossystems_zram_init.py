#
# Copyright (C) 2026 O.S. Systems Software LTDA.
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.data import skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage


class ZramInitTest(OERuntimeTestCase):
    """zram-init: zram_swap.service sets up a zram swap device at boot."""

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['zram-init'])
    @skipIfNotFeature('systemd', 'Test requires systemd in DISTRO_FEATURES')
    def test_zram_swap(self):
        status, output = self.target.run('modprobe zram 2>/dev/null; test -e /sys/class/zram-control')
        if status != 0:
            self.skipTest('kernel has no zram support')

        status, output = self.target.run('systemctl is-active zram_swap.service')
        if status != 0:
            output += self.target.run('systemctl status --full zram_swap.service')[1]
        self.assertEqual(status, 0, msg=output)

        status, output = self.target.run('cat /proc/swaps')
        self.assertIn('/dev/zram', output, msg='no zram swap active:\n%s' % output)
