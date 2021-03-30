FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://options"

do_install_append() {
    install -Dm 0755 ${WORKDIR}/options ${D}${sysconfdir}/${PN}/options
}
