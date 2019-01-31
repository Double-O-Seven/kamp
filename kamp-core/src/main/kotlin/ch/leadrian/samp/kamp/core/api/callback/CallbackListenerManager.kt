package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * [CallbackListenerManager] allows you to register and unregister any object as a listener for a given callback.
 *
 * In most cases, the callback listener class should be annotated with [javax.inject.Singleton] and implement one or more callback listener interfaces.
 * It is recommended to register an object in the [CallbackListenerManager] in an initialization function annotated with [javax.annotation.PostConstruct].
 * Further more, the callback listener class should be bound as eager singleton in your [com.google.inject.Module].
 *
 * As an alternative, the callback listener class may be annotated with the deprecated [com.netflix.governator.annotations.AutoBindSingleton].
 * Be aware, that the base package must be returned by [ch.leadrian.samp.kamp.core.api.GameMode.getTextProviderResourcePackages]
 * or [ch.leadrian.samp.kamp.core.api.Plugin.getTextProviderResourcePackages] of your game mode or plugin.
 *
 * Example:
 * ```
 * @Singleton
 * class FooService
 * @Inject
 * constructor(
 *         private val callbackListenerManager: CallbackListenerManager
 * ) : OnPlayerConnectListener {
 *
 *     @PostConstruct
 *     fun initialize() {
 *         callbackListenerManager.register(this)
 *     }
 *
 *     override fun onPlayerConnect(player: Player) {
 *         // Say hello to the player.
 *     }
 * }
 * ```
 *
 * In order to register a callback listener, a suitable [CallbackListenerRegistry] must be bound in a [ch.leadrian.samp.kamp.core.api.inject.KampModule]
 * using [ch.leadrian.samp.kamp.core.api.inject.KampModule.newCallbackListenerRegistrySetBinder].
 *
 * @see [com.google.inject.binder.ScopedBindingBuilder.asEagerSingleton]
 * @see [ch.leadrian.samp.kamp.core.api.inject.KampModule]
 * @see [com.google.inject.AbstractModule]
 * @see [CallbackListenerRegistry]
 * @see [OnPlayerConnectListener]
 * @see [ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerConnectHandler]
 */
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

    /**
     * Registers [listener] only as a listener of type [T].
     * Any other known callback listener interfaces that may be implemented by [listener] will be ignored.
     *
     * If [priority] is not null, [listener] will be registered with priority [priority],
     * else, if any [Priority] or [Priorities] annotation is present, the priority will be derived from those,
     * else, the default priority of 0 will be used.
     *
     * @throws [IllegalStateException] if [listener] does not implement [T]
     * @see [Priority]
     * @see [Priorities]
     */
    fun <T : Any> registerOnlyAs(clazz: KClass<T>, listener: T, priority: Int? = null) {
        callbackListenerRegistries[clazz]?.register(listener, priority)
                ?: throw IllegalStateException("$listener could not be registered as $clazz")
    }

    /**
     * @see [registerOnlyAs] without inline
     */
    inline fun <reified T : Any> registerOnlyAs(listener: T, priority: Int? = null) {
        registerOnlyAs(T::class, listener, priority)
    }

    /**
     * Unregisters [listener] only as a listener of type [T].
     * Any other known callback listener interfaces that may be implemented by [listener] will be ignored.
     *
     * @throws [IllegalStateException] if [listener] was not registered as listener of type [T]
     */
    fun <T : Any> unregisterOnlyAs(clazz: KClass<T>, listener: T) {
        val unregistered = callbackListenerRegistries[clazz]?.unregister(listener) ?: false
        if (!unregistered) {
            throw IllegalStateException("$listener could not be unregistered as $clazz")
        }
    }

    /**
     * @see [unregisterOnlyAs] without inline
     */
    inline fun <reified T : Any> unregisterOnlyAs(listener: T) {
        unregisterOnlyAs(T::class, listener)
    }

    /**
     * Registers [listener] as listener for all known callbacks.
     *
     * If [priority] is not null, [listener] will be registered with priority [priority],
     * else, if any [Priority] or [Priorities] annotation is present, the priority will be derived from those,
     * else, the default priority of 0 will be used.
     *
     * @throws [IllegalStateException] if [listener] does not implement any known callback listener interface
     */
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

    /**
     * Unregisters [listener] as listener for all known callbacks.
     *
     * @throws [IllegalStateException] if [listener] was not registered
     */
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