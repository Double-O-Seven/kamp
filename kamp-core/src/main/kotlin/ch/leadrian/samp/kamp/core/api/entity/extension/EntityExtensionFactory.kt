package ch.leadrian.samp.kamp.core.api.entity.extension

import kotlin.reflect.KClass

interface EntityExtensionFactory<E : Any, T : Any> {

    val extensionClass: KClass<T>

    fun create(entity: E): T

}