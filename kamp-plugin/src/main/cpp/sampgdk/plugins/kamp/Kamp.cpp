
#include "Kamp.hpp"

Kamp::Kamp() {

}

Kamp::~Kamp() {
	this->Shutdown();
}

void Kamp::Launch() {
	if (this->launched) {
		return;
	}

	long result = this->CreateJVM();

	if (result) {
		std::cerr << "Failed to create JVM, exiting..." << std::endl;
		exit(result);
		return;
	}

	this->kampLauncherClass = this->jniEnv->FindClass(KAMP_LAUNCHER_CLASS.c_str());
	if (this->kampLauncherClass == nullptr) {
		this->DestroyJVM();
		std::cerr << "Could not find launcher class " << KAMP_LAUNCHER_CLASS << ", exiting..." << std::endl;
		exit(1);
		return;
	}

	jmethodID launchMethodID = this->jniEnv->GetStaticMethodID(this->kampLauncherClass, KAMP_LAUNCHER_LAUNCH_METHOD_NAME.c_str(), "()V");
	if (!launchMethodID) {
		this->DestroyJVM();
		std::cerr << "Could not find method " << KAMP_LAUNCHER_LAUNCH_METHOD_NAME << " in class " << KAMP_LAUNCHER_CLASS << ", exiting..." << std::endl;
		exit(1);
		return;
	}
	this->jniEnv->CallStaticVoidMethod(this->kampLauncherClass, launchMethodID);

	jmethodID getCallbacksInstanceMethodID = this->jniEnv->GetStaticMethodID(
		this->kampLauncherClass,
		KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME.c_str(),
		KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_SIGNATURE.c_str()
	);
	if (!getCallbacksInstanceMethodID) {
		this->DestroyJVM();
		std::cerr << "Could not find method " << KAMP_LAUNCHER_GET_CALLBACKS_INSTANCE_METHOD_NAME << " in class " << KAMP_LAUNCHER_CLASS << ", exiting..." << std::endl;
		exit(1);
		return;
	}
	this->sampCallbacksInstance = this->jniEnv->CallStaticObjectMethod(this->kampLauncherClass, getCallbacksInstanceMethodID);
	if (this->sampCallbacksInstance == nullptr) {
		this->DestroyJVM();
		std::cerr << "Could not get SAMPCallbacks instance" << std::endl;
		exit(1);
		return;
	}

	this->launched = true;
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

jobject Kamp::GetSAMPCallbacksInstance() {
	return this->sampCallbacksInstance;
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

	const std::string classPath = "-Djava.class.path=" + KAMP_CLASS_PATH;
	optionStrings.push_back(_strdup(classPath.c_str()));

	const std::string libraryPath = "-Djava.library.path=./plugins";
	optionStrings.push_back(_strdup(libraryPath.c_str()));

	auto vmOptions = new JavaVMOption[optionStrings.size()];

	for (size_t i = 0; i < optionStrings.size(); i++) {
		vmOptions[i].optionString = optionStrings[i];
	}

	JavaVMInitArgs vmArgs;
	vmArgs.version = JNI_VERSION_1_8;
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
	this->jniEnv->ExceptionDescribe();
	this->javaVM->DestroyJavaVM();
}
