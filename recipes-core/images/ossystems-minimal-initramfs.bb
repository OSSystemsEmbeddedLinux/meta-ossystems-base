# The very minimal initramfs image capable of booting a device.
#
# Copyright (C) 2017, O.S. Systems Softwares Ltda.  All Rights Reserved
# Released under the MIT license

SUMMARY = "The very minimal initramfs image capable of booting a device"

PACKAGE_INSTALL = "initramfs-module-mdev ${VIRTUAL-RUNTIME_base-utils} ${ROOTFS_BOOTSTRAP_INSTALL}"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "read-only-rootfs"

# No extra locale content
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

BAD_RECOMMENDATIONS += " \
    busybox-udhcpc \
    busybox-syslog \
    initramfs-module-rootfs \
"
