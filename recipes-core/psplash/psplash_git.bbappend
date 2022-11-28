FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:oel = " \
    file://psplash-colors.h \
    file://psplash-start-override.service \
    file://psplash-systemd-override.service \
"

do_configure:append:oel () {
    # If a psplash-colors.h is provided, use it
    if [ -e ${WORKDIR}/psplash-colors.h ]; then
        cp ${WORKDIR}/psplash-colors.h ${S}
        touch ${S}/psplash.c
    fi
}

do_install:append:oel () {
    install -Dm 0644 ${WORKDIR}/psplash-start-override.service ${D}${sysconfdir}/systemd/system/psplash-start.service.d/override.conf
    install -Dm 0644 ${WORKDIR}/psplash-systemd-override.service ${D}${sysconfdir}/systemd/system/psplash-systemd.service.d/override.conf
}
