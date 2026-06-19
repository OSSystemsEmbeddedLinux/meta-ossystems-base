SUMMARY = "Modular psplash to initramfs system"
SUMMARY:initramfs-module-psplash = "initramfs psplash support"
DESCRIPTION = "Adds psplash start and finish hooks to initramfs-framework images."
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "base"
CVE_PRODUCT = "initramfs-framework-psplash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r4"

SRC_URI = "\
    file://COPYING.MIT \
    file://psplash \
    file://psplash-finish \
"

S = "${UNPACKDIR}"

PACKAGES = "initramfs-module-psplash"

inherit allarch

do_install() {
    # psplash
    install -Dm 0755 ${S}/psplash ${D}/init.d/11-psplash
    install -Dm 0755 ${S}/psplash-finish ${D}/init.d/98-psplash_finish
}

FILES:initramfs-module-psplash += "/init.d"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_base-utils}"
RDEPENDS:initramfs-module-psplash = "initramfs-framework-base psplash"
RRECOMMENDS:${PN} = "${VIRTUAL-RUNTIME_base-utils-syslog}"
