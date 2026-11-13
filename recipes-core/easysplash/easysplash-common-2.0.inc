PV = "2.0.0"

SRC_URI = "git://github.com/OSSystems/EasySplash.git;branch=master;protocol=https"
SRCREV = "7829a12e8d8ff3740eff806cbeb63be96ab4250e"

# Upstream has no tag for 2.0.0, so the version check follows the branch head.
UPSTREAM_CHECK_COMMITS = "1"

EXTRA_OEMAKE += "\
    prefix=${prefix} \
    sysconfdir=${sysconfdir} \
    sbindir=${bindir} \
    base_libdir=${base_libdir} \
    \
    ${PACKAGECONFIG_CONFARGS} \
"
