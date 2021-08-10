# -*- python -*-
# ossystems-onsite-only-recipe-handler.bbclass
# Copyright (C) 2015-2020 O.S. Systems Software Ltda. All Rights Reserved
# Released under the MIT license (see packages/COPYING)
#
# This class allows for recipes to be skipped when building outsite of
# company's infrastructure.
#
# The following variables need to be defined:
#
#  OSSYSTEMS_ONSITE_ONLY_RECIPES: Recipes which are on-site specific
#  OSSYSTEMS_USE_ONSITE_ONLY_RECIPES: Enable/disable recipes
#
# So when building externally, the OSSYSTEMS_USE_ONSITE_ONLY_RECIPES
# should be set to '0' and as consequence the recipes listed in
# OSSYSTEMS_ONSITE_ONLY_RECIPES will be disabled.

def ossystems_onsite_only_recipe_handler(d):
    onsite_only_recipes = d.getVar('OSSYSTEMS_ONSITE_ONLY_RECIPES', True)
    if not onsite_only_recipes:
        bb.debug(1, "O.S. Systems OnSite-Only Recipes handler: OSSYSTEMS_ONSITE_ONLY_RECIPES is not set.")
        return

    if d.getVar('OSSYSTEMS_USE_ONSITE_ONLY_RECIPES', True) == "1":
        bb.note("O.S. Systems OnSite-Only Recipes handler: Enabled")
        return

    bb.note("O.S. Systems OnSite-Only Recipes handler: Disabled")

    for recipe in onsite_only_recipes.split():
        d.setVar("SRCPV:pn-%s" % recipe, "OSSystemsOnSiteOnlyRecipeTag")
        bb.debug(1, "O.S. Systems OnSite-Only Recipes handler: Overriding %s" % recipe)

def ossystems_onsite_only_recipe_check(d):
    recipes = d.getVar('OSSYSTEMS_ONSITE_ONLY_RECIPES', True)
    if not recipes:
        return

    pn = d.getVar('PN', True)
    if pn in recipes.split():
        srcpv = d.getVar('SRCPV', True)
        if srcpv == "OSSystemsOnSiteOnlyRecipeTag":
            raise bb.parse.SkipPackage("O.S. Systems OnSite-Only Recipes handler: recipe skipped.")

addhandler ossystems_onsite_only_recipe_eventhandler
python ossystems_onsite_only_recipe_eventhandler() {
    if bb.event.getName(e) == "ConfigParsed":
        ossystems_onsite_only_recipe_handler(e.data)
}

python () {
    ossystems_onsite_only_recipe_check(d)
}
