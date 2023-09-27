SUMMARY = "Startup script and service for the Kiosk Browser"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://${BPN}.default \
    file://${BPN}.initd \
    file://${BPN}.service \
    file://${BPN}.conf \
"

inherit systemd update-rc.d

## Configuration variables
# Defines the URL to load when start.
QT_KIOSK_URL ?= "https://ossystems.com.br"

# Defines the time to activate the screen saving mode in milliseconds (0 to disable).
QT_KIOSK_SCREENSAVER_TIME ?= "0"

# Defines the time to restart the browser after entering in screen saving mode (0 to disable).
QT_KIOSK_RESTART_TIME ?= "0"


SYSTEMD_SERVICE:${PN} = "qt-kiosk-browser.service"

# Start after weston-init
INITSCRIPT_PARAMS = "start 10 5 2 . stop 19 0 1 6 ."

do_configure[noexec] = "1"
do_compile[noexec] = "1"

INITSCRIPT_NAME = "qt-kiosk-browser"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${WORKDIR}/${PN}.initd ${D}${sysconfdir}/init.d/kiosk
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/${PN}.service ${D}${systemd_system_unitdir}/qt-kiosk-browser.service
    fi

    install -Dm 0644 ${WORKDIR}/${PN}.default ${D}${sysconfdir}/default/qt-kiosk-browser

    install -Dm 0644 ${WORKDIR}/${PN}.conf ${D}${sysconfdir}/${PN}.conf

    sed -e 's,@QT_KIOSK_URL@,${QT_KIOSK_URL},g' \
    -e 's,@QT_KIOSK_RESTART_TIME@,${QT_KIOSK_RESTART_TIME},g' \
    -e 's,@QT_KIOSK_SCREENSAVER_TIME@,${QT_KIOSK_SCREENSAVER_TIME},g' \
    -i ${D}${sysconfdir}/${PN}.conf
}

RDEPENDS:${PN} += "qt-kiosk-browser"

PACKAGE_ARCH = "${MACHINE_ARCH}"
