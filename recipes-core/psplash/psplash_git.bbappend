FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_oel = " file://psplash-colors.h"

do_configure_append_oel () {
    # If a psplash-colors.h is provided, use it
    if [ -e ${WORKDIR}/psplash-colors.h ]; then
        cp ${WORKDIR}/psplash-colors.h ${S}
        touch ${S}/psplash.c
    fi
}
