DESCRIPTION = "EasySplash is an application that runs early the OS boot for showing graphical \
animation while the boot process itself happens in the background. \
The app is built on Rust and binds to GStreamer, so it can run in any backend that supports both."

require easysplash-common-2.0.inc

inherit cargo_bin pkgconfig systemd update-rc.d

DEPENDS = "glib-2.0 gstreamer1.0"

INITSCRIPT_NAME = "${PN}-start"
INITSCRIPT_PARAMS_${PN} = "start 5 S ."

SYSTEMD_SERVICE_${PN} = "${PN}-start.service ${PN}-quit.service"

EXTRA_CARGO_FLAGS += " \
    ${@bb.utils.contains('PACKAGECONFIG', 'systemd', '--features systemd', '', d)} \
"

PACKAGECONFIG ?= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'sysvinit', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
"

PACKAGECONFIG[sysvinit] = "INIT=1, INIT=0,"
PACKAGECONFIG[systemd] = "SYSTEMD=1, SYSTEM=0, systemd"

do_install_append() {
    (cd ${S} ; oe_runmake install-service DESTDIR=${D})
    rm ${D}${sysconfdir}/default/easysplash
}

RDEPENDS_${PN} += "easysplash-config"
