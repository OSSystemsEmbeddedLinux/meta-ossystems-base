SUMMARY = "Network device MAC persistency"
DESCRIPTION = "Provides support to store/restore the MAC of a specific network device"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://${BPN} \
    file://${BPN}.default \
    file://${BPN}.service \
"

OSSYSTEMS_FACTORY_DEFAULTS_HOOKS = "file://${BPN}.factory-defaults-hook"

inherit ossystems-factory-defaults systemd

SYSTEMD_SERVICE_${PN} = "${PN}.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -Dm 0644 ${WORKDIR}/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
    install -Dm 0644 ${WORKDIR}/${PN}.default ${D}${sysconfdir}/default/${PN}
	install -Dm 0755 ${WORKDIR}/${PN} ${D}${bindir}/${PN}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
