SUMMARY = "A wrapper script for the zram kernel module with interactive and init support"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/README.md;beginline=5;endline=7;md5=1c6f4971407e5a5b1aa502b9badcdf98"

inherit update-rc.d systemd

SRC_URI = "https://github.com/vaeth/zram-init;branch=main"
SRCREV = "703f63bd3e595b9b357d74c58db1370b40af250d"

S = "${WORKDIR}/git"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE:${PN} = "zram_swap.service"

do_install () {
    install -Dm 0755 ${S}/sbin/${PN}  ${D}${base_sbindir}/${PN}
    install -Dm 0644 ${S}/modprobe.d/zram.conf ${D}${sysconfdir}/modprobe.d/zram.conf

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${S}/systemd/system/zram_swap.service ${D}${systemd_unitdir}/system/zram_swap.service
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${S}/openrc/init.d/${PN} ${D}/${sysconfdir}/init.d/${PN}
        install -Dm 0644 ${S}/openrc/conf.d/${PN} ${D}${sysconfdir}/openrc/conf.d/${PN}
    fi
}

RDEPENDS:${PN} = "e2fsprogs-tune2fs"
RRECOMMENDS:${PN} = "kernel-module-zram"
