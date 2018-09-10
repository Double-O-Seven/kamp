package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerVars
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal class PlayerVarsImpl(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : PlayerVars {

    override fun setInt(varName: String, value: Int): Boolean =
            nativeFunctionExecutor.setPVarInt(playerid = player.id.value, varname = varName, value = value)

    override fun getInt(varName: String): Int =
            nativeFunctionExecutor.getPVarInt(playerid = player.id.value, varname = varName)

    override fun setString(varName: String, value: String): Boolean =
            nativeFunctionExecutor.setPVarString(playerid = player.id.value, varname = varName, value = value)

    override fun getString(varName: String, resultLength: Int): String? {
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarString(playerid = player.id.value, varname = varName, value = result, size = resultLength)
        return result.value
    }

    override fun setFloat(varName: String, value: Float): Boolean =
            nativeFunctionExecutor.setPVarFloat(playerid = player.id.value, varname = varName, value = value)

    override fun getFloat(varName: String): Float =
            nativeFunctionExecutor.getPVarFloat(playerid = player.id.value, varname = varName)

    override fun delete(varName: String): Boolean =
            nativeFunctionExecutor.deletePVar(playerid = player.id.value, varname = varName)

    override val upperIndex: Int
        get() = nativeFunctionExecutor.getPVarsUpperIndex(player.id.value)

    override fun getNameAtIndex(index: Int, resultLength: Int): String? {
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarNameAtIndex(playerid = player.id.value, index = index, varname = result, size = resultLength)
        return result.value
    }

    override fun getType(varName: String): ch.leadrian.samp.kamp.core.api.constants.PlayerVarType =
            nativeFunctionExecutor.getPVarType(playerid = player.id.value, varname = varName).let { ch.leadrian.samp.kamp.core.api.constants.PlayerVarType[it] }
}