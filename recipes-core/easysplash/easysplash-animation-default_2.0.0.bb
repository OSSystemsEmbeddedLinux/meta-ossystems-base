SUMMARY = "Default EasySplash Animation intended for general use"
LICENSE = "CLOSED"

require easysplash-common-2.0.inc

inherit easysplash-animation

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    (cd ${S} ; oe_runmake install-glowing-animation DESTDIR=${D})

    # We will handle the default animation using the update-alternatives system.
    rm ${D}${base_libdir}/easysplash/animation

    # Renamed the installed animations to more meaningful names for use
    # in the system.
    mv ${D}${base_libdir}/easysplash/glowing-logo ${D}${base_libdir}/easysplash/${PN}
}

ALTERNATIVE_PRIORITY[animation] = "10"

RDEPENDS:${PN} += " \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-base-playback \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-bad-kms \
    gstreamer1.0-plugins-bad-camerabin \
    gstreamer1.0-plugins-good-videofilter \
    gstreamer1.0-plugins-base-videoconvert \
    gstreamer1.0-plugins-base-videoscale \
    gstreamer1.0-plugins-good-deinterlace \
    gstreamer1.0-plugins-good-multifile \
    gstreamer1.0-plugins-base-typefindfunctions \
"
