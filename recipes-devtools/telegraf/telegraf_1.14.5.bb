DESCRIPTION = "The plugin-driven server agent for collecting & reporting metrics"
HOMEPAGE = "https://github.com/influxdata/telegraf"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/src/${GO_IMPORT}/LICENSE;md5=4c87a94f9ef84eb3e8c5f0424fef3b9e"

SRC_URI = "git://github.com/influxdata/telegraf;branch=release-1.14"
SRCREV = "e77ce3d11d2b3d2f66e85921142d4927752054b2"

inherit go-mod systemd

# Avoid dynamic linking as it causes segfault
GO_LINKSHARED = ""

GO_IMPORT = "github.com/influxdata/telegraf"
GO_INSTALL = "github.com/influxdata/telegraf/cmd/telegraf"

SYSTEMD_SERVICE_${PN} = "${PN}.service"

do_install_append() {
    # FIXME: This has mixed architecture files and causes errors during
    # packaging
    rm -rf ${D}${libdir}/go/pkg/mod ${D}${libdir}/go/pkg/sumdb

    # Fix the python version to use.
    sed -i -e's,^#!/usr/bin/python,#!/usr/bin/env python,' ${D}${libdir}/go/src/${GO_IMPORT}/scripts/build.py

    install -Dm 0644 ${S}/src/${GO_IMPORT}/etc/${PN}.conf ${D}${sysconfdir}/${PN}/${PN}.conf
    install -Dm 0644 ${S}/src/${GO_IMPORT}/scripts/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
    install -d ${D}${sysconfdir}/${PN}/${PN}.d
    install -d ${D}${sysconfdir}/tmpfiles.d
    echo "d /var/log/${PN} 0755 root root -" > ${D}${sysconfdir}/tmpfiles.d/${PN}.conf
}

RDEPENDS_${PN}-dev += "bash"
