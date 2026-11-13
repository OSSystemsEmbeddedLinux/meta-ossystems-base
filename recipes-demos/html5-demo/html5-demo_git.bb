SUMMARY = "HTML5 demo useful for test browser performance"
DESCRIPTION = "Installs the O.S. Systems HTML5 browser performance demo."
HOMEPAGE = "https://github.com/OSSystems/html5-demo"
BUGTRACKER = "https://github.com/OSSystems/html5-demo/issues"
SECTION = "demos"
CVE_PRODUCT = "html5-demo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://../COPYING.MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    git://github.com/OSSystems/${BPN}.git;protocol=https;branch=master \
    file://COPYING.MIT \
"
SRCREV = "f6ce362a53e4ce5405383bef94864adf10777377"

# Upstream has no release tags: report new commits on the branch instead.
UPSTREAM_CHECK_COMMITS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${datadir}/${PN}
    cd ${S}
    find * -type d -exec install -d ${D}${datadir}/${PN}/{} \;
    find * -type f -exec install -m 0644 {} ${D}${datadir}/${PN}/{} \;
}

FILES:${PN} += "${datadir}/${PN}"
