SUMMARY = "Easysplash Configuration"
DESCRIPTION = "Default EasySplash configuration installed under /etc/default."
HOMEPAGE = "https://github.com/OSSystems/EasySplash"
BUGTRACKER = "https://github.com/OSSystems/EasySplash/issues"
SECTION = "graphics"
CVE_PRODUCT = "easysplash"
LICENSE = "CLOSED"

SRC_URI += "file://easysplash.default"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/easysplash.default ${D}${sysconfdir}/default/easysplash
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

BBCLASSEXTEND = ""
