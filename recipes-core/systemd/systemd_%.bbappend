FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://watchdog-reduce-watchdog-pings-in-timeout-interval.patch"

RDEPENDS_${PN} += "util-linux-umount"
