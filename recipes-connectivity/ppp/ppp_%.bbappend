FILESEXTRAPATHS:prepend:oel := "${THISDIR}/files:"

SRC_URI:append:oel = " file://options"

do_install:append:oel() {
    install -Dm 0755 ${UNPACKDIR}/options ${D}${sysconfdir}/${PN}/options
}
