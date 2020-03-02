SUMMARY = "HTML5 demo useful for test browser performance"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/OSSystems/${BPN}.git"
SRCREV = "e9ede7082b9e9e919e25b3a147d18e46dc3802f0"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${datadir}/${PN}
    cp -rf ${WORKDIR}/git/* ${D}${datadir}/${PN}
}

FILES_${PN} += "${datadir}/${PN}"
