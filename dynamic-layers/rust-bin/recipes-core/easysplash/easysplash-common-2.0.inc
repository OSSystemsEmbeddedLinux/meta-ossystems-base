LICENSE = "Apache-2.0|MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE-2.0;md5=1836efb2eb779966696f473ee8540542"
LIC_FILES_CHKSUM = "file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

SRCREV = "aa7bd290bf112f96c20e7bf7be1fd1d5d691468e"
SRCBRANCH = "master"

SRC_URI = "git://github.com/OSSystems/EasySplash.git;branch=${SRCBRANCH}"

PV = "2.0.0"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += " \
    prefix=${prefix} \
    sysconfdir=${sysconfdir} \
    sbindir=${bindir} \
    base_libdir=${base_libdir} \
    \
    ${PACKAGECONFIG_CONFARGS} \
"
