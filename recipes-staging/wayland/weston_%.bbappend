FILESEXTRAPATHS:prepend:oel := "${THISDIR}/${PN}:"

PACKAGE_BEFORE_PN:append:oel = " ${PN}-touch-calibrator"

FILES:${PN}-touch-calibrator += "${bindir}/weston-touch-calibrator"
