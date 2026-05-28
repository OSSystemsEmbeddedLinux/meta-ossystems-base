# -*- python -*-
# easysplash-animation.bbclass allows for easy packaging of EasySplash
# animation packages.
#
# Copyright (C) 2015, 2021 O.S. Systems Softwares Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

inherit update-alternatives allarch

ALTERNATIVE:${PN} += "animation"
ALTERNATIVE_TARGET[animation] = "${base_libdir}/easysplash/${PN}"
ALTERNATIVE_LINK_NAME[animation] = "${base_libdir}/easysplash/animation"
ALTERNATIVE_PRIORITY[animation] = "50"

FILES:${PN} += "${base_libdir}/easysplash/${PN}"
RDEPENDS:${PN} += "easysplash"
