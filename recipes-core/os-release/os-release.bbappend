OS_RELEASE_FIELDS_append_oel = " BUILD_NUMBER"

# Append build number for development builds
VERSION_append_oel = "${@' (build %s)' % BUILD_NUMBER if 'BUILD_NUMBER' in d and DISTRO_VERSION.endswith('+devel') else ''}"
