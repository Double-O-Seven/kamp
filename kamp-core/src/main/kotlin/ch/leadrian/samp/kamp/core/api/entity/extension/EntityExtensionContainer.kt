package ch.leadrian.samp.kamp.core.api.entity.extension

import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.EntityExtensionAlreadyInstalledException
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityExtensionException
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

class EntityExtensionContainer<E : Any>(val entity: E) : AbstractDestroyable() {

    private val extensions: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> install(entityExtensionFactory: EntityExtensionFactory<E, T>) {
        requireNotDestroyed()
        extensions.merge(entityExtensionFactory.extensionClass, entityExtensionFactory.create(entity)) { extension, _ ->
            throw EntityExtensionAlreadyInstalledException(extension.toString())
        }
    }

    operator fun <T : Any> get(entityClass: KClass<T>): T =
            entityClass.safeCast(extensions[entityClass]) ?: throw NoSuchEntityExtensionException(entityClass.qualifiedName)

    inline fun <reified T : Any> get(): T = get(T::class)

    fun getAll(): List<Any> = extensions.values.toList()

    override fun onDestroy() {
        extensions.forEach { _, extension ->
            if (extension is Destroyable) {
                extension.destroy()
            }
        }
        extensions.clear()
    }

}