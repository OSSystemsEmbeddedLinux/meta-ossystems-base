SUMMARY = "Easysplash Configuration"
LICENSE = "CLOSED"

SRC_URI += "file://easysplash.default"

do_install() {
    install -Dm 0644 ${WORKDIR}/easysplash.default ${D}${sysconfdir}/default/easysplash
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
