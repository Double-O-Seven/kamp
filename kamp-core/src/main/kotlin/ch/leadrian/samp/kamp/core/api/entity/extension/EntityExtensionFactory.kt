package ch.leadrian.samp.kamp.core.api.entity.extension

interface EntityExtensionFactory<E : Any, T : Any> {

    fun create(entity: E): T

}