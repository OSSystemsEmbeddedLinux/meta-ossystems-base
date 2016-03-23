require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

DEPENDS_mxs += "elftosb-native openssl-native"

PROVIDES += "u-boot"

PV = "v2016.03+git${SRCPV}"

SRCREV = "c29b4676ad6f9ab0d09e6ddf2b42ffe32b9be26f"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"
SRCBRANCH = "2016.03+ossystems"

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
