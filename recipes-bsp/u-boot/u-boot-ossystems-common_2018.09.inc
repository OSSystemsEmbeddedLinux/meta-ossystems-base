# Copyright (C) 2012-2018 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

SRC_URI = "git://code.ossystems.com.br/bsp/u-boot;protocol=http;branch=${SRCBRANCH}"

SRCREV = "fc3b1aa53b509ff76d6ad8df34a1784fbeafef15"
SRCBRANCH = "2018.09+ossystems"

PV = "v2018.09+git${SRCPV}"

S = "${WORKDIR}/git"
