SUMMARY = "O.S. Systems default boot animation for easysplash"
LICENSE = "Apache-2.0|MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "http://download.ossystems.com.br/tarballs/${BPN}-${PV}.zip;unpack=0"
SRC_URI[md5sum] = "882345727cdea329f4d43484c3b3835f"
SRC_URI[sha256sum] = "5f77a53887d1fe2774aa319853ed5288950089a74507717ffb9071dc5e0e2d7e"

S = "${WORKDIR}"

inherit easysplash-animation

ALTERNATIVE_PRIORITY[bootanimation] = "10"
