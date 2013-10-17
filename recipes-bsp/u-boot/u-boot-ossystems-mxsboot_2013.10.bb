DESCRIPTION = "U-boot bootloader mxsboot tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"
SECTION = "bootloader"
DEPENDS = "openssl"
PROVIDES = "u-boot-mxsboot"

PV = "v2013.10"

SRCREV = "82a70d56402bead4189bf1354818c769e5b97504"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC} ${CPPFLAGS}" HOSTLDFLAGS="-L${libdir} -L${base_libdir}" HOSTSTRIP=true CONFIG_MX28=y'

do_compile () {
    oe_runmake tools
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 tools/mxsboot ${D}${bindir}/uboot-mxsboot
    ln -sf uboot-mxsboot ${D}${bindir}/mxsboot
}

BBCLASSEXTEND = "native nativesdk"
