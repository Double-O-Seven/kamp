
#ifndef KAMP_H
#define KAMP_H

#include <jni.h>
#include <sampgdk.h>

const char KAMP_LAUNCHER_LAUNCH_METHOD_NAME[] = "launch";
const char KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME[] = "getCallbacksInstance";

class Kamp
{
public:

  Kamp();

  ~Kamp();

private:
  JavaVM *javaVM;
  JNIENV *jniEnv;
  jclass kampLauncherClass = nullptr;
  jobject kampLauncherInstance = nullptr;

}

#endif
