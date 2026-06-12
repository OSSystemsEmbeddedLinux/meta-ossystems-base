SUMMARY = "Weston Touch Screen Calibrator Service"
DESCRIPTION = "Installs the Weston touch calibrator service, default configuration, and udev rules."
HOMEPAGE = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base"
BUGTRACKER = "https://github.com/OSSystemsEmbeddedLinux/meta-ossystems-base/issues"
SECTION = "graphics"
CVE_PRODUCT = "weston-touch-calibrator-service"
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
    install -Dm 0644 ${UNPACKDIR}/weston-touch-calibrator.default ${D}${sysconfdir}/default/weston-touch-calibrator
    install -Dm 0644 ${UNPACKDIR}/weston-touch-calibrator.service ${D}${systemd_system_unitdir}/weston-touch-calibrator.service
    install -Dm 0755 ${UNPACKDIR}/save-touch-calibration ${D}${bindir}/save-touch-calibration
    install -Dm 0644 ${UNPACKDIR}/touchscreen.rules ${D}${base_libdir}/udev/rules.d/98-touchscreen.rules
}

FILES:${PN} += "\
    ${sysconfdir}/default \
    ${systemd_system_unitdir} \
    ${base_libdir}/udev/rules.d \
"

RDEPENDS:${PN} += "\
    weston-init \
    weston-touch-calibrator \
"
