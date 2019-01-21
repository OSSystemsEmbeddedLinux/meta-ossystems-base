SUMMARY = "A wrapper script for the zram kernel module with interactive and init support"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/README.md;beginline=5;endline=7;md5=1c6f4971407e5a5b1aa502b9badcdf98"

inherit update-rc.d systemd

SRCREV = "7b0b68d889d9a6ddfc7861731fb1d62c6838dd5c"
SRC_URI = " \
    git://github.com/vaeth/zram-init;protocol=https \
    file://${PN}.initd \
"

S = "${WORKDIR}/git"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE_${PN} = " \
    zram_swap.service \
    zram_tmp.service \
"

do_install () {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0755 ${S}/sbin/${PN}  ${D}${base_sbindir}/${PN}
        install -Dm 0644 ${S}/modprobe.d/zram.conf ${D}${sysconfdir}/modprobe.d/zram.conf
        install -Dm 0644 ${S}/systemd/system/zram_swap.service ${D}${systemd_unitdir}/system/zram_swap.service
        install -Dm 0644 ${S}/systemd/system/zram_tmp.service ${D}${systemd_unitdir}/system/zram_tmp.service
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${WORKDIR}/${PN}.initd ${D}/${sysconfdir}/init.d/${PN}
    fi
}

RDEPENDS_${PN} = "e2fsprogs-tune2fs"
RRECOMMENDS_${PN} = "kernel-module-zram"
