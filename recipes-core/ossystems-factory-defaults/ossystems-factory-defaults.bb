SUMMARY = "Tool to restore image state to factory defaults"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://factory-defaults \
    file://${BPN}.service \
"

inherit ossystems-factory-defaults-base systemd update-rc.d

INITSCRIPT_NAME = "factory-defaults"
INITSCRIPT_PARAMS = "start 39 S ."

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "${PN}.service"

do_configure() {
    sed -e 's,__OSSYSTEMS_FACTORY_DEFAULTS_DIR__,${OSSYSTEMS_FACTORY_DEFAULTS_DIR}, ;
            s,__OSSYSTEMS_FACTORY_DEFAULTS_HOOKS_DIR__,${OSSYSTEMS_FACTORY_DEFAULTS_HOOKS_DIR}, ;
            s,__OSSYSTEMS_FACTORY_DEFAULTS_RUNTIME_DIR__,${OSSYSTEMS_FACTORY_DEFAULTS_RUNTIME_DIR},' \
        -i ${UNPACKDIR}/factory-defaults
}

do_install() {
    install -d ${D}${sysconfdir}/factory-defaults.d
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -Dm 0755 ${UNPACKDIR}/factory-defaults ${D}${bindir}/factory-defaults
        install -Dm 0644 ${UNPACKDIR}/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
    else
        install -Dm 0755 ${UNPACKDIR}/factory-defaults ${D}${sysconfdir}/init.d/factory-defaults
    fi
}
