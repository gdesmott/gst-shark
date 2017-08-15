project('gst-shark', 'c',
  version : '0.4.5.1',
  meson_version : '>= 0.36.0',
  default_options : [ 'warning_level=1',
                      'buildtype=debugoptimized' ])

glib_req = '>= 2.40.0'
glib_dep = dependency('glib-2.0', version : glib_req)
gio_dep = dependency('gio-2.0', version : glib_req)

gst_req = '>= 1.8.1'
gst_dep = dependency('gstreamer-1.0', version : gst_req,
  fallback : ['gstreamer', 'gst_dep'])
gstbase_dep = dependency('gstreamer-base-1.0', version : gst_req,
  fallback : ['gstreamer', 'gst_base_dep'])

plugins_install_dir = '@0@/gstreamer-1.0'.format(get_option('libdir'))

cdata = configuration_data()
cdata.set('VERSION', '"@0@"'.format(meson.project_version()))
cdata.set('GST_STABLE_RELEASE', 1)
cdata.set('PACKAGE', '"gst-shark"')
cdata.set('PACKAGE_NAME', '"GstShark"')
cdata.set('PACKAGE_URL', '"https://github.com/RidgeRun/gst-shark"')
cdata.set('GST_SHARK_LICENSE', '"LGPL"')

configure_file(output : 'config.h', configuration : cdata)

gst_shark_args = ['-DHAVE_CONFIG_H']
configinc = include_directories('.')

subdir('plugins')
