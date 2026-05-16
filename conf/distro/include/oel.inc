DISTRO_VERSION = "26.05-snapshot-${DATE}"

SDK_VENDOR = "-oelsdk"
SDK_VERSION := "${@'${DISTRO_VERSION}'.replace('snapshot-${DATE}','snapshot')}"
SDK_NAME_PREFIX = "${DISTRO}"

MAINTAINER = "O.S. Systems Software LTDA. <contato@ossystems.com.br>"

TARGET_VENDOR = "-oel"

SDKMACHINE ?= "x86_64"

# Paths
OELDIR ?= "${COREBASE}/.."

SDK_NAME = "${DISTRO}-${TCLIBC}-${SDKMACHINE}-${IMAGE_BASENAME}-${TUNE_PKGARCH}-${MACHINE}"
SDKPATHINSTALL = "/opt/${DISTRO}/${SDK_VERSION}"

# Move the persist db up out of TMPDIR so it survives its removal
PERSISTENT_DIR = "${TOPDIR}/db"

# Restore any available saved headrevs
DUMP_HEADREVS_DB ?= '${OELDIR}/${MACHINE}/saved_persist_data.db'
INHERIT += "restore-dumped-headrevs"

# Co-locate the hash equivalence database with SSTATE_DIR so multiple builds
# sharing the same sstate cache also share the hashserv DB (per OE-Core's
# sanity.bbclass).
BB_HASHSERVE_DB_DIR ??= "${SSTATE_DIR}"

# LAYERDIRs for our layers, by name
INHERIT += "layerdirs"

# Do an up front type check to sanity check user configuration
INHERIT += "typecheck"

# Also check for host user contamination in the rootfs
ROOTFS_POSTPROCESS_COMMAND:append = " rootfs_check_host_user_contaminated;"

SANITY_TESTED_DISTROS ?= " \
    almalinux-8 \
    almalinux-9 \
    centos-stream-9 \
    centos-stream-10 \
    debian-11 \
    debian-12 \
    debian-13 \
    fedora-42 \
    fedora-43 \
    opensuseleap-15.6 \
    opensuseleap-16.0 \
    rocky-8 \
    rocky-9 \
    ubuntu-22.04 \
    ubuntu-24.04 \
    ubuntu-25.10 \
    ubuntu-26.04 \
"

####
#### Distro Features & Recipe Configuration
####

# Override these in oel based distros
OEL_DEFAULT_DISTRO_FEATURES ??= "ipv4 ipv6"
OEL_EXTRA_DISTRO_FEATURES ??= ""
OEL_DEFAULT_EXTRA_RDEPENDS = "packagegroup-core-boot"

DISTRO_FEATURES ?= "${OEL_DEFAULT_DISTRO_FEATURES} \
                    ${OEL_EXTRA_DISTRO_FEATURES}"

DISTRO_EXTRA_RDEPENDS += "${OEL_DEFAULT_EXTRA_RDEPENDS}"

DISTRO_FEATURES_OPTED_OUT ?= "pulseaudio gobject-introspection-data"

# Exclude DATE from DISTRO/SDK_VERSION checksums
DISTRO_VERSION[vardepsexclude] = "DATE"
SDK_VERSION[vardepsexclude] = "DATE"

# Lower the priority of meta-oe, as we prefer oe-core when recipes are duped
BBFILE_PRIORITY_openembedded-layer = "1"

# Preferred kernel version for QEMU based machines
PREFERRED_VERSION_linux-yocto ?= "6.18%"

####
#### Inherits
####

INHERIT += "buildhistory"

####
#### Preferences and providers
####

# Prefer O.S. Systems U-Boot fork
#PREFERRED_PROVIDER_u-boot              ?= "u-boot-ossystems"
#PREFERRED_PROVIDER_virtual/bootloader  ?= "u-boot-ossystems"

# Since many embedded systems don't have non-root users, allow autospawn for
# root as well.
PACKAGECONFIG:append:pn-pulseaudio = " autospawn-for-root"

