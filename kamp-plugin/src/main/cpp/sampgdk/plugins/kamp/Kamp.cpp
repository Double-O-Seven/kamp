
#include "Kamp.hpp"

Kamp::Kamp() {

}

Kamp::~Kamp() {
	this->Shutdown();
}

long Kamp::Launch() {
	if (this->launched) {
		return 0;
	}

	long result = this->CreateJVM();

	if (result) {
		return result;
	}

	this->launched = true;

	return result;
}

long Kamp::CreateJVM() {
	std::vector<char *> optionStrings;

	std::ifstream jvmOptionsFile(KAMP_JVM_OPTIONS_FILE);
	std::string line;
	while (std::getline(jvmOptionsFile, line)) {
		if (!line.empty()) {
			optionStrings.push_back(strdup(line.c_str()));
		}
	}

	std::string classPath = "-Djava.class.path=" + KAMP_CLASS_PATH;
	optionStrings.push_back(strdup(classPath.c_str()));

	std::string libraryPath = "-Djava.library.path=./plugins";
	optionStrings.push_back(strdup(libraryPath.c_str()));

	auto vmOptions = new JavaVMOption[optionStrings.size()];

	for (int i = 0; i < optionStrings.size(); i++) {
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

void Kamp::Shutdown() {
	if (!this->launched) {
		return;
	}

	this->javaVM->DestroyJavaVM();

	this->javaVM = nullptr;
	this->jniEnv = nullptr;
	this->launched = false;
}
