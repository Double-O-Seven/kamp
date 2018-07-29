
#ifndef KAMP_HPP
#define KAMP_HPP

#include <string>
#include <vector>
#include <fstream>
#include <iostream>

#include <jni.h>

#include <sampgdk/a_actor.h>
#include <sampgdk/a_objects.h>
#include <sampgdk/a_players.h>
#include <sampgdk/a_samp.h>
#include <sampgdk/a_vehicles.h>
#include <sampgdk/core.h>
#include <sampgdk/sdk.h>

#include "FieldCache.hpp"
#include "SAMPCallbacksMethodCache.hpp"

const std::string KAMP_LAUNCHER_CLASS = "ch/leadrian/samp/kamp/runtime/KampLauncher";
const std::string KAMP_LAUNCHER_LAUNCH_METHOD_NAME = "launch";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME = "getCallbacksInstance";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_SIGNATURE = "()Lch/leadrian/samp/kamp/runtime/SAMPCallbacks;";
const std::string KAMP_CLASS_PATH = "./Kamp/launch/jars";
const std::string KAMP_JVM_OPTIONS_FILE = "./Kamp/launch/jvmopts.txt";


class Kamp
{
public:
	static Kamp& GetInstance() {
		static Kamp instance;
		return instance;
	}

	~Kamp();

	void Launch();

	void Shutdown();

	FieldCache& GetFieldCache() {
		return this->fieldCache;
	}

	jobject GetSAMPCallbacksInstance() {
		return this->sampCallbacksInstance;
	}

	SAMPCallbacksMethodCache& GetSAMPCallbacksMethodCache() {
		return this->sampCallbacksMethodCache;
	}

	JNIEnv *GetJNIEnv() {
		return this->jniEnv;
	}

private:

	Kamp() {}
	Kamp(Kamp const&); // Don't implement for singleton
	void operator=(Kamp const&); // Don't implement for singleton

	long CreateJVM();

	void DestroyJVM();

	bool launched = false;
	JavaVM *javaVM;
	JNIEnv *jniEnv;

	jclass kampLauncherClass = nullptr;
	jobject kampLauncherClassReference = nullptr;

	jobject sampCallbacksInstance = nullptr;
	jobject sampCallbacksInstanceReference = nullptr;

	FieldCache fieldCache;
	SAMPCallbacksMethodCache sampCallbacksMethodCache;

};

#endif
