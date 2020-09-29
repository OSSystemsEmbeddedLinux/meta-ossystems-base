SUMMARY = "Startup script and service for the Cog Browser"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://${BPN}.default \
    file://${BPN}.initd \
    file://${BPN}.service \
"

inherit systemd update-rc.d

## Configuration variables

# The known good values are 'fdo' and 'rdk'; most machines use the 'fdo' as it
# is the preferred backend.
COG_PLATFORM   ?= "fdo"

# Force fullscreen when using 'fdo' platform.
COG_PLATFORM_FDO_VIEW_FULLSCREEN ?= "1"

# URL to load when start.
COG_URL        ?= "https://ossystems.com.br"

# Extra arguments to pass to 'cog' application.
COG_EXTRA_ARGS ?= ""

# To start cog at boot it's necessary to set graphical login as default adding
# this to your image recipe:
#
#   SYSTEMD_DEFAULT_TARGET = "graphical.target"
#
SYSTEMD_SERVICE_${PN} = "cog.service"

# Start after weston-init
INITSCRIPT_NAME = "cog"
INITSCRIPT_PARAMS = "start 10 5 2 . stop 19 0 1 6 ."

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${WORKDIR}/${PN}.initd ${D}${sysconfdir}/init.d/cog
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/${PN}.service ${D}${systemd_system_unitdir}/cog.service
    fi

    install -Dm 0644 ${WORKDIR}/${PN}.default ${D}${sysconfdir}/default/cog

    sed -e 's,@COG_PLATFORM@,${COG_PLATFORM},g' \
        -e 's,@COG_URL@,${COG_URL},g' \
        -e 's,@COG_EXTRA_ARGS@,${COG_EXTRA_ARGS},g' \
        -e 's,@COG_PLATFORM_FDO_VIEW_FULLSCREEN@,${COG_PLATFORM_FDO_VIEW_FULLSCREEN},g' \
        -i ${D}${sysconfdir}/default/cog
}

RDEPENDS_${PN} += "cog"

PACKAGE_ARCH = "${MACHINE_ARCH}"
