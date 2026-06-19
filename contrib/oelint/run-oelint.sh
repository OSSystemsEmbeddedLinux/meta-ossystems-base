#!/bin/sh
# Run oelint-adv over meta-ossystems-base. Requires oelint-adv on PATH.
#
# All linter configuration is declarative and lives at the layer root, so this
# script only has to enumerate the files (oelint-adv does not recurse into
# directories):
#   * .oelint.cfg           - sets '--release' and the single layer-wide
#                             suppression (oelint.var.bbclassextend); auto-loaded
#                             from the working directory
#   * oelint.constants.json - the layer constant-DB additions, auto-loaded by
#                             oelint-adv from the layer root (no --constantmods)
#
# Aside from oelint.var.bbclassextend (which never applies to a target-only
# layer), exceptions stay inline as '# nooelint: <rule.id>' comments next to the
# finding, so new recipes are always fully linted and each exception is
# documented in place (and flagged by oelint.file.inlinesuppress_na once stale).
set -eu

# Neutralise CDPATH so 'cd' below can't print or jump to an unexpected dir.
unset CDPATH

here=$(cd -- "$(dirname -- "$0")" && pwd)
layer=$(cd -- "$here/../.." && pwd)

# Run from the layer root so '.oelint.cfg' (probed in the working directory) and
# 'oelint.constants.json' (probed in the file's layer root) are both picked up.
cd -- "$layer"

files=$(find . \
    \( -name '*.bb' -o -name '*.bbappend' -o -name '*.bbclass' -o -name '*.inc' \) \
    | sort)

# Run serially: oelint-adv's parallel workers race while loading the layer
# constants, intermittently emitting false "unknown variable/override" findings.
# Serial execution is deterministic. Pass '--jobs N' to override.
# shellcheck disable=SC2086
exec oelint-adv --jobs 1 "$@" $files
