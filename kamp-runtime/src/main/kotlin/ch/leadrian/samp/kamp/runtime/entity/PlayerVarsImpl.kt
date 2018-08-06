package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.PlayerVarType
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerVars
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

class PlayerVarsImpl(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : PlayerVars {

    override fun setInt(varName: String, value: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(varName: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setString(varName: String, value: String, resultLength: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(varName: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFloat(varName: String, value: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(varName: String): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(varName: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val upperIndex: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getNameAtIndex(index: Int, resultLength: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(varName: String): PlayerVarType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}