include(AMXConfig)
include(AddSAMPPlugin)

if(MSVC)
  add_definitions(-D_CRT_SECURE_NO_WARNINGS)
endif()

add_samp_plugin(kamp
  ${SAMPSDK_DIR}/amxplugin.cpp
)

target_link_libraries(kamp sampgdk)
