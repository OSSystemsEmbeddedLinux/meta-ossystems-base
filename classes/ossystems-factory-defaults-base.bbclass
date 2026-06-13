# nooelint: oelint.bbclass.underscores oelint.file.inlinesuppress_na  no EXPORT_FUNCTIONS here, so the dash is harmless
OSSYSTEMS_FACTORY_DEFAULTS_DIR ?= "${datadir}/factory-defaults"
OSSYSTEMS_FACTORY_DEFAULTS_FILES ?= ""
OSSYSTEMS_FACTORY_DEFAULTS_HOOKS ?= ""
OSSYSTEMS_FACTORY_DEFAULTS_HOOKS_DIR ?= "${sysconfdir}/factory-defaults.d"
OSSYSTEMS_FACTORY_DEFAULTS_POSTINST_FILES ?= ""
OSSYSTEMS_FACTORY_DEFAULTS_RUNTIME_DIR ?= "/data"
