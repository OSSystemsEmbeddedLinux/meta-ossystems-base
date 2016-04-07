#!/bin/sh

verlte () {
    [ "$1" = "`printf "$1\n$2" | sort -V | head -n1`" ]
}

verlt() {
    [ "$1" = "$2" ] && return 1 || verlte $1 $2
}

verlt `uname -r` @OLDEST_KERNEL@
if [ $? = 0 ]; then
    echo "Error: The bundle needs a kernel > @OLDEST_KERNEL@"
    exit 1
fi

DEFAULT_INSTALL_DIR="@RELEASE_BUNDLE_PATH@"
SUDO_EXEC=""
export target_dir=""
answer=""
relocate=1
savescripts=0
verbose=0
while getopts ":yd:nDRS" OPT; do
    case $OPT in
	y)
	    answer="Y"
	    ;;
	d)
	    target_dir=$OPTARG
	    ;;
	n)
	    prepare_buildsystem="no"
	    ;;
	D)
	    verbose=1
	    ;;
	R)
	    relocate=0
	    savescripts=1
	    ;;
	S)
	    savescripts=1
	    ;;
	*)
	    echo "Usage: $(basename $0) [-y] [-d <dir>]"
	    echo "  -y         Automatic yes to all prompts"
	    echo "  -d <dir>   Install the bundle to <dir>"
	    echo "======== Extensible bundle only options ========="
	    echo "  -n         Do not prepare the build system"
	    echo "======== Advanced DEBUGGING ONLY OPTIONS ========"
	    echo "  -S         Save relocation scripts"
	    echo "  -R         Do not relocate executables"
	    echo "  -D         use set -x to see what is going on"
	    exit 1
	    ;;
    esac
done

titlestr="@RELEASE_BUNDLE_TITLE@ installer version @RELEASE_BUNDLE_VERSION@"
printf "%s\n" "$titlestr"
printf "%${#titlestr}s\n" | tr " " "="

if [ $verbose = 1 ] ; then
    set -x
fi

if [ "$target_dir" = "" ]; then
    if [ "$answer" = "Y" ]; then
	target_dir="$DEFAULT_INSTALL_DIR"
    else
	read -p "Enter target directory for the bundle (default: $DEFAULT_INSTALL_DIR): " target_dir
	[ "$target_dir" = "" ] && target_dir=$DEFAULT_INSTALL_DIR
    fi
fi

eval target_dir=$(echo "$target_dir"|sed 's/ /\\ /g')
if [ -d "$target_dir" ]; then
    target_dir=$(cd "$target_dir"; pwd)
else
    target_dir=$(readlink -m "$target_dir")
fi

if [ -n "$(echo $target_dir|grep ' ')" ]; then
	echo "The target directory path ($target_dir) contains spaces. Abort!"
	exit 1
fi

printf "You are about to install the bundle to \"$target_dir\". Proceed? [Y/n] "
default_answer="y"
if [ "$answer" = "" ]; then
    read answer
    [ "$answer" = "" ] && answer="$default_answer"
else
    echo $answer
fi

if [ "$answer" != "Y" -a "$answer" != "y" ]; then
    echo "Installation aborted!"
    exit 1
fi

# Try to create the directory (this will not succeed if user doesn't have rights)
mkdir -p $target_dir >/dev/null 2>&1

# if don't have the right to access dir, gain by sudo
if [ ! -x $target_dir -o ! -w $target_dir -o ! -r $target_dir ]; then
    SUDO_EXEC=$(which "sudo")
    if [ -z $SUDO_EXEC ]; then
	echo "No command 'sudo' found, please install sudo first. Abort!"
	exit 1
    fi

    # test sudo could gain root right
    $SUDO_EXEC pwd >/dev/null 2>&1
    [ $? -ne 0 ] && echo "Sorry, you are not allowed to execute as root." && exit 1

    # now that we have sudo rights, create the directory
    $SUDO_EXEC mkdir -p $target_dir >/dev/null 2>&1
fi

payload_offset=$(($(grep -na -m1 "^MARKER:$" $0|cut -d':' -f1) + 1))

printf "Extracting bundle..."
tail -n +$payload_offset $0| $SUDO_EXEC tar xj -C $target_dir --checkpoint=.2500
echo "done"

repo_executable_path=`which repo`
if [ -z "$repo_executable_path" ]; then
	echo ""
	cat <<EOF
============================== REPO NOT DETECTED! =============================

Repo is a tool that makes it easier to work with Git in the context of
the Yocto Project. You will need it to update the repositories of the
sources of this bundle.

To install Repo:

Make sure you have a bin/ directory in your home directory and that it
is included in your path:

$ mkdir ~/bin
$ PATH=~/bin:$PATH

Download the Repo tool and ensure that it is executable:

$ curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
$ chmod a+x ~/bin/repo

After installing Repo, set up your client to access the source repositories:

Configure git with your real name and email address:

$ git config --global user.name "Your Name"
$ git config --global user.email "you@example.com"
===============================================================================
EOF
	echo ""
fi

echo "The bundle has been successfully set up and is ready to be used."
echo "Each time you wish to use the bundle in a new shell session, you need to source the environment setup script e.g."
echo " \$ . setup-environment <build-directory>"

exit 0

MARKER:
