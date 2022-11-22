FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://psplash-start-override.service \
    file://psplash-systemd-override.service \
"

do_install:append() {
    install -Dm 0644 ${WORKDIR}/psplash-start-override.service ${D}${sysconfdir}/systemd/system/psplash-start.service.d/override.conf
    install -Dm 0644 ${WORKDIR}/psplash-systemd-override.service ${D}${sysconfdir}/systemd/system/psplash-systemd.service.d/override.conf
}
