package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.PlayerVarType
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

class PlayerVars
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    val upperIndex: Int
        get() = nativeFunctionExecutor.getPVarsUpperIndex(player.id.value)

    fun setInt(varName: String, value: Int): Boolean =
            nativeFunctionExecutor.setPVarInt(playerid = player.id.value, varname = varName, value = value)

    fun getInt(varName: String): Int =
            nativeFunctionExecutor.getPVarInt(playerid = player.id.value, varname = varName)

    fun setString(varName: String, value: String): Boolean =
            nativeFunctionExecutor.setPVarString(playerid = player.id.value, varname = varName, value = value)

    fun getString(varName: String, resultLength: Int = 256): String? {
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarString(
                playerid = player.id.value,
                varname = varName,
                value = result,
                size = resultLength
        )
        return result.value
    }

    fun setFloat(varName: String, value: Float): Boolean =
            nativeFunctionExecutor.setPVarFloat(playerid = player.id.value, varname = varName, value = value)

    fun getFloat(varName: String): Float =
            nativeFunctionExecutor.getPVarFloat(playerid = player.id.value, varname = varName)

    fun delete(varName: String): Boolean =
            nativeFunctionExecutor.deletePVar(playerid = player.id.value, varname = varName)

    fun getNameAtIndex(index: Int, resultLength: Int = 256): String? {
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarNameAtIndex(
                playerid = player.id.value,
                index = index,
                varname = result,
                size = resultLength
        )
        return result.value
    }

    fun getType(varName: String): PlayerVarType =
            nativeFunctionExecutor.getPVarType(playerid = player.id.value, varname = varName).let { PlayerVarType[it] }
}