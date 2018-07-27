
#ifndef KAMP_HPP
#define KAMP_HPP

#include <string>
#include <vector>
#include <fstream>

#include <jni.h>

#include <sampgdk/a_actor.h>
#include <sampgdk/a_http.h>
#include <sampgdk/a_objects.h>
#include <sampgdk/a_players.h>
#include <sampgdk/a_samp.h>
#include <sampgdk/a_vehicles.h>
#include <sampgdk/core.h>
#include <sampgdk/sdk.h>

const std::string KAMP_LAUNCHER_LAUNCH_METHOD_NAME = "launch";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME = "getCallbacksInstance";
const std::string KAMP_CLASS_PATH = "./Kamp/launch/jars";
const std::string KAMP_JVM_OPTIONS_FILE = "./Kamp/launch/jvmopts.txt";

class Kamp
{
public:

	Kamp();

	~Kamp();

	long Launch();

	void Shutdown();

private:

	long CreateJVM();

	bool launched = false;
	JavaVM *javaVM;
	JNIEnv *jniEnv;
	jclass kampLauncherClass = nullptr;
	jobject kampLauncherInstance = nullptr;

};

#endif
