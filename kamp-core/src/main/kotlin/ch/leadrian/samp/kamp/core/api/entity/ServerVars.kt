package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ServerVarType
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

class ServerVars
internal constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    val upperIndex: Int
        get() = nativeFunctionExecutor.getSVarsUpperIndex()

    fun setInt(varName: String, value: Int): Boolean = nativeFunctionExecutor.setSVarInt(varName, value)

    fun getInt(varName: String): Int = nativeFunctionExecutor.getSVarInt(varName)

    fun setString(varName: String, value: String): Boolean =
            nativeFunctionExecutor.setSVarString(varname = varName, string_value = value)

    fun getString(varName: String, resultLength: Int = 256): String? {
        val value = ReferenceString()
        nativeFunctionExecutor.getSVarString(varName, value, resultLength)
        return value.value
    }

    fun setFloat(varName: String, value: Float): Boolean = nativeFunctionExecutor.setSVarFloat(varName, value)

    fun getFloat(varName: String): Float = nativeFunctionExecutor.getSVarFloat(varName)

    fun delete(varName: String): Boolean = nativeFunctionExecutor.deleteSVar(varName)

    fun getNameAtIndex(index: Int, resultLength: Int = 256): String? {
        val value = ReferenceString()
        nativeFunctionExecutor.getSVarNameAtIndex(index, value, resultLength)
        return value.value
    }

    fun getType(varName: String): ServerVarType = nativeFunctionExecutor.getSVarType(varName).let { ServerVarType[it] }
}