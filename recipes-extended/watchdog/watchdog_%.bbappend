FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://wd_keepalive.init"

PACKAGES =+ "${PN}-keepalive"

RDEPENDS_${PN} += "${PN}-config"
RDEPENDS_${PN}-keepalive += "${PN}-config"

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN} \
                       ${PN}-keepalive"

INITSCRIPT_NAME_${PN}-keepalive = "wd_keepalive"
INITSCRIPT_PARAMS_${PN}-keepalive = "start 15 1 2 3 4 5 . stop 85 0 6 ."

do_install_append() {
    install -Dm 755 ${WORKDIR}/wd_keepalive.init ${D}${sysconfdir}/init.d/wd_keepalive

    # watchdog.conf is provided by the watchdog-config recipe
    rm ${D}${sysconfdir}/watchdog.conf
}

FILES_${PN}-keepalive = "${sysconfdir}/init.d/wd_keepalive \
                         ${sbindir}/wd_keepalive"

RDEPENDS_${PN} = "${PN}-keepalive"

RRECOMMENDS_${PN} = "kernel-module-softdog"
