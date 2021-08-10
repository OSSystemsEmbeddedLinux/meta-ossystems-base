PACKAGECONFIG[viv-fb] = ",,virtual/libgles2 virtual/libg2d"
OPENGL_WINSYS:append = "${@bb.utils.contains('PACKAGECONFIG', 'viv-fb', ' viv-fb', '', d)}"
