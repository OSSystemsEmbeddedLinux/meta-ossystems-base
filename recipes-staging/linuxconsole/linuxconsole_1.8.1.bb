SUMMARY = "Linux Console Project"
DESCRIPTION = "This project maintains the Linux Console tools, which include \
               utilities to test and configure joysticks, connect legacy devices to the kernel's \
               input subsystem (providing support for serial mice, touchscreens etc.), and test \
               the input event layer."
HOMEPAGE = "https://sourceforge.net/projects/linuxconsole"
BUGTRACKER = "https://sourceforge.net/p/linuxconsole/bugs/"
SECTION = "console/utils"
CVE_PRODUCT = "linuxconsole"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "libsdl2${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' systemd', '', d)}"

SRC_URI = "\
    ${SOURCEFORGE_MIRROR}/linuxconsole/linuxconsoletools-${PV}.tar.bz2 \
    file://51-these-are-not-joysticks-rm.rules \
    file://60-joystick.rules \
    file://inputattachctl \
    file://inputattach.service \
"

SRC_URI[sha256sum] = "4da29745c782b7db18f5f37c49e77bf163121dd3761e2fc7636fa0cbf35c2456"

S = "${UNPACKDIR}/linuxconsoletools-${PV}"

inherit systemd pkgconfig

LINUXCONSOLE_UDEVDIR = "${nonarch_base_libdir}/udev"
LINUXCONSOLE_UPSTREAM_UDEVDIR = "${base_prefix}/lib/udev"

EXTRA_OEMAKE = "DESTDIR=${D} PREFIX=${prefix} -C utils"
EXTRA_OEMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'SYSTEMD_SUPPORT=1', '', d)}"

SYSTEMD_PACKAGES += "inputattach"
SYSTEMD_SERVICE:inputattach = "inputattach.service"
SYSTEMD_AUTO_ENABLE:inputattach = "enable"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install

    if [ -e ${D}${LINUXCONSOLE_UPSTREAM_UDEVDIR}/js-set-enum-leds ] && [ "${LINUXCONSOLE_UPSTREAM_UDEVDIR}" != "${LINUXCONSOLE_UDEVDIR}" ]; then
        install -Dm 0755 ${D}${LINUXCONSOLE_UPSTREAM_UDEVDIR}/js-set-enum-leds ${D}${LINUXCONSOLE_UDEVDIR}/js-set-enum-leds
        install -Dm 0644 ${D}${LINUXCONSOLE_UPSTREAM_UDEVDIR}/rules.d/80-stelladaptor-joystick.rules ${D}${LINUXCONSOLE_UDEVDIR}/rules.d/80-stelladaptor-joystick.rules
        rm -rf ${D}${LINUXCONSOLE_UPSTREAM_UDEVDIR}
        rmdir --ignore-fail-on-non-empty ${D}${base_prefix}/lib || true
    fi

    install -Dm 0644 ${UNPACKDIR}/51-these-are-not-joysticks-rm.rules ${D}${LINUXCONSOLE_UDEVDIR}/rules.d/51-these-are-not-joysticks-rm.rules
    install -Dm 0644 ${UNPACKDIR}/60-joystick.rules ${D}${LINUXCONSOLE_UDEVDIR}/rules.d/60-joystick.rules

    install -Dm 0644 ${UNPACKDIR}/inputattach.service ${D}${systemd_system_unitdir}/inputattach.service
    install -Dm 0755 ${UNPACKDIR}/inputattachctl ${D}${bindir}/inputattachctl
}

PACKAGES += "inputattach joystick"

# We won't package any file here as we are following the same packaging schema
# Debian does and we are splitting it in 'inputattach' and 'joystick' packages.
# nooelint: oelint.var.filesoverride  intentionally empties the main package (see above)
FILES:${PN} = ""

FILES:inputattach += "\
    ${bindir}/inputattach \
    ${bindir}/inputattachctl \
    ${systemd_system_unitdir}/inputattach.service \
"

FILES:joystick += "\
    ${bindir}/evdev-joystick \
    ${bindir}/ffcfstress \
    ${bindir}/ffmvforce \
    ${bindir}/ffset \
    ${bindir}/fftest \
    ${bindir}/jscal \
    ${bindir}/jscal-restore \
    ${bindir}/jscal-store \
    ${bindir}/jstest \
    ${datadir}/joystick \
    ${LINUXCONSOLE_UDEVDIR}/rules.d/51-these-are-not-joysticks-rm.rules \
    ${LINUXCONSOLE_UDEVDIR}/js-set-enum-leds \
    ${LINUXCONSOLE_UDEVDIR}/rules.d/60-joystick.rules \
    ${LINUXCONSOLE_UDEVDIR}/rules.d/80-stelladaptor-joystick.rules \
"

RDEPENDS:inputattach += "inputattach-config"

RDEPENDS:joystick += "\
    bash \
    gawk \
"
