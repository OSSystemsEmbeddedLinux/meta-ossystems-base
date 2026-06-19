SUMMARY = "Advanced PC speaker beeper"
DESCRIPTION = "A command line tool for Linux that beeps the PC speaker."
HOMEPAGE = "https://github.com/spkr-beep/beep"
BUGTRACKER = "https://github.com/spkr-beep/beep/issues"
SECTION = "console/utils"
LICENSE = "GPL-2.0-only"
RECIPE_MAINTAINER = "O.S. Systems Software LTDA. <contato@ossystems.com.br>"
CVE_PRODUCT = "beep"

LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/spkr-beep/${BPN}.git;protocol=https;nobranch=1"
SRCREV = "11453a79f2cea81832329b06ca3a284aa7a0a52e"

EXTRA_OEMAKE = ' \
               CC="${CC}" \
               CFLAGS="${CFLAGS}" \
               CPPFLAGS="${CPPFLAGS}" \
               LDFLAGS="${LDFLAGS}" \
               common_CFLAGS="-std=gnu99" \
               PANDOC=false \
               prefix="${prefix}" \
               bindir="${bindir}" \
               mandir="${mandir}" \
               '

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake DESTDIR="${D}" CC=false install
}
