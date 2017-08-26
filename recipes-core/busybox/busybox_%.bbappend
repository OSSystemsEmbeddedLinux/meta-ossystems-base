FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = "\
    file://setsid.cfg \
    file://resize.cfg \
    file://pidof.cfg \
    file://top.cfg \
    file://mdev.cfg \
"
