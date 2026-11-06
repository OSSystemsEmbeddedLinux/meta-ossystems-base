SUMMARY = "Easysplash Configuration"
DESCRIPTION = "Default EasySplash configuration installed under /etc/default."
HOMEPAGE = "https://github.com/OSSystems/EasySplash"
BUGTRACKER = "https://github.com/OSSystems/EasySplash/issues"
SECTION = "graphics"
CVE_PRODUCT = "easysplash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "file://easysplash.default"

S = "${UNPACKDIR}"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/easysplash.default ${D}${sysconfdir}/default/easysplash
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

BBCLASSEXTEND = ""
