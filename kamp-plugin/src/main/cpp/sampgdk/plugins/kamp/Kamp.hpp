
#ifndef KAMP_HPP
#define KAMP_HPP

#include <jni.h>
#include <sampgdk/a_actor.h>
#include <sampgdk/a_http.h>
#include <sampgdk/a_objects.h>
#include <sampgdk/a_players.h>
#include <sampgdk/a_samp.h>
#include <sampgdk/a_vehicles.h>
#include <sampgdk/core.h>
#include <sampgdk/sdk.h>

const char KAMP_LAUNCHER_LAUNCH_METHOD_NAME[] = "launch";
const char KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME[] = "getCallbacksInstance";

class Kamp
{
public:

	Kamp();

	~Kamp();

	void launch();

	void shutdown();

private:
	bool launched = false;
	JavaVM *javaVM;
	JNIEnv *jniEnv;
	jclass kampLauncherClass = nullptr;
	jobject kampLauncherInstance = nullptr;

};

#endif
