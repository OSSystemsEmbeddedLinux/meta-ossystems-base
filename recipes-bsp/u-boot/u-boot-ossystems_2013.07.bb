require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
COMPATIBLE_MACHINE = "(mxs|mx3|mx5|mx6)"

DEPENDS_mxs += "elftosb-native"

PROVIDES += "u-boot"

PV = "v2013.07"

SRCREV = "9ea6798ba8a8c82b4f0b8b75c98d52fc5be69239"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http"

S = "${WORKDIR}/git"

UBOOT_LOGO_BMP ?= "logos/ossystems.bmp"

# By default use O.S. Systems logo
EXTRA_OEMAKE += "LOGO_BMP=${UBOOT_LOGO_BMP}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
