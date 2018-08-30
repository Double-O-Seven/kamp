package ch.leadrian.samp.kamp.api.constants

open class ConstantValueRegistry<K, V : ConstantValue<K>>(vararg constants: V, private val defaultValue: V? = null) {

    private val constantsByValue: MutableMap<K, V> = mutableMapOf()

    init {
        constants.forEach { constantsByValue[it.value] = it }
    }

    operator fun get(value: K): V =
            constantsByValue[value] ?: defaultValue ?: throw IllegalArgumentException("No constant with value $value")

    fun exists(value: K): Boolean = constantsByValue.containsKey(value)
}