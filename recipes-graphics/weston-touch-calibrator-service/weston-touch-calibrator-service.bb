SUMMARY = "Weston Touch Screen Calibrator Service"
LICENSE = "CLOSED"

SRC_URI = "\
    file://weston-touch-calibrator.default \
    file://weston-touch-calibrator.service \
    file://save-touch-calibration \
    file://touchscreen.rules \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "weston-touch-calibrator.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${WORKDIR}/weston-touch-calibrator.default ${D}${sysconfdir}/default/weston-touch-calibrator
    install -Dm 0644 ${WORKDIR}/weston-touch-calibrator.service ${D}${systemd_system_unitdir}/weston-touch-calibrator.service
    install -Dm 0755 ${WORKDIR}/save-touch-calibration ${D}${bindir}/save-touch-calibration
    install -Dm 0644 ${WORKDIR}/touchscreen.rules ${D}${base_libdir}/udev/rules.d/98-touchscreen.rules
}

RDEPENDS:${PN} += " \
    weston-init \
    weston-touch-calibrator \
"

FILES:${PN} = "\
    ${sysconfdir}/default \
    ${systemd_system_unitdir} \
    ${bindir} \
    ${base_libdir}/udev/rules.d \
"
