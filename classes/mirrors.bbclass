# nooelint: oelint.vars.bbvars.PREMIRRORS  set here on purpose: this is the project mirror class
PREMIRRORS:append = " \
    git://sources.redhat.com/ git://sourceware.org/ \
"

# nooelint: oelint.vars.bbvars.MIRRORS  set here on purpose: this is the project mirror class
MIRRORS += "\
    ${KERNELORG_MIRROR}/ https://kernel.googlesource.com/ \
    ${KERNELORG_MIRROR}/ http://mirror.nexcess.net/kernel.org/ \
    ${KERNELORG_MIRROR}/ http://mirror.gbxs.net/pub/ \
    ${APACHE_MIRROR}/ http://archive.apache.org/dist/ \
    ${DEBIAN_MIRROR}/ ftp://archive.debian.org/debian/pool/ \
    git://git.kernel.org/pub/ http://mirror.nexcess.net/kernel.org/ \
    \
    (ftp|https?)://.*/.* http://autobuilder.yoctoproject.org/sources/ \
    (ftp|https?)://.*/.* http://sources.openembedded.org/ \
    (ftp|https?)://.*/.* http://www.angstrom-distribution.org/unstable/sources/ \
    \
    (cvs|svn|git|gitsm|hg|bzr|osc|p4|svk)://.*/.* http://downloads.yoctoproject.org/mirror/sources/ \
    (cvs|svn|git|gitsm|hg|bzr|osc|p4|svk)://.*/.* http://autobuilder.yoctoproject.org/sources/ \
    (cvs|svn|git|gitsm|hg|bzr|osc|p4|svk)://.*/.* http://sources.openembedded.org/ \
    (cvs|svn|git|gitsm|hg|bzr|osc|p4|svk)://.*/.* http://www.angstrom-distribution.org/unstable/sources/ \
"
