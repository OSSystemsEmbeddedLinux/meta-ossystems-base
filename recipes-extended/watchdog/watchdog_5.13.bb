SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecc0551bf54ad97f6b541720f84d6569"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz \
           file://fixsepbuild.patch \
           file://fix-ping-failure.patch \
           file://watchdog.init \
           file://wd_keepalive.init \
          "

SRC_URI[md5sum] = "153455f008f1cf8f65f6ad9586a21ff1"
SRC_URI[sha256sum] = "141e0faf3ee4d8187a6ff4e00b18ef7b7a4ce432a2d4c8a6e6fdc62507fc6eb0"

PACKAGES =+ "${PN}-keepalive"

RDEPENDS_${PN} += "${PN}-config"
RDEPENDS_${PN}-keepalive += "${PN}-config"

inherit autotools update-rc.d

INITSCRIPT_PACKAGES = "${PN} \
                       ${PN}-keepalive"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 89 11"

INITSCRIPT_NAME_${PN}-keepalive = "wd_keepalive"
INITSCRIPT_PARAMS_${PN}-keepalive = "start 09 2 3 4 5 ."

do_install_append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 755 ${WORKDIR}/watchdog.init ${D}${sysconfdir}/init.d/watchdog
    install -m 755 ${WORKDIR}/wd_keepalive.init ${D}${sysconfdir}/init.d/wd_keepalive

    # watchdog.conf is provided by the watchdog-config recipe
    rm ${D}${sysconfdir}/watchdog.conf
}

FILES_${PN} = "${sysconfdir}/init.d/${PN} \
               ${sbindir}/${PN} \
               ${sbindir}/wd_identify"

FILES_${PN}-keepalive = "${sysconfdir}/init.d/wd_keepalive \
                         ${sbindir}/wd_keepalive"

RDEPENDS_${PN} = "${PN}-keepalive"

RRECOMMENDS_${PN} = "kernel-module-softdog"
