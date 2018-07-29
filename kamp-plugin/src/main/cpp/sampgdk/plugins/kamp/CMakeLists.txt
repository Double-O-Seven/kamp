include(AMXConfig)
include(AddSAMPPlugin)

find_package(JNI)

if (JNI_FOUND)
  message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
  message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()

if(MSVC)
  add_definitions(-D_CRT_SECURE_NO_WARNINGS)
endif()

add_samp_plugin(kamp
  Kamp.cpp
  Kamp.hpp
  SAMPNativeFunctions.cpp
  SAMPNativeFunctions.h
  FieldCache.cpp
  FieldCache.hpp
  SAMPCallbacks.cpp
  SAMPCallbacks.def
  SAMPCallbacksMethodCache.cpp
  SAMPCallbacksMethodCache.hpp
  ${SAMPSDK_DIR}/amxplugin.cpp
)

include_directories(${JNI_INCLUDE_DIRS})

target_link_libraries(kamp sampgdk ${JNI_LIBRARIES})
