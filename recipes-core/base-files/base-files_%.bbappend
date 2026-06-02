FILESEXTRAPATHS:prepend:oel := "${THISDIR}/${PN}:"

SRC_URI:append:oel = " file://profile.d_locale.sh"

do_install:append:oel() {
    install -Dm 0644 ${S}/profile.d_locale.sh ${D}${sysconfdir}/profile.d/locale.sh

    if [ "${@d.getVar('DEFAULT_SYSTEM_LOCALE', True)}" != "" ]; then
        install -d ${D}${sysconfdir}
        printf 'LANG=%s\n' "${DEFAULT_SYSTEM_LOCALE}" > ${D}${sysconfdir}/locale.conf
    fi
}
