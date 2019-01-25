
#include "Kamp.hpp"

#include <sampgdk/core.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <exception>

Kamp::~Kamp() {
	this->Shutdown();
}

void Kamp::Launch() {
	if (this->launched) {
		return;
	}

	try {
		this->InitializeJVM();
		this->InitializeKampLauncherClass();
		this->InitializeFieldCache();
		this->CallLaunchMethod();
		this->InitializeSAMPCallbacksInstance();
		this->InitializeSAMPCallbacksMethodCache();
	} catch (std::exception& e) {
		sampgdk::logprintf("Failed to initialize Kamp: %s", e.what());
		sampgdk::logprintf("Destroying JVM...");
		this->DestroyJVM();
		sampgdk::logprintf("Exiting...");
		exit(1);
	}

	this->launched = true;
}

void Kamp::InitializeJVM() {
	long result = this->CreateJVM();
	if (result) {
		throw std::exception(("Failed to create JVM: " + std::to_string(result)).c_str());
	}
}

void Kamp::InitializeKampLauncherClass() {
	this->kampLauncherClass = this->jniEnv->FindClass(KAMP_LAUNCHER_CLASS.c_str());
	if (!this->kampLauncherClass) {
		throw std::exception(("Could not find launcher class " + KAMP_LAUNCHER_CLASS).c_str());
	}

	this->kampLauncherClassReference = this->jniEnv->NewGlobalRef(this->kampLauncherClass);
	if (!this->kampLauncherClassReference) {
		throw std::exception("Could not create global reference for Kamp launcher class");
	}
}

void Kamp::InitializeFieldCache() {
	int result = this->fieldCache.Initialize(this->jniEnv);
	if (result) {
		throw std::exception(("Initializing field cache failed with result: " + std::to_string(result)).c_str());
	}
}

void Kamp::CallLaunchMethod() {
	jmethodID launchMethodID = this->jniEnv->GetStaticMethodID(this->kampLauncherClass, KAMP_LAUNCHER_LAUNCH_METHOD_NAME.c_str(), "()V");
	if (!launchMethodID) {
		throw std::exception(("Could not find method " + KAMP_LAUNCHER_LAUNCH_METHOD_NAME + " in class " + KAMP_LAUNCHER_CLASS).c_str());
	}
	this->jniEnv->CallStaticVoidMethod(this->kampLauncherClass, launchMethodID);
	if (this->jniEnv->ExceptionOccurred()) {
		this->jniEnv->ExceptionDescribe();
		this->jniEnv->ExceptionClear();
	}
}

void Kamp::InitializeSAMPCallbacksInstance() {
	jmethodID getCallbacksInstanceMethodID = this->jniEnv->GetStaticMethodID(
		this->kampLauncherClass,
		KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME.c_str(),
		KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_SIGNATURE.c_str()
	);
	if (!getCallbacksInstanceMethodID) {
		throw std::exception(("Could not find method " + KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME + " in class " + KAMP_LAUNCHER_CLASS).c_str());
	}

	this->sampCallbacksInstance = this->jniEnv->CallStaticObjectMethod(this->kampLauncherClass, getCallbacksInstanceMethodID);
	if (!this->sampCallbacksInstance) {
		throw std::exception("Could not get SAMPCallbacks instance");
	}

	this->sampCallbacksInstanceReference = this->jniEnv->NewGlobalRef(this->sampCallbacksInstance);
	if (!this->sampCallbacksInstanceReference) {
		throw std::exception("Could not create global reference for SAMPCallbacks instance");
	}
}

void Kamp::InitializeSAMPCallbacksMethodCache() {
	jclass sampCallbacksInstanceClass = this->jniEnv->GetObjectClass(this->sampCallbacksInstanceReference);
	if (!sampCallbacksInstanceClass) {
		throw std::exception("Failed to get SAMPCallbacks instance class");
	}

	int initializeSAMPCallbacksMethodCacheResult = this->sampCallbacksMethodCache.Initialize(this->jniEnv, sampCallbacksInstanceClass);
	this->jniEnv->DeleteLocalRef(sampCallbacksInstanceClass);
	if (initializeSAMPCallbacksMethodCacheResult) {
		throw std::exception(("Initializing SAMPCallbacks method cache failed with result: " + std::to_string(initializeSAMPCallbacksMethodCacheResult)).c_str());
	}
}

void Kamp::Shutdown() {
	if (!this->launched) {
		return;
	}

	this->DestroyJVM();

	this->javaVM = nullptr;
	this->jniEnv = nullptr;
	this->launched = false;
	this->kampLauncherClass = nullptr;
	this->sampCallbacksInstance = nullptr;
}

long Kamp::CreateJVM() {
	std::vector<char *> optionStrings;

	std::ifstream jvmOptionsFile(KAMP_JVM_OPTIONS_FILE);
	std::string line;
	while (std::getline(jvmOptionsFile, line)) {
		if (!line.empty()) {
			optionStrings.push_back(_strdup(line.c_str()));
		}
	}

	const std::string libraryPath = "-Djava.library.path=./plugins";
	optionStrings.push_back(_strdup(libraryPath.c_str()));

	auto vmOptions = new JavaVMOption[optionStrings.size()];

	for (size_t i = 0; i < optionStrings.size(); i++) {
		vmOptions[i].optionString = optionStrings[i];
	}

	JavaVMInitArgs vmArgs;
	vmArgs.version = JNI_VERSION_1_8;
	JNI_GetDefaultJavaVMInitArgs(&vmArgs);
	vmArgs.nOptions = optionStrings.size();
	vmArgs.options = vmOptions;
	vmArgs.ignoreUnrecognized = true;

	long result = JNI_CreateJavaVM(&this->javaVM, (void **) &this->jniEnv, &vmArgs);

	for (auto it = optionStrings.begin(); it != optionStrings.end(); ++it) {
		free(*it);
	}
	delete[] vmOptions;

	return result;
}

void Kamp::DestroyJVM() {
	if (this->jniEnv) {
		this->jniEnv->ExceptionDescribe();
	}
	if (this->javaVM) {
		this->javaVM->DestroyJavaVM();
	}
}
