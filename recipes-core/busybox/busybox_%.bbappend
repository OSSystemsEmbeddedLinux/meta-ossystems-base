FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append_oel = "\
    file://setsid.cfg \
    file://resize.cfg \
    file://pidof.cfg \
    file://top.cfg \
    file://mdev.cfg \
"
