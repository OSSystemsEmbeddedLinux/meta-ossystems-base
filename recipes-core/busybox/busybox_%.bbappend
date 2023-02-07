FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append:oel = "\
    file://setsid.cfg \
    file://resize.cfg \
    file://pidof.cfg \
    file://top.cfg \
    file://mdev.cfg \
    file://history.cfg \
"
