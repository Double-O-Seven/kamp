
#include "FieldCache.hpp"

int FieldCache::Initialize(JNIEnv *jniEnv) {
	int result;

	result = this->InitializeReferenceFloatValueField(jniEnv);
	if (result) {
		return result;
	}

	result = this->InitializeReferenceIntValueField(jniEnv);
	if (result) {
		return result;
	}

	result = this->InitializeReferenceStringValueField(jniEnv);
	if (result) {
		return result;
	}

	return 0;
}

int FieldCache::InitializeReferenceFloatValueField(JNIEnv *jniEnv) {
	jclass referenceFloatClass = jniEnv->FindClass(REFERENCE_FLOAT_CLASS.c_str());
	if (referenceFloatClass == nullptr) {
		return -1;
	}

	this->referenceFloatValueFieldID = jniEnv->GetFieldID(referenceFloatClass, "value", "F");

	jniEnv->DeleteLocalRef(referenceFloatClass);

	return 0;
}

int FieldCache::InitializeReferenceIntValueField(JNIEnv *jniEnv) {
	jclass referenceIntClass = jniEnv->FindClass(REFERENCE_INT_CLASS.c_str());
	if (referenceIntClass == nullptr) {
		return -1;
	}

	this->referenceIntValueFieldID = jniEnv->GetFieldID(referenceIntClass, "value", "I");

	jniEnv->DeleteLocalRef(referenceIntClass);

	return 0;
}

int FieldCache::InitializeReferenceStringValueField(JNIEnv *jniEnv) {
	jclass referenceStringClass = jniEnv->FindClass(REFERENCE_STRING_CLASS.c_str());
	if (referenceStringClass == nullptr) {
		return -1;
	}

	this->referenceStringValueFieldID = jniEnv->GetFieldID(referenceStringClass, "value", "Ljava/lang/String;");

	jniEnv->DeleteLocalRef(referenceStringClass);

	return 0;
}
