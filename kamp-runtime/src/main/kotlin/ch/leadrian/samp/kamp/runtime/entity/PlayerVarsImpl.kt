package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.PlayerVarType
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerVars
import ch.leadrian.samp.kamp.api.entity.requireOnline
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceString

internal class PlayerVarsImpl(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : PlayerVars {

    override fun setInt(varName: String, value: Int): Boolean {
        player.requireOnline()
        return nativeFunctionExecutor.setPVarInt(playerid = player.id.value, varname = varName, value = value)
    }

    override fun getInt(varName: String): Int {
        player.requireOnline()
        return nativeFunctionExecutor.getPVarInt(playerid = player.id.value, varname = varName)
    }

    override fun setString(varName: String, value: String): Boolean {
        player.requireOnline()
        return nativeFunctionExecutor.setPVarString(playerid = player.id.value, varname = varName, value = value)
    }

    override fun getString(varName: String, resultLength: Int): String? {
        player.requireOnline()
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarString(playerid = player.id.value, varname = varName, value = result, size = resultLength)
        return result.value
    }

    override fun setFloat(varName: String, value: Float): Boolean {
        player.requireOnline()
        return nativeFunctionExecutor.setPVarFloat(playerid = player.id.value, varname = varName, value = value)
    }

    override fun getFloat(varName: String): Float {
        player.requireOnline()
        return nativeFunctionExecutor.getPVarFloat(playerid = player.id.value, varname = varName)
    }

    override fun delete(varName: String): Boolean {
        player.requireOnline()
        return nativeFunctionExecutor.deletePVar(playerid = player.id.value, varname = varName)
    }

    override val upperIndex: Int
        get() {
            player.requireOnline()
            return nativeFunctionExecutor.getPVarsUpperIndex(player.id.value)
        }

    override fun getNameAtIndex(index: Int, resultLength: Int): String? {
        player.requireOnline()
        val result = ReferenceString()
        nativeFunctionExecutor.getPVarNameAtIndex(playerid = player.id.value, index = index, varname = result, size = resultLength)
        return result.value
    }

    override fun getType(varName: String): PlayerVarType {
        player.requireOnline()
        return nativeFunctionExecutor.getPVarType(playerid = player.id.value, varname = varName).let { PlayerVarType[it] }
    }
}