FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_BEFORE_PN = "${PN}-touch-calibrator"

FILES:${PN}-touch-calibrator = "${bindir}/weston-touch-calibrator"
