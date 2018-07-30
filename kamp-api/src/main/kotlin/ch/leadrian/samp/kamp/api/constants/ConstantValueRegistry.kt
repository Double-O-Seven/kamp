package ch.leadrian.samp.kamp.api.constants

open class ConstantValueRegistry<K, V : ConstantValue<K>>(vararg constants: V) {

    private val sampConstantsByValue: MutableMap<K, V> = mutableMapOf()

    init {
        constants.forEach { sampConstantsByValue[it.value] = it }
    }

    operator fun get(value: K): V? =
            sampConstantsByValue[value] ?: throw IllegalArgumentException("No constant with value $value")
}