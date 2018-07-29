
#ifndef FIELD_CACHE_HPP
#define FIELD_CACHE_HPP

#include <string>

#include <jni.h>

const std::string REFERENCE_FLOAT_CLASS = "ch/leadrian/samp/kamp/runtime/types/ReferenceFloat";
const std::string REFERENCE_INT_CLASS = "ch/leadrian/samp/kamp/runtime/types/ReferenceInt";
const std::string REFERENCE_STRING_CLASS = "ch/leadrian/samp/kamp/runtime/types/ReferenceString";

class FieldCache {

public:
	int Initialize(JNIEnv *jniEnv);

	jfieldID GetReferenceFloatValueFieldID() {
		return this->referenceFloatValueFieldID;
	}

	jfieldID GetReferenceIntValueFieldID() {
		return this->referenceIntValueFieldID;
	}

	jfieldID GetReferenceStringValueFieldID() {
		return this->referenceStringValueFieldID;
	}

private:
	int InitializeReferenceFloatValueField(JNIEnv *jniEnv);
	int InitializeReferenceIntValueField(JNIEnv *jniEnv);
	int InitializeReferenceStringValueField(JNIEnv *jniEnv);

	jfieldID referenceFloatValueFieldID;
	jfieldID referenceIntValueFieldID;
	jfieldID referenceStringValueFieldID;

};

#endif
