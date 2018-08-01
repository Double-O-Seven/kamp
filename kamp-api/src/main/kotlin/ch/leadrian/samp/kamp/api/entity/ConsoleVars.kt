package ch.leadrian.samp.kamp.api.entity

interface ConsoleVars {

    fun getString(varName: String, resultLength: Int = 256): String

    fun getInt(varName: String): Int

    fun getBoolean(varName: String): Boolean

}