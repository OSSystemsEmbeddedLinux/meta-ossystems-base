FILESEXTRAPATHS:prepend:oel := "${THISDIR}/${PN}:"

#The default config file is removed to allow the generation of a new one with the proper parameters
do_install:append:oel() {
    rm ${D}${sysconfdir}/${PN}.conf
}
