SUMMARY = "Captive portal Wi-Fi configuration utility"
DESCRIPTION = "WiFi Connect dynamically sets Wi-Fi configuration via a captive portal."
HOMEPAGE = "https://github.com/balena-os/wifi-connect"
BUGTRACKER = "https://github.com/balena-os/wifi-connect/issues"
SECTION = "connectivity"
CVE_PRODUCT = "wifi-connect"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3bfd34238ccc26128aef96796a8bbf97"

DEPENDS = "dbus"
SRCREV_wifi-connect = "5bd4c1bea548fb5714bedb18bbd12f088d5fa407"
SRCREV_network-manager = "4da2e6a57de16b6ae911f74321f929d78af8b1ba"
SRCREV_FORMAT = "wifi-connect_network-manager"

SRC_URI += "\
    git://github.com/balena-os/wifi-connect;branch=master;protocol=https;name=wifi-connect \
    git://github.com/balena-io-modules/network-manager.git;branch=master;protocol=https;name=network-manager;destsuffix=network-manager \
    https://github.com/balena-os/wifi-connect/releases/download/v${PV}/wifi-connect-ui.tar.gz;downloadfilename=wifi-connect-ui-${PV}.tar.gz;subdir=wifi-connect-ui;name=ui \
    file://0001-wifi-connect-refresh-lockfile-for-params-num-features.patch \
    file://0001-traitobject-remove-duplicate-marker-trait-impls.patch;patchdir=${UNPACKDIR}/cargo_home/bitbake/traitobject-0.1.0/ \
    file://0001-params-disable-num-default-features.patch;patchdir=${UNPACKDIR}/cargo_home/bitbake/params-0.8.0/ \
    file://wifi-connect.service \
    file://wifi-connect-start.sh \
"

SRC_URI[ui.sha256sum] = "e57a3cec559729516decf892beb1e7f191b23e71b2e13bcd43d36b980034ffbe"

RDEPENDS:${PN} = "dnsmasq networkmanager"

inherit cargo pkgconfig systemd

require ${BPN}-4.11.84-crates.inc

EXTRA_OECARGO_PATHS += "${UNPACKDIR}/network-manager"

SYSTEMD_SERVICE:${PN} = "wifi-connect.service"

do_install:append() {
    install -d ${D}${datadir}/wifi-connect/ui
    cp -r ${UNPACKDIR}/wifi-connect-ui/. ${D}${datadir}/wifi-connect/ui/

    install -Dm 0644 ${UNPACKDIR}/wifi-connect.service ${D}${systemd_system_unitdir}/wifi-connect.service
    install -Dm 0755 ${UNPACKDIR}/wifi-connect-start.sh ${D}${bindir}/wifi-connect-start.sh
}
