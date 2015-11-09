DESCRIPTION = "U-boot bootloader mxsboot tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"
SECTION = "bootloader"
DEPENDS = "openssl"
PROVIDES = "u-boot-mxsboot"

PV = "v2015.10+git${SRCPV}"

SRCREV = "3fb8c1c248f76f59d817848b7a6062f6a58fce97"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"
SRCBRANCH = "2015.10+ossystems"

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
