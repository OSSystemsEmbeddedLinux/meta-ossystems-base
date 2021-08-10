FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://profile.d_locale.sh"

DEFAULT_SYSTEM_LOCALE ??= ""

do_generate_locale_conf() {
    if [ "${@d.getVar('DEFAULT_SYSTEM_LOCALE', True)}" != "" ]; then
        cat > ${WORKDIR}/locale.conf <<EOF
LANG=${DEFAULT_SYSTEM_LOCALE}
EOF
    fi
}
addtask do_generate_locale_conf after do_compile before do_install

do_install:append() {
    install -Dm 0644 ${WORKDIR}/profile.d_locale.sh ${D}${sysconfdir}/profile.d/locale.sh

    if [ "${@d.getVar('DEFAULT_SYSTEM_LOCALE', True)}" != "" ]; then
        install -Dm 0644 ${WORKDIR}/locale.conf ${D}${sysconfdir}/locale.conf
    fi
}
