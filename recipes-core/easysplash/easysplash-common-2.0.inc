PV = "2.0.0"

SRC_URI = "git://github.com/OSSystems/EasySplash.git;branch=master;protocol=https"
SRCREV = "81a44392efe15fd12ea673f1c828a52e95103eea"

EXTRA_OEMAKE += "\
    prefix=${prefix} \
    sysconfdir=${sysconfdir} \
    sbindir=${bindir} \
    base_libdir=${base_libdir} \
    \
    ${PACKAGECONFIG_CONFARGS} \
"
