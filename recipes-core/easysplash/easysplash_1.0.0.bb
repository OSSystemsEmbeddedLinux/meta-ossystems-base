# Copyright (C) 2015-2020 O.S. Systems Software Ltda. All Rights Reserved
# Released under the MIT and APACHE-2.0 licenses

SUMMARY = "Userspace framebuffer boot animation based on usplash"
DESCRIPTION = "EasySplash is a simple program for animated splash screens \
using OpenGL ES for rendering. It takes as input zip archives containing \
a description and PNG-encoded image frames."
LICENSE = "Apache-2.0|MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE-2.0;md5=1836efb2eb779966696f473ee8540542"
LIC_FILES_CHKSUM = "file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"
SECTION = "base"

DEPENDS = "zlib libpng"

SRCREV = "41aa12405d5aebb3f99b2bf5b183ba4599cba18f"
SRCBRANCH = "1.0.x"
SRC_URI = "\
    git://github.com/OSSystems/easysplash.git;branch=${SRCBRANCH} \
    file://${BPN}.default \
"

PV = "1.0.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig update-rc.d systemd

# We want the binaries to be in /sbin
EXTRA_OECMAKE += "\
    -DCMAKE_INSTALL_PREFIX:PATH=/ \
    -DCTL_FIFO_PATH=/dev/easysplashctl \
    -DEASYSPLASH_PID_FILE=/dev/easysplash.pid \
"

INITSCRIPT_NAME = "${PN}-start"
INITSCRIPT_PARAMS_${PN} = "start 5 S ."

SYSTEMD_SERVICE_${PN} = "${PN}-start.service ${PN}-quit.service"

PACKAGECONFIG_DISTRO ?= "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'sysvinit', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
"

PACKAGECONFIG ?= "${PACKAGECONFIG_DISTRO} swrender"
PACKAGECONFIG_mx6q ?= "${PACKAGECONFIG_DISTRO} vivante-g2d"
PACKAGECONFIG_mx6dl ?= "${PACKAGECONFIG_DISTRO} vivante-g2d"
PACKAGECONFIG_rpi ?= "${PACKAGECONFIG_DISTRO} ${@bb.utils.contains('MACHINE_FEATURES', 'vc4graphics', 'mesa-gbm', 'rpi-dispmanx', d)}"
PACKAGECONFIG_use-mainline-bsp ?= "${PACKAGECONFIG_DISTRO} mesa-gbm"

PACKAGECONFIG[swrender] = "-DDISPLAY_TYPE_SWRENDER=TRUE,-DDISPLAY_TYPE_SWRENDER=FALSE,pixman"
PACKAGECONFIG[vivante-g2d] = "-DDISPLAY_TYPE_G2D=TRUE,-DDISPLAY_TYPE_G2D=FALSE,virtual/libg2d"
# NOTE: not adding -DDISPLAY_TYPE_GLES=FALSE when the packageconfig isn't used
# because both mesa-gbm and vivante-gles2 enable GLES, and DDISPLAY_TYPE_GLES=FALSE
# is anyway the default
PACKAGECONFIG[vivante-gles2] = "-DDISPLAY_TYPE_GLES=TRUE -DEGL_PLATFORM_VIV_FB=TRUE,,virtual/libgles2"
# libgbm is part of the mesa recipe
PACKAGECONFIG[mesa-gbm] = "-DDISPLAY_TYPE_GLES=TRUE -DEGL_PLATFORM_GBM=TRUE,,virtual/libgles2 mesa libdrm udev"
PACKAGECONFIG[rpi-dispmanx] = "-DDISPLAY_TYPE_GLES=TRUE -DEGL_PLATFORM_RPI_DISPMANX=TRUE,,virtual/libgles2 userland"
PACKAGECONFIG[sysvinit] = "-DENABLE_SYSVINIT_SUPPORT=TRUE, -DENABLE_SYSVINIT_SUPPORT=FALSE"
PACKAGECONFIG[systemd] = "-DENABLE_SYSTEMD_SUPPORT=TRUE, -DENABLE_SYSTEMD_SUPPORT=FALSE, systemd"

do_install_append() {
    install -Dm 0644 ${WORKDIR}/${PN}.default ${D}${sysconfdir}/default/${PN}
}

RRECOMMENDS_${PN} = "easysplash-bootanimation-default"

# NXP SoC arch
PACKAGE_ARCH_imx = "${MACHINE_SOCARCH}"
PACKAGE_ARCH_use-mainline-bsp = "${MACHINE_SOCARCH}"

# Raspberry Pi
PACKAGE_ARCH_rpi = "${MACHINE_ARCH}"
