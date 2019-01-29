
#ifndef KAMP_HPP
#define KAMP_HPP

#include <string>
#include <jni.h>
#include <exception>

#include "FieldCache.hpp"
#include "SAMPCallbacksMethodCache.hpp"

const std::string KAMP_LAUNCHER_CLASS = "ch/leadrian/samp/kamp/core/runtime/KampLauncher";
const std::string KAMP_LAUNCHER_LAUNCH_METHOD_NAME = "launch";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME = "getCallbacksInstance";
const std::string KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_SIGNATURE = "()Lch/leadrian/samp/kamp/core/runtime/SAMPCallbacks;";
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

	bool IsLaunched() {
		return launched;
	}

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

	long CreateJVM() ;

	void DestroyJVM();

	void InitializeJVM();

	void InitializeKampLauncherClass();

	void InitializeFieldCache();

	void CallLaunchMethod();

	void InitializeSAMPCallbacksInstance();

	void InitializeSAMPCallbacksMethodCache();

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

class KampException : public std::exception {

public:
	KampException(const char* message) {
		this->message = message;
	}

	virtual const char* what() const throw() {
		return message;
	}

private:
	const char* message;

};

#endif
