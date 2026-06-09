SUMMARY = "Generate release bundle"
DESCRIPTION = "Generates self-extracting release bundles from the configured platform source tree."
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "devel"
CVE_PRODUCT = "meta-ossystems-base"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit release-bundle-generation

SRC_URI += "file://COPYING.MIT"
S = "${UNPACKDIR}"

BBCLASSEXTEND = ""