# Ensure we have the writable paths we need in a read-only rootfs
VOLATILE_BINDS:append = "\
	/var/volatile/root-home ${ROOT_HOME}\n\
	/var/volatile/media /media\n\
	/var/volatile/resolv.conf /etc/resolv.conf\n\
"

# Override pulseaudio to use a system rather than user systemd service when we
# don't have pam available, as the user service won't be started
systemd_userunitdir:pn-pulseaudio = "${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${systemd_unitdir}/user', '${systemd_unitdir}/system', d)}"

# We prefer busybox rather than tinylogin
VIRTUAL-RUNTIME_login_manager ??= "busybox"

# Use systemd as syslog in distros that has systemd in DISTRO_FEATURES
VIRTUAL-RUNTIME_base-utils-syslog = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'busybox-syslog', d)}"

# Default to these image types
IMAGE_FSTYPES ?= "tar.bz2 ext3"
UBI_VOLNAME ?= "rootfs"

# Quadruple the normal. 'du' is not a good way to really see how much
# space will be needed and fails badly as the fs size grows.
IMAGE_ROOTFS_EXTRA_SPACE = "40960"

# Include nss-myhostname for sysvinit, so the hostname resolves. systemd
# includes myhostname itself.
DISTRO_EXTRA_RRECOMMENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'nss-myhostname', d)}"

####
#### Workarounds & Overrides
####

# Work around missing vardep bug in bitbake
sstate_stage_all[vardeps] += "sstate_stage_dirs"

# Layer name and path for the recipe
RECIPE_LAYERNAME = "${@bb.utils.get_file_layer('${FILE}', d) or 'UNKNOWN'}"
RECIPE_LAYERDIR = "${LAYERDIR_${RECIPE_LAYERNAME}}"
LAYERDIR_UNKNOWN = 'UNKNOWN'

# Support pulling downloads and sstate from inside individual layers. This
# will let us ship self contained layers to a release without risking file
# conflicts between them.
PREMIRRORS:prepend = "${@'.*://.*/.* file://${RECIPE_LAYERDIR}/downloads\n' if '${RECIPE_LAYERDIR}' != 'UNKNOWN' else ''}"
LAYER_SSTATE_MIRRORS = "${@" ".join('file://%s' % sl for sl in ('%s/sstate-cache' % l for l in '${BBLAYERS}'.split()) if os.path.exists(sl))}"
SSTATE_MIRROR_SITES:prepend = "${LAYER_SSTATE_MIRRORS} "

####
#### Includes
####

# Ensure that we implement shared state reuse handling for non-target recipes
require conf/distro/include/sstate.inc

# This function changes the default tune for machines which are based on armv7a
# to use common tune value.
def arm_tune_handler(d):
    features = d.getVar('TUNE_FEATURES', True).split()
    if 'armv7a' in features or 'armv7ve' in features:
        tune = 'armv7ahf'
        if 'bigendian' in features:
            tune += 'b'
        if 'vfpv3' in features:
            tune += '-vfpv3'
        if 'vfpv3d16' in features:
            tune += '-vfpv3d16'
        if 'neon' in features:
            tune += '-neon'
        if 'vfpv4' in features:
            tune += '-vfpv4'
    else:
        tune = d.getVar('DEFAULTTUNE', True)
    return tune

DEFAULTTUNE_oel := "${@arm_tune_handler(d)}"

DISTRO_ARM_INSTRUCTION ?= "arm"
DISTRO_ARM_INSTRUCTION:armv4 ?= "arm"
DISTRO_ARM_INSTRUCTION:armv5te ?= "arm"
DISTRO_ARM_INSTRUCTION:armv6 ?= "arm"
ARM_INSTRUCTION_SET = "${DISTRO_ARM_INSTRUCTION}"

# Default system locale used in /etc/locale.conf file
DEFAULT_SYSTEM_LOCALE ?= "en_US.UTF-8"
