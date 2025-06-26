FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://options"

do_install:append() {
    install -Dm 0755 ${UNPACKDIR}/options ${D}${sysconfdir}/${PN}/options
}
