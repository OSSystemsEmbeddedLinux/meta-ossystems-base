# -*- python -*-
# easysplash-animation.bbclass allows for easy packaging of EasySplash
# animation packages.
#
# Copyright (C) 2015, 2020 O.S. Systems Softwares Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

inherit update-alternatives allarch

ALTERNATIVE_${PN} += "bootanimation"
ALTERNATIVE_TARGET[bootanimation] = "/lib/easysplash/${PN}.zip"
ALTERNATIVE_LINK_NAME[bootanimation] = "/lib/easysplash/bootanimation.zip"
ALTERNATIVE_PRIORITY[bootanimation] = "50"

do_install_append() {
    install -Dm 0644 ${S}/*.zip ${D}/lib/easysplash/${PN}.zip
}

FILES_${PN} += "/lib/easysplash/${PN}.zip"
RDEPENDS_${PN} += "easysplash"
