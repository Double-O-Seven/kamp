
#include <sampgdk/interop.h>

#include "AmxNativeFunctionInvoker.h"

JNIEXPORT jint JNICALL Java_ch_leadrian_samp_kamp_core_runtime_amx_AmxNativeFunctionInvokerImpl_findNative(JNIEnv *env, jobject, jstring name) {
    const char *nameChars = env->GetStringUTFChars(name, NULL);
    auto native = sampgdk_FindNative(nameChars);
    env->ReleaseStringUTFChars(name, nameChars);
    return reinterpret_cast<jint>(native);
}

JNIEXPORT jint JNICALL Java_ch_leadrian_samp_kamp_core_runtime_amx_AmxNativeFunctionInvokerImpl_callNative(JNIEnv *env, jobject, jint native, jintArray args) {
    jsize argsLength = env->GetArrayLength(args);
    cell *cellArgs = new cell[argsLength + 1];
    cellArgs[0] = argsLength * sizeof(cell);
    env->GetIntArrayRegion(args, 0, argsLength, reinterpret_cast<jint *>(&cellArgs[1]));
    auto result = sampgdk_CallNative(reinterpret_cast<AMX_NATIVE>(native), cellArgs);
    delete[] cellArgs;
    return result;
}

JNIEXPORT jint JNICALL Java_ch_leadrian_samp_kamp_core_runtime_amx_AmxNativeFunctionInvokerImpl_invokeNative(JNIEnv *env, jobject, jint native, jstring format, jint argsAddress) {
    const char *formatChars = env->GetStringUTFChars(format, NULL);
    auto result = sampgdk_InvokeNativeArray(reinterpret_cast<AMX_NATIVE>(native), formatChars, reinterpret_cast<void **>(argsAddress));
    env->ReleaseStringUTFChars(format, formatChars);
    return result;
}
