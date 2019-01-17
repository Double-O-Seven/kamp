package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class CallbackListenerManager
@Inject
internal constructor(callbackListenerRegistries: Set<@JvmSuppressWildcards CallbackListenerRegistry<*>>) {

    private companion object {

        val log = loggerFor<CallbackListenerManager>()

    }

    private val callbackListenerRegistries: MutableMap<KClass<out Any>, CallbackListenerRegistry<*>> = mutableMapOf()

    init {
        callbackListenerRegistries.forEach {
            this.callbackListenerRegistries.merge(it.listenerClass, it) { oldValue, newValue ->
                throw IllegalArgumentException("Duplicate listeners for class ${it.listenerClass}: $oldValue, $newValue")
            }
        }
    }

    @PostConstruct
    internal fun printCallbackListenerRegistries() {
        callbackListenerRegistries.forEach { listenerClass, registry ->
            log.info(
                    "Using {} as callback listener registry for {}",
                    registry::class.qualifiedName,
                    listenerClass.qualifiedName
            )
        }
    }

    fun <T : Any> registerOnlyAs(clazz: KClass<T>, listener: T, priority: Int? = null) {
        callbackListenerRegistries[clazz]?.register(listener, priority)
                ?: throw IllegalStateException("$listener could not be registered as $clazz")
    }

    inline fun <reified T : Any> registerOnlyAs(listener: T, priority: Int? = null) {
        registerOnlyAs(T::class, listener, priority)
    }

    fun <T : Any> unregisterOnlyAs(clazz: KClass<T>, listener: T) {
        val unregistered = callbackListenerRegistries[clazz]?.unregister(listener) ?: false
        if (!unregistered) {
            throw IllegalStateException("$listener could not be unregistered as $clazz")
        }
    }

    inline fun <reified T : Any> unregisterOnlyAs(listener: T) {
        unregisterOnlyAs(T::class, listener)
    }

    fun register(listener: Any, priority: Int? = null) {
        var registrations = 0
        callbackListenerRegistries.forEach {
            if (it.value.register(listener, priority)) {
                registrations++
            }
        }
        if (registrations == 0) {
            throw IllegalStateException("$listener was not registered as any listener")
        }
    }

    fun unregister(listener: Any) {
        var unregistered = false
        callbackListenerRegistries.forEach {
            unregistered = it.value.unregister(listener) || unregistered
        }
        if (!unregistered) {
            throw IllegalStateException("$listener was not unregistered as any listener")
        }
    }

}