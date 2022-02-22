# For compatibility
SSTATE_MIRROR_SITES ?= ""

# Handle sstate checksum issues
python sstate_config_handler () {
    # Ensure the path to the platform install doesn't affect checksums
    d.appendVar('BB_BASEHASH_IGNORE_VARS', ' PLATFORM_ROOT_DIR')

    # These vars shouldn't change except for when BUILD_ARCH does, and that's
    # already captured via the sstate archive filename. Excluding them should
    # let us reuse target sstates even if the BUILD_ARCH of the natives
    # changes (e.g. reuse target between 32 and 64 bit build hosts).
    d.appendVar("BB_BASEHASH_IGNORE_VARS", " SITEINFO_ENDIANNESS SITEINFO_BITS SIZEOF_POINTER")
}
sstate_config_handler[eventmask] = "bb.event.ConfigParsed"
addhandler sstate_config_handler
