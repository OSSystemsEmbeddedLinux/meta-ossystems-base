DESCRIPTION = "Small image with psplash implementation to use as \
an alternative to the splash kernel."

IMAGE_FSTYPES = "cpio.gz.u-boot"
IMAGE_ROOTFS_SIZE = "0"

IMAGE_FEATURES = " \
    splash \
    debug-tweaks \
"

# Avoid installation of syslog
BAD_RECOMMENDATIONS += "busybox-syslog"

# Avoid static /dev
USE_DEVFS = "1"

inherit core-image

CORE_IMAGE_BASE_INSTALL = " \
    initramfs-framework-base \
    initramfs-module-psplash \
    initramfs-module-rootfs \
    initramfs-module-udev \
    packagegroup-core-boot \
"
