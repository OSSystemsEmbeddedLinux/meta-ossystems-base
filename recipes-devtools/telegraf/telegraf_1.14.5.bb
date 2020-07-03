DESCRIPTION = "The plugin-driven server agent for collecting & reporting metrics"
HOMEPAGE = "https://github.com/influxdata/telegraf"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/src/${GO_IMPORT}/LICENSE;md5=4c87a94f9ef84eb3e8c5f0424fef3b9e"

SRC_URI = "git://github.com/influxdata/telegraf;branch=release-1.14"
SRCREV = "e77ce3d11d2b3d2f66e85921142d4927752054b2"

inherit go-mod

GO_IMPORT = "github.com/influxdata/telegraf"
GO_INSTALL = "github.com/influxdata/telegraf/cmd/telegraf"

do_install_append() {
    # FIXME: This has mixed architecture files and causes errors during
    # packaging
    rm -rf ${D}${libdir}/go/pkg/mod ${D}${libdir}/go/pkg/sumdb

    # Fix the python version to use.
    sed -i -e's,^#!/usr/bin/python,#!/usr/bin/env python,' ${D}${libdir}/go/src/${GO_IMPORT}/scripts/build.py
}

RDEPENDS_${PN}-dev += "bash"
