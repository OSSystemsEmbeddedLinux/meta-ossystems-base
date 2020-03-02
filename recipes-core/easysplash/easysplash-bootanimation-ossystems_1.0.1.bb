SUMMARY = "O.S. Systems animation used for image demos"
LICENSE = "APACHE-2.0|MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "http://download.ossystems.com.br/tarballs/${PN}-${PV}.zip;unpack=0"

SRC_URI[md5sum] = "e721376508827f79382463a8dcde1cb8"
SRC_URI[sha256sum] = "472f0e5cf1dc865a450d1b87d3e93dcbb7ae5e16021d5864a01a6d28ff24b0ca"

S = "${WORKDIR}"

inherit easysplash-animation
