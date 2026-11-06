SUMMARY = "Default EasySplash Animation intended for general use"
DESCRIPTION = "Default EasySplash animation assets intended for general use."
HOMEPAGE = "https://github.com/OSSystems/EasySplash"
BUGTRACKER = "https://github.com/OSSystems/EasySplash/issues"
SECTION = "graphics"
CVE_PRODUCT = "easysplash"
LICENSE = "Apache-2.0 OR MIT"
LIC_FILES_CHKSUM = "\
    file://LICENSE-APACHE-2.0;md5=1836efb2eb779966696f473ee8540542 \
    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d \
"
LICENSE_FLAGS = "commercial"
LICENSE_FLAGS_DETAILS[commercial] = "Requires gstreamer1.0-libav, which is license-flagged commercial."

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

RDEPENDS:${PN} += "\
    gstreamer1.0-libav \
    gstreamer1.0-plugins-bad-camerabin \
    gstreamer1.0-plugins-bad-kms \
    gstreamer1.0-plugins-base-playback \
    gstreamer1.0-plugins-base-typefindfunctions \
    gstreamer1.0-plugins-base-videoconvertscale \
    gstreamer1.0-plugins-good-deinterlace \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-good-multifile \
    gstreamer1.0-plugins-good-videofilter \
"

BBCLASSEXTEND = ""
