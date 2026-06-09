SUMMARY = "Network device MAC persistency"
DESCRIPTION = "Provides support to store/restore the MAC of a specific network device"
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "base"
CVE_PRODUCT = "net-persistent-mac"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://COPYING.MIT \
    file://${BPN} \
    file://${BPN}.default \
    file://${BPN}.service \
"
S = "${UNPACKDIR}"

OSSYSTEMS_FACTORY_DEFAULTS_HOOKS = "file://${BPN}.factory-defaults-hook"

inherit ossystems-factory-defaults systemd

SYSTEMD_SERVICE:${PN} = "${PN}.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
    install -Dm 0644 ${UNPACKDIR}/${PN}.default ${D}${sysconfdir}/default/${PN}
    install -Dm 0755 ${UNPACKDIR}/${PN} ${D}${bindir}/${PN}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

BBCLASSEXTEND = ""
