package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

class ConsoleVars
internal constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun getString(varName: String, resultLength: Int = 256): String {
        val varValue = ReferenceString()
        nativeFunctionExecutor.getConsoleVarAsString(varname = varName, buffer = varValue, len = resultLength)
        return varValue.value ?: ""
    }

    fun getInt(varName: String): Int =
            nativeFunctionExecutor.getConsoleVarAsInt(varName)

    fun getBoolean(varName: String): Boolean =
            nativeFunctionExecutor.getConsoleVarAsBool(varName)
}