python save_layerdirs() {
    platform_dir = d.getVar('PLATFORM_ROOT_DIR', True)

    for layerpath in d.getVar('BBLAYERS', True).split():
        layerconf = os.path.join(layerpath, 'conf', 'layer.conf')
        relative_layerpath = oe.path.relative(platform_dir, layerpath)

        l = bb.data.init()
        l.setVar('LAYERDIR', layerpath)
        l.setVar('PLATFORM_LAYERDIR', relative_layerpath)
        l = bb.parse.handle(layerconf, l)
        l.expandVarref('LAYERDIR')
        l.expandVarref('PLATFORM_LAYERDIR')

        for layername in l.getVar('BBFILE_COLLECTIONS', True).split():
            d.setVar('LAYERDIR_%s' % layername, layerpath)
            d.setVar('PLATFORM_LAYERDIR_%s' % layername, relative_layerpath)
}
save_layerdirs[eventmask] = "bb.event.ConfigParsed"
addhandler save_layerdirs
