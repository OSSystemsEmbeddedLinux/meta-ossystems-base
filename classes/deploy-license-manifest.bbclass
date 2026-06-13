# nooelint: oelint.bbclass.underscores oelint.file.inlinesuppress_na  no EXPORT_FUNCTIONS here, so the dash is harmless
ROOTFS_POSTPROCESS_COMMAND += "deploy_license_manifest;"
IMAGE_POSTPROCESS_COMMAND += "link_license_manifest;"

deploy_license_manifest[doc] = "Copy the image license manifest into the deploy directory and generate a CSV variant."
deploy_license_manifest () {
    if [ -e "${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest" ]; then
        cp ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest ${IMGDEPLOYDIR}/${IMAGE_NAME}.license_manifest
        sed -n '/PACKAGE NAME/{: start; /^ *$/b done; /LICENSE:/{s/: /: "/; s/$/"/;}; s/^.*://; H; n; b start; : done; x; s/^[\n ]*//; s/ *\n */,/g; p}' ${IMGDEPLOYDIR}/${IMAGE_NAME}.license_manifest >${IMGDEPLOYDIR}/${IMAGE_NAME}.license_manifest.csv
    fi
}

link_license_manifest[doc] = "Create stable-named symlinks to the deployed license manifest and its CSV variant."
link_license_manifest () {
    if [ -e "${IMGDEPLOYDIR}/${IMAGE_NAME}.license_manifest" ]; then
        ln -sf ${IMAGE_NAME}.license_manifest ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.license_manifest
        ln -sf ${IMAGE_NAME}.license_manifest.csv ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.license_manifest.csv
    fi
}
