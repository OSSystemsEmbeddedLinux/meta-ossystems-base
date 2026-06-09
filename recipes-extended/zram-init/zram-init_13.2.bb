SUMMARY = "A wrapper script for the zram kernel module with interactive and init support"
DESCRIPTION = "Installs zram-init service and configuration for compressed memory devices."
HOMEPAGE = "https://github.com/vaeth/zram-init"
BUGTRACKER = "https://github.com/vaeth/zram-init/issues"
SECTION = "base"
CVE_PRODUCT = "zram-init"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://README.md;beginline=5;endline=7;md5=1c6f4971407e5a5b1aa502b9badcdf98"

inherit update-rc.d systemd

DEPENDS += "gettext-native"
PE = "1"

SRC_URI = "git://github.com/vaeth/zram-init;protocol=https;branch=main"
SRCREV = "66ef54e36d67abf76a8bfa2c2f1a6a0d38cd2498"

EXTRA_OEMAKE += "SHEBANG='#!/bin/sh'"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE:${PN} = "zram_swap.service"

do_compile:prepend() {
    # Force upstream's generated script to be recreated with the OE shebang.
    rm -f ${S}/sbin/${PN}
}

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
