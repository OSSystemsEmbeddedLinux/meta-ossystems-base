require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

DEPENDS_mxs += "elftosb-native openssl-native"

PROVIDES += "u-boot"

PV = "v2015.10+git${SRCPV}"

SRCREV = "3fb8c1c248f76f59d817848b7a6062f6a58fce97"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"
SRCBRANCH = "2015.10+ossystems"

S = "${WORKDIR}/git"

UBOOT_LOGO_BMP ?= "${S}/tools/logos/ossystems.bmp"

# By default use O.S. Systems logo
EXTRA_OEMAKE += "LOGO_BMP=${UBOOT_LOGO_BMP}"

# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="-L${STAGING_BASE_LIBDIR_NATIVE} -L${STAGING_LIBDIR_NATIVE}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mxs|mx3|mx5|mx6)"
