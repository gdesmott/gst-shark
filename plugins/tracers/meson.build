libgstshark_sources = [
  'gstdot.c',
  'gstcpuusagecompute.c',
  'gstproctimecompute.c',
  'gstctf.c',
  'gstparser.c',
]

libgstsharktracers_sources = [
  'gstsharktracers.c',
  'gstgraphic.c',
  'gstcpuusage.c',
  'gstproctime.c',
  'gstinterlatency.c',
  'gstscheduletime.c',
  'gstframerate.c',
  'gstqueuelevel.c',
  'gstbitrate.c',
  'gstbuffer.c']


libgstshark = library('gstshark',
  libgstshark_sources,
  c_args : gst_shark_args,
  include_directories : [configinc],
  dependencies : [gio_dep, gst_dep],
  install : true,
)

libgstsharktracers = library('gstsharktracers',
  libgstsharktracers_sources,
  c_args : gst_shark_args + ['-DGST_USE_UNSTABLE_API'],
  include_directories : [configinc],
  dependencies : [gio_dep, gst_dep],
  link_with : [libgstshark],
  install : true,
  install_dir : plugins_install_dir,
)
