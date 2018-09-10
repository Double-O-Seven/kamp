package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.ConsoleVars
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal class ConsoleVarsImpl(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) : ConsoleVars {

    override fun getString(varName: String, resultLength: Int): String {
        val varValue = ReferenceString()
        nativeFunctionExecutor.getConsoleVarAsString(varname = varName, buffer = varValue, len = resultLength)
        return varValue.value ?: ""
    }

    override fun getInt(varName: String): Int =
            nativeFunctionExecutor.getConsoleVarAsInt(varName)

    override fun getBoolean(varName: String): Boolean =
            nativeFunctionExecutor.getConsoleVarAsBool(varName)
}