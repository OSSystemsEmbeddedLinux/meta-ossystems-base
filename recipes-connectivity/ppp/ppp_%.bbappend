FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://options"

do_install:append() {
    install -Dm 0755 ${WORKDIR}/options ${D}${sysconfdir}/${PN}/options
}
