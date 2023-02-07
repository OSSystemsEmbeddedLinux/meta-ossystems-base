SUMMARY = "Modular psplash to initramfs system"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_base-utils}"
RRECOMMENDS:${PN} = "${VIRTUAL-RUNTIME_base-utils-syslog}"

PR = "r4"

inherit allarch

SRC_URI = " \
    file://psplash \
    file://psplash-finish \
"

S = "${WORKDIR}"

do_install() {
    # psplash
    install -Dm 0755 ${WORKDIR}/psplash ${D}/init.d/11-psplash
    install -Dm 0755 ${WORKDIR}/psplash-finish ${D}/init.d/98-psplash_finish
}

PACKAGES = "initramfs-module-psplash"

SUMMARY:initramfs-module-psplash = "initramfs psplash support"
RDEPENDS:initramfs-module-psplash = "initramfs-framework-base psplash"
FILES:initramfs-module-psplash = "/init.d"
