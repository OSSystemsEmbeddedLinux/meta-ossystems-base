DESCRIPTION = " \
    WiFi Connect is a utility for dynamically setting the WiFi configuration on a Linux device via a captive portal. \
    WiFi credentials are specified by connecting with a mobile phone or laptop to the access point that WiFi Connect creates. \
"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3bfd34238ccc26128aef96796a8bbf97"

inherit cargo cargo-update-recipe-crates pkgconfig systemd

require ${BPN}-crates.inc

DEPENDS = "dbus"

SRCREV_wifi-connect = "ac20e664d630712593f959a41cb29953686395a8"
SRC_URI += " \
    git://github.com/balena-os/wifi-connect;branch=master;protocol=https;name=${BPN};name=${BPN};destsuffix=${BPN} \
    file://wifi-connect.service \
    file://wifi-connect-start.sh \
"

S = "${WORKDIR}/wifi-connect"

SRCREV_FORMAT .= "_wifi-connect"

## network-manager
SRCREV_network-manager = "4da2e6a57de16b6ae911f74321f929d78af8b1ba"
SRC_URI += "git://github.com/balena-io-modules/network-manager.git;branch=master;protocol=https;name=network-manager;destsuffix=network-manager"

SRCREV_FORMAT .= "_network-manager"
EXTRA_OECARGO_PATHS += "${WORKDIR}/network-manager"

PV .= "+${SRCPV}"

SYSTEMD_SERVICE:${PN} = "wifi-connect.service"

do_install:append() {
    install -d ${D}${datadir}/wifi-connect/ui
    cp -r ${S}/ui/build/* ${D}${datadir}/wifi-connect/ui

    install -Dm 0644 ${WORKDIR}/wifi-connect.service ${D}${systemd_system_unitdir}/wifi-connect.service
    install -Dm 0755 ${WORKDIR}/wifi-connect-start.sh ${D}${bindir}/wifi-connect-start.sh
}

RDEPENDS:${PN} = "networkmanager"
