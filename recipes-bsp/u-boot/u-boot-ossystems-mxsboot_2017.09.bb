DESCRIPTION = "U-boot bootloader mxsboot tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SECTION = "bootloader"
DEPENDS = "openssl"
PROVIDES = "u-boot-mxsboot"

PV = "v2017.09+git${SRCPV}"

SRCREV = "d5e2dd8a4a39cfc9d910334cea328e05fd54c263"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"
SRCBRANCH = "2017.09+ossystems"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC} ${CPPFLAGS}" HOSTLDFLAGS="-L${libdir} -L${base_libdir}" HOSTSTRIP=true CONFIG_MX28=y'

do_configure () {
    oe_runmake sandbox_defconfig
}

do_compile () {
    oe_runmake tools-only
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 tools/mxsboot ${D}${bindir}/uboot-mxsboot
    ln -sf uboot-mxsboot ${D}${bindir}/mxsboot
}

BBCLASSEXTEND = "native nativesdk"
