SUMMARY = "Initramfs image with psplash"
DESCRIPTION = "Small image with psplash implementation to use as \
               an alternative to the splash kernel."
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "images"
CVE_PRODUCT = "meta-ossystems-base"
LICENSE = "MIT"

IMAGE_FSTYPES = "cpio.gz.u-boot"
IMAGE_ROOTFS_SIZE = "0"

IMAGE_FEATURES = "splash"

# Avoid installation of syslog
BAD_RECOMMENDATIONS += "busybox-syslog"

# Avoid static /dev
USE_DEVFS = "1"

inherit core-image

CORE_IMAGE_BASE_INSTALL = "\
    initramfs-framework-base \
    initramfs-module-psplash \
    initramfs-module-rootfs \
    initramfs-module-udev \
    packagegroup-core-boot \
"
