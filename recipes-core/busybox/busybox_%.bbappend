FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append_oel = "\
    file://setsid.cfg \
    file://resize.cfg \
    file://pidof.cfg \
    file://top.cfg \
    file://mdev.cfg \
    file://history.cfg \
"
