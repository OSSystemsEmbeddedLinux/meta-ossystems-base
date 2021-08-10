# -*- python -*-
# ossystems-srcrev-handler.bbclass
# Copyright (C) 2013, 2014 O.S. Systems Software Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
#
# This class helps the management of several project components of a product.
# The class uses a file to describe the recipe and the version to use for
# stable releases but also easy the use of top of tree builds of those
# components for development purposes.
#
# The file should have the following format:
#
# <recipe> <version> <hash>
#
# To use the top of tree for building, OSSYSTEMS_SRCREV_AUTOREV must be
# set to "1". Otherwise it uses the stable revisions.
#
# O.S. Systems has a set of tools to facilitate the release management
# of products based on Yocto Project and this class is part of the system.

def _stable_release(d):
    return (d.getVar('OSSYSTEMS_SRCREV_AUTOREV', True) != "1")

def ossystems_srcrev_handler(d):
    srcrevs = d.getVar('OSSYSTEMS_SRCREVS_FILE', True)
    if not srcrevs:
        bb.debug(1, "O.S. Systems SRCREV handler: OSSYSTEMS_SRCREVS_FILE is not set.")
        return

    if not os.path.exists(srcrevs):
        bb.error("O.S. Systems SRCREV handler: '%s' doesn't exists." % srcrevs)
        return

    if _stable_release(d):
        bb.plain("O.S. Systems SRCREV handler: Using stable versions to build recipes...")
    else:
        bb.plain("O.S. Systems SRCREV handler: Using 'AUTOREV' to build recipes...")

    with open(srcrevs, 'r') as srcrevs_fd:
        for line in srcrevs_fd.readlines():
            pkg, version, srcrev = line.split()

            if _stable_release(d):
                bb.debug(1, "O.S. Systems SRCREV handler: Setting %s for version %s (%s)..."
                         % (pkg, version, srcrev))
                rev = srcrev
            else:
                bb.debug(1, "O.S. Systems SRCREV handler: Setting %s for 'AUTOREV'..."
                         % (pkg))
                rev = "${AUTOREV}"

            d.setVar("SRCREV:pn-%s" % pkg, rev)

addhandler ossystems_srcrev_eventhandler
python ossystems_srcrev_eventhandler() {
    if bb.event.getName(e) == "ConfigParsed":
        ossystems_srcrev_handler(e.data)
}
