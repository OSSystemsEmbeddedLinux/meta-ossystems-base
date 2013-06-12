def __after_init_ossystems_base():
    PLATFORM_ROOT_DIR = os.environ['PLATFORM_ROOT_DIR']
    append_layers([ os.path.join(PLATFORM_ROOT_DIR, 'sources', p) for p in
                    ['meta-ossystems-base']
                    ])

run_after_init(__after_init_ossystems_base)
