SUMMARY = "Basic init system for constrained systems"
DESCRIPTION = "Tiny init system for constrained systems based on BusyBox."
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "base"
CVE_PRODUCT = "tiny-init-system"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=40eb1f165a0cf177f2660b7d4223cc56"

SRC_URI = "\
    file://COPYING.MIT \
    file://init \
    file://rc.local.sample \
"

S = "${UNPACKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -m 0755 ${S}/init ${D}
    install -Dm 0755 ${S}/rc.local.sample ${D}${sysconfdir}/rc.local.sample
}

FILES:${PN} += "/init ${sysconfdir}/rc.local.sample"
RDEPENDS:${PN} = "busybox"

BBCLASSEXTEND = ""
