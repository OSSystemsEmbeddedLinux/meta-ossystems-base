OS_RELEASE_FIELDS_oel_append = " BUILD_NUMBER"

# Append build number for development builds
VERSION_oel_append = "${@' (build %s)' % BUILD_NUMBER if 'BUILD_NUMBER' in d and DISTRO_VERSION.endswith('+devel') else ''}"
