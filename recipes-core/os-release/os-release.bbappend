OS_RELEASE_FIELDS:append_oel = " BUILD_NUMBER"

# Append build number for development builds
VERSION:append_oel = "${@' (build %s)' % BUILD_NUMBER if 'BUILD_NUMBER' in d and DISTRO_VERSION.endswith('+devel') else ''}"
