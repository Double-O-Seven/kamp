package ch.leadrian.samp.kamp.view

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ValueSupplier<T>(private var supplier: () -> T) : ReadWriteProperty<Any, T> {

    constructor(initialValue: T) : this({ initialValue })

    override fun getValue(thisRef: Any, property: KProperty<*>): T = supplier()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        supplier = { value }
    }

    fun value(supplier: () -> T) {
        this.supplier = supplier
    }
}