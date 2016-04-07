# -*- python -*-
# release-bundle.bbclass
#
# Copyright (C) 2016 O.S. Systems Software Ltda.  All Rights Reserved
#
# This class include additional tasks for the recipes, which allow for
# the release bundle generation.
#
# The main objective of this is to allow the mirror tarball generation
# and collection for individual recipes.
#
# Released under the MIT license (see packages/COPYING)

RELEASE_BUNDLE_TMP_DOWNLOAD_CACHE = "${TMPDIR}/release-bundle/download-cache"

addtask generate_mirror_tarball before do_collect_recipe_source
do_generate_mirror_tarball[doc] = "Generates the mirror tarball for a target"
do_generate_mirror_tarball[dirs] = "${DL_DIR}"
do_generate_mirror_tarball[vardeps] += "SRCREV"
do_generate_mirror_tarball[nostamp] = "1"
python do_generate_mirror_tarball() {
    src_uri = (d.getVar('SRC_URI', True) or '').split()
    if len(src_uri) == 0:
        return

    pn = d.getVar('PN', True)
    bb.debug(1, "Generating mirror tarball for '%s' recipe." % pn)

    # FIXME: The cache needs to be cleaned up or recipes which
    # references SRCPV fails to identify the changed value.
    bb.fetch2.urldata_cache = {}
    d.setVar("BB_GENERATE_MIRROR_TARBALLS", "1")

    try:
        fetcher = bb.fetch2.Fetch(src_uri, d)
        fetcher.download()
    except bb.fetch2.BBFetchException as e:
        raise bb.build.FuncFailed(e)
}

RELEASE_BUNDLE_LAYERNAME = "${@bb.utils.get_file_layer('${FILE}', d) or 'UNKNOWN'}"
RELEASE_BUNDLE_LAYERDIR = "${PLATFORM_LAYERDIR_${RECIPE_LAYERNAME}}"

addtask collect_recipe_source after do_generate_mirror_tarball
do_collect_recipe_source[doc] = "Collect the source code of a target"
do_collect_recipe_source[dirs] = "${DL_DIR}"
do_collect_recipe_source[cleandirs] = "${RELEASE_BUNDLE_TMP_DOWNLOAD_CACHE}/${PN}/${RELEASE_BUNDLE_LAYERDIR}/downloads"
do_collect_recipe_source[file-checksums] = "${@bb.fetch.get_checksum_file_list(d)}"
do_collect_recipe_source[file-checksums] += " ${@get_lic_checksum_file_list(d)}"
do_collect_recipe_source[vardeps] += "SRCREV"
do_collect_recipe_source[nostamp] = "1"
python do_collect_recipe_source() {
    src_uri = (d.getVar('SRC_URI', True) or '').split()
    if len(src_uri) == 0:
        return

    pn = d.getVar('PN', True)
    layerdir = d.getVar('RELEASE_BUNDLE_LAYERDIR', True)
    bb.debug(1, "Collecting the source code for '%s' recipe in %s" % (pn, layerdir))

    dl_dir = d.getVar('DL_DIR', True)
    dl_dir_bundle = oe.path.join(d.getVar('RELEASE_BUNDLE_TMP_DOWNLOAD_CACHE', True), pn, layerdir, 'downloads')

    try:
        fetcher = bb.fetch2.Fetch(src_uri, d)
        for u in fetcher.ud.values():
            if u.mirrortarball:
                oe.path.symlink(oe.path.join(dl_dir, u.mirrortarball),
                                oe.path.join(dl_dir_bundle, u.mirrortarball),
                                force=True)
    except bb.fetch2.BBFetchException as e:
        raise bb.build.FuncFailed(e)
}
