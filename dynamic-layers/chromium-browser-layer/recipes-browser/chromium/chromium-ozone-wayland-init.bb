SUMMARY = "Startup script and service for the Chromium Browser using Ozone"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://${BPN}.default \
    file://${BPN}.initd \
    file://${BPN}.service \
"

inherit systemd update-rc.d

## Configuration variables

# URL to load when start.
CHROMIUM_URL        ?= "https://ossystems.com.br"

# Extra arguments to pass to 'chromium' application.
CHROMIUM_EXTRA_ARGS ?= ""

# To start chromium at boot it's necessary to set graphical login as default adding
# this to your image recipe:
#
#   SYSTEMD_DEFAULT_TARGET = "graphical.target"
#
SYSTEMD_SERVICE:${PN} = "chromium-ozone-wayland.service"

# Start after weston-init
INITSCRIPT_NAME = "${BPM}"
INITSCRIPT_PARAMS = "start 10 5 2 . stop 19 0 1 6 ."

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${WORKDIR}/${BPN}.initd ${D}${sysconfdir}/init.d/chromium-ozone-wayland
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_system_unitdir}/chromium-ozone-wayland.service
    fi

    install -Dm 0644 ${WORKDIR}/${BPN}.default ${D}${sysconfdir}/default/chromium-ozone-wayland

    echo ${CHROMIUM_ENV} >> ${D}${sysconfdir}/default/chromium-ozone-wayland

    sed -e 's,@CHROMIUM_URL@,${CHROMIUM_URL},g' \
        -e 's,@CHROMIUM_EXTRA_ARGS@,${CHROMIUM_EXTRA_ARGS},g' \
        -i ${D}${sysconfdir}/default/chromium-ozone-wayland
}

RDEPENDS:${PN} += "chromium-ozone-wayland"

PACKAGE_ARCH = "${MACHINE_ARCH}"
