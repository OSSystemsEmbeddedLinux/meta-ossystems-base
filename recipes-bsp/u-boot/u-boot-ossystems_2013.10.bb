require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"
COMPATIBLE_MACHINE = "(mxs|mx3|mx5|mx6)"

DEPENDS_mxs += "elftosb-native openssl-native"

PROVIDES += "u-boot"

PV = "v2013.10"

SRCREV = "82a70d56402bead4189bf1354818c769e5b97504"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http"

S = "${WORKDIR}/git"

UBOOT_LOGO_BMP ?= "logos/ossystems.bmp"

# By default use O.S. Systems logo
EXTRA_OEMAKE += "LOGO_BMP=${UBOOT_LOGO_BMP}"

# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="-L${STAGING_BASE_LIBDIR_NATIVE} -L${STAGING_LIBDIR_NATIVE}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
