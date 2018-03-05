PACKAGECONFIG = "python update-alternatives"
PACKAGECONFIG[update-alternatives] = ",,,"

PROVIDES_oel_remove = "${@'virtual/update-alternatives' if 'update-alternatives' not in PACKAGECONFIG.split() else ''}"

do_install_oel_append () {
    if ${@'true' if 'update-alternatives' not in PACKAGECONFIG.split() else 'false'}; then
        rm -f "${D}${bindir}/update-alternatives"
    fi
}
