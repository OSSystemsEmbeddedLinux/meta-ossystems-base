# -*- python -*-
# ossystems-distro-version.bbclass provides a DISTRO_VERSION variable
# that can be used to identify the release of a product.
#
# The OSSYSTEMS_DISTRO_VERSION_DEVEL_SUFFIX variable are used to
# configure the class. It allows to override the suffix used for
# development versions. By default the '+devel' value is used however
# depending on the product needs other variables can be used as well
# (e.g: '-snapthost-${DATE}').
#
# Using OSSYSTEMS_DISTRO_VERSION_DEVEL_VERSION_RE and
# OSSYSTEMS_DISTRO_VERSION_STABLE_VERSION_RE, we can customize how we match the
# tags to identify the version for special use cases which does not follow the
# semver one.
#
# Copyright (C) 2009-2020, O.S. Systems Softwares Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

OSSYSTEMS_DISTRO_VERSION_DEVEL_VERSION_RE ?= "^((\d+\.)+\d+)-\d+-g\w+$"
OSSYSTEMS_DISTRO_VERSION_STABLE_VERSION_RE ?= "^(\d+\.)+\d+$"
OSSYSTEMS_DISTRO_VERSION_DEVEL_SUFFIX ?= "+devel"

def ossystems_get_distro_version(d):
    import re, os, os.path

    devel_version = d.getVar('OSSYSTEMS_DISTRO_VERSION_DEVEL_SUFFIX', False)
    devel_version_re = d.getVar('OSSYSTEMS_DISTRO_VERSION_DEVEL_VERSION_RE', False)
    stable_version_re = d.getVar('OSSYSTEMS_DISTRO_VERSION_STABLE_VERSION_RE', False)

    bspdir = d.getVar('PLATFORM_ROOT_DIR', True)
    layerdir = os.path.join(bspdir, '.repo', 'manifests')

    cmd = 'cd %s ; git describe --always 2> /dev/null' % (layerdir)
    with os.popen(cmd) as cmd:
        output = cmd.read().strip()

        if re.compile(stable_version_re).match(output):
            return output
        else:
            is_devel = re.compile(devel_version_re).match(output)
            if is_devel:
                return "%s%s" % (is_devel.group(1), devel_version)

    return "0.0.0-git%s" % devel_version

DISTRO_VERSION := "${@ossystems_get_distro_version(d)}"
