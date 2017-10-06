LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

PV = "v2017.09+git${SRCPV}"

SRCREV = "d5e2dd8a4a39cfc9d910334cea328e05fd54c263"
SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"
SRCBRANCH = "2017.09+ossystems"

S = "${WORKDIR}/git"
