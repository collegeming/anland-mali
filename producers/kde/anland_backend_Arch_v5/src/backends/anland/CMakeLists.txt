target_sources(kwin PRIVATE
    anland_backend.cpp
    anland_egl_backend.cpp
    anland_input.cpp
    anland_logging.cpp
    anland_output.cpp
    # KWin-independent PipeWire engines, shared via the top-level
    # libdisplay_producer/ (reusable by other producers; no KWin API deps)
    libdisplay_producer/anland_audio.c
    libdisplay_producer/anland_camera.c
    # symlink to the top-level libdisplay_producer/ (talks to the Android display daemon)
    libdisplay_producer/display_producer.c
    libdisplay_producer/socket_utils.c
)

target_include_directories(kwin PRIVATE ${CMAKE_CURRENT_SOURCE_DIR} ${CMAKE_CURRENT_SOURCE_DIR}/libdisplay_producer)
# PkgConfig::PipeWire is already resolved at the top level for the screencast plugin.
target_link_libraries(kwin PRIVATE Libdrm::Libdrm PkgConfig::PipeWire)
