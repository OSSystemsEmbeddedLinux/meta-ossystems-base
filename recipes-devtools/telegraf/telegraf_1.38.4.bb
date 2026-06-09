SUMMARY = "Plugin-driven metrics collection agent"
DESCRIPTION = "The plugin-driven server agent for collecting & reporting metrics"
HOMEPAGE = "https://github.com/influxdata/telegraf"
BUGTRACKER = "https://github.com/influxdata/telegraf/issues"
SECTION = "console/network"
CVE_PRODUCT = "telegraf"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=fe53cff8eef1afa881ea0e6325071ecd"

SRC_URI = "git://github.com/influxdata/telegraf;protocol=https;branch=release-1.38;destsuffix=${BP}/src/${GO_IMPORT} \
           file://telegraf.conf \
"

SRCREV = "c79b06d58e912124624d029a88bbe182254f0ff4"
S = "${UNPACKDIR}/${BP}"

inherit go-mod systemd

# Avoid dynamic linking as it causes segfault
GO_LINKSHARED = ""

GO_IMPORT = "github.com/influxdata/telegraf"
GO_INSTALL = "github.com/influxdata/telegraf/cmd/telegraf"

SYSTEMD_SERVICE:${PN} = "${PN}.service"

do_compile[network] = "1"

do_install:append() {
    # FIXME: This has mixed architecture files and causes errors during
    # packaging
    rm -rf ${D}${libdir}/go/pkg/mod ${D}${libdir}/go/pkg/sumdb
    install -Dm 0644 ${UNPACKDIR}/telegraf.conf ${D}${sysconfdir}/${PN}/${PN}.conf
    install -Dm 0644 ${S}/src/${GO_IMPORT}/scripts/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
    install -d ${D}${sysconfdir}/${PN}/${PN}.d
    install -d ${D}${sysconfdir}/tmpfiles.d
    echo "d ${localstatedir}/log/${PN} 0755 root root -" > ${D}${sysconfdir}/tmpfiles.d/${PN}.conf
}

RDEPENDS:${PN}-dev += "bash"
