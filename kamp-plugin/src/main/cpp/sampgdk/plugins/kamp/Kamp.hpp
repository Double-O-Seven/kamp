
#ifndef KAMP_HPP
#define KAMP_HPP

#include <string>
#include <vector>
#include <fstream>
#include <iostream>

#include <jni.h>

#include <sampgdk/a_actor.h>
#include <sampgdk/a_http.h>
#include <sampgdk/a_objects.h>
#include <sampgdk/a_players.h>
#include <sampgdk/a_samp.h>
#include <sampgdk/a_vehicles.h>
#include <sampgdk/core.h>
#include <sampgdk/sdk.h>

const std::string KAMP_LAUNCHER_CLASS = "ch/leadrian/samp/kamp/runtime/KampLauncher";
const std::string KAMP_LAUNCHER_LAUNCH_METHOD_NAME = "launch";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME = "getCallbacksInstance";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_SIGNATURE = "()Lch/leadrian/samp/kamp/runtime/SAMPCallbacks;";
const std::string KAMP_CLASS_PATH = "./Kamp/launch/jars";
const std::string KAMP_JVM_OPTIONS_FILE = "./Kamp/launch/jvmopts.txt";


class Kamp
{
public:

	Kamp();

	~Kamp();

	void Launch();

	void Shutdown();

	jobject GetSAMPCallbacksInstance();

private:

	long CreateJVM();

	void DestroyJVM();

	bool launched = false;
	JavaVM *javaVM;
	JNIEnv *jniEnv;
	jclass kampLauncherClass = nullptr;
	jobject sampCallbacksInstance = nullptr;

};

#endif
