# nooelint: oelint.bbclass.underscores oelint.file.inlinesuppress_na  no EXPORT_FUNCTIONS here, so the dash is harmless
# -*- python -*-
# release-bundle-generation.bbclass
#
# Copyright (C) 2016 O.S. Systems Software Ltda.  All Rights Reserved
#
# This class include additional tasks for the release bundle
# generation.
#
# The main objective of this is allow reuse of this in recipes.
#
# Following variables are used, to configure the release bundle:
#
# - RELEASE_BUNDLE_NAME: Used for the release bundle filename
# - RELEASE_BUNDLE_TITLE: Title shown in the release bundle
# - RELEASE_BUNDLE_VERSION: Version shown in the release bundle
#
# Released under the MIT license (see packages/COPYING)

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
do_packagedata[noexec] = "1"
do_package[noexec] = "1"
do_package_qa[noexec] = "1"

SRC_URI = "file://bundle-shar-extract.sh"

PACKAGES = ""

INHIBIT_DEFAULT_DEPS = "1"

RELEASE_BUNDLE_NAME ?= "${DISTRO}"
RELEASE_BUNDLE_TITLE ?= "${@d.getVar('DISTRO_NAME', True) or d.getVar('DISTRO', True)}"
RELEASE_BUNDLE_VERSION ?= "${DISTRO_VERSION}"

RELEASE_BUNDLE_DEPLOY = "${DEPLOY_DIR}/release-bundle"
RELEASE_BUNDLE_WORKDIR = "${TMPDIR}/release-bundle/workdir"
RELEASE_BUNDLE_TMP_DOWNLOAD_CACHE ?= "${TMPDIR}/release-bundle/download-cache"
RELEASE_BUNDLE_OUTPUTNAME ?= "${RELEASE_BUNDLE_NAME}-release-${RELEASE_BUNDLE_VERSION}"
RELEASE_BUNDLE_RECIPES_WITH_SOURCE ?= ""

do_build[depends] = ""
do_build[recrdeptask] = ""
do_release_bundle_finalize[depends] += "${@' '.join('%s:do_collect_recipe_source' % recipe for recipe in d.getVar('RELEASE_BUNDLE_RECIPES_WITH_SOURCE').split())}"

addtask collect_platform_source before do_release_bundle_finalize
do_collect_platform_source[doc] = "Garbage-collect and copy the platform source tree (.repo, sources, setup-environment) into the release bundle workdir."
do_collect_platform_source[cleandirs] = "${RELEASE_BUNDLE_WORKDIR}"
do_collect_platform_source[depends] += "repo-native:do_populate_sysroot "
do_collect_platform_source[doc] = "Collect platform repositories into the release bundle work directory."
do_collect_platform_source[nostamp] = "1"
do_collect_platform_source() {
    cd "${PLATFORM_ROOT_DIR}"

    cpu_count=`cat /proc/cpuinfo | grep processor | wc -l`

    bbdebug 1 "Compressing source repositories. Using $cpu_count jobs..."
    repo forall --abort-on-errors --jobs=$cpu_count -c git gc --aggressive --quiet

    bbdebug 1 "Copy all source repositories and pre-download files..."
    cp --archive \
       \
       ${PLATFORM_ROOT_DIR}/.repo \
       ${PLATFORM_ROOT_DIR}/sources \
       ${PLATFORM_ROOT_DIR}/setup-environment \
       \
       ${RELEASE_BUNDLE_WORKDIR}
}

RELEASE_BUNDLE_TAR_OPTS = "--owner=root --group=root"
RELEASE_BUNDLE_OLDEST_KERNEL = "3.2.0"
RELEASE_BUNDLE_PATH = "\$HOME/src/${RELEASE_BUNDLE_NAME}/${RELEASE_BUNDLE_VERSION}"

tar_release_bundle[doc] = "Create the compressed release bundle archive."
fakeroot tar_release_bundle() {
    mkdir -p ${RELEASE_BUNDLE_DEPLOY}
    cd ${RELEASE_BUNDLE_WORKDIR}
    tar ${RELEASE_BUNDLE_TAR_OPTS} -cf - . | pbzip2 > ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.tar.bz2
}

addtask release_bundle_finalize after do_unpack do_collect_platform_source before do_build
do_release_bundle_finalize[doc] = "Assemble the final self-extracting release bundle from the collected sources and download cache."
do_release_bundle_finalize[dirs] = "${RELEASE_BUNDLE_WORKDIR}/download"
do_release_bundle_finalize[depends] += "pbzip2-native:do_populate_sysroot "
do_release_bundle_finalize[doc] = "Finalize and emit the self-extracting release bundle."
do_release_bundle_finalize() {
    for bundle_dir in ${RELEASE_BUNDLE_TMP_DOWNLOAD_CACHE}/*/*; do
        [ -e "$bundle_dir" ] || continue
        cp --archive -L "$bundle_dir" ${RELEASE_BUNDLE_WORKDIR}/
    done

    find ${RELEASE_BUNDLE_WORKDIR} -type d -empty -delete

    tar_release_bundle

    cp "${UNPACKDIR}/bundle-shar-extract.sh" ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.sh

    # substitute variables
    sed -e "s#@RELEASE_BUNDLE_PATH@#${RELEASE_BUNDLE_PATH}#g" \
        -e "s#@OLDEST_KERNEL@#${RELEASE_BUNDLE_OLDEST_KERNEL}#g" \
        -e "s#@RELEASE_BUNDLE_TITLE@#${RELEASE_BUNDLE_TITLE}#g" \
        -e "s#@RELEASE_BUNDLE_VERSION@#${RELEASE_BUNDLE_VERSION}#g" \
        \
        -i ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.sh

    # add execution permission
    chmod +x ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.sh

    # append the SDK tarball
    cat ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.tar.bz2 >> ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.sh

    # delete the old tarball, we don't need it anymore
    rm ${RELEASE_BUNDLE_DEPLOY}/${RELEASE_BUNDLE_OUTPUTNAME}.tar.bz2
}
