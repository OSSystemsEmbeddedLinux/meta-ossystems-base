SUMMARY = "Plugin-driven metrics collection agent"
DESCRIPTION = "The plugin-driven server agent for collecting & reporting metrics"
HOMEPAGE = "https://github.com/influxdata/telegraf"
BUGTRACKER = "https://github.com/influxdata/telegraf/issues"
SECTION = "console/network"
CVE_PRODUCT = "telegraf"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=fe53cff8eef1afa881ea0e6325071ecd"
require ${BPN}-licenses.inc

SRC_URI = "git://github.com/influxdata/telegraf;protocol=https;branch=release-1.38;destsuffix=${BP}/src/${GO_IMPORT} \
           file://telegraf.conf \
"
require ${BPN}-go-mods.inc

SRCREV = "c79b06d58e912124624d029a88bbe182254f0ff4"
S = "${UNPACKDIR}/${BP}"

inherit go-mod go-mod-update-modules systemd

# Avoid dynamic linking as it causes segfault
GO_LINKSHARED = ""

# The binary is large enough that the PLT-to-GOT displacement overflows the
# 28-bit short ARM PLT entry, tripping a BFD_ASSERT in binutils (elf32-arm.c).
# Use 16-byte long PLT entries, which reach the full address space.
GO_EXTLDFLAGS:append:arm = " -Wl,--long-plt"

GO_IMPORT = "github.com/influxdata/telegraf"
GO_INSTALL = "github.com/influxdata/telegraf/cmd/telegraf"

SYSTEMD_SERVICE:${PN} = "${PN}.service"

# go install only extracts the modules in the target's build graph, which is
# arch-specific (klauspost/cpuid is amd64-only), but telegraf-licenses.inc
# lists every module in SRC_URI. Extract the rest from the module cache, as
# go would, so do_populate_lic finds each LICENSE on every architecture.
python telegraf_extract_all_modules() {
    import glob, os, zipfile
    modcache = d.getVar('GOMODCACHE')
    download = os.path.join(modcache, 'cache', 'download')
    for zpath in glob.glob(os.path.join(download, '**', '@v', '*.zip'), recursive=True):
        version = os.path.basename(zpath)[:-len('.zip')]
        escaped = os.path.relpath(os.path.dirname(os.path.dirname(zpath)), download)
        dest = os.path.join(modcache, '%s@%s' % (escaped, version))
        if os.path.isdir(dest):
            continue
        with zipfile.ZipFile(zpath) as z:
            for info in z.infolist():
                name = info.filename.split('@%s/' % version, 1)[1]
                if not name or name.endswith('/'):
                    continue
                target = os.path.join(dest, name)
                os.makedirs(os.path.dirname(target), exist_ok=True)
                with z.open(info) as src, open(target, 'wb') as dst:
                    dst.write(src.read())
}
telegraf_extract_all_modules[doc] = "Extract every Go module left zipped in the module cache, so each module LICENSE exists on every architecture."
do_compile[postfuncs] += "telegraf_extract_all_modules"

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
