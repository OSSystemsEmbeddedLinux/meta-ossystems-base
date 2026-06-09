SUMMARY = "inputattach configuration file"
DESCRIPTION = "Installs the default inputattach configuration used by the inputattach service."
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "base"
CVE_PRODUCT = "inputattach-config"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "\
    file://GPL-2.0 \
    file://inputattach.conf \
"

S = "${UNPACKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/inputattach.conf ${D}${sysconfdir}/inputattach.conf
}
