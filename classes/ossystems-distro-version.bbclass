# -*- python -*-
# ossystems-distro-version.bbclass provides a DISTRO_VERSION variable
# that can be used to identify the release of a product.
#
# Copyright (C) 2009-2015, O.S. Systems Softwares Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

def ossystems_get_distro_version(d):
    import re, os, os.path

    devel_version_re = "^((\d+\.)+\d+)-\d+-g\w+$"
    stable_version_re = "^(\d+\.)+\d+$"

    bspdir = d.getVar('PLATFORM_ROOT_DIR', True)
    layerdir = os.path.join(bspdir, '.repo', 'manifests')

    cmd = 'cd %s ; git describe --always 2> /dev/null' % (layerdir)
    output = os.popen(cmd).read().strip()

    if re.compile(stable_version_re).match(output):
        return output
    else:
        is_devel = re.compile(devel_version_re).match(output)
        if is_devel:
            return "%s+devel" % is_devel.group(1)

    return "0.0.0+git+devel"

DISTRO_VERSION = "${@ossystems_get_distro_version(d)}"
