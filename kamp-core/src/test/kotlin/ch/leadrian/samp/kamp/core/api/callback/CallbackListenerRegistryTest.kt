package ch.leadrian.samp.kamp.core.api.callback

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CallbackListenerRegistryTest {

    private val callbackListenerRegistry = CallbackListenerRegistry(CallbackListener::class)

    @Test
    fun shouldRegisterCallbackListener() {
        val result = callbackListenerRegistry.register(FooCallbackListener)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactly(FooCallbackListener)
        assertThat(result)
                .isTrue()
    }

    @Test
    fun givenObjectThatIsInstanceOfListenerClassItShouldNotRegisterObjectAsListener() {
        val listener = "Hi there"
        val result = callbackListenerRegistry.register(listener)

        assertThat(callbackListenerRegistry.listeners.toList())
                .isEmpty()
        assertThat(result)
                .isFalse()
    }

    @Test
    fun shouldReturnListenersInExpectedOrder() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)

        val listeners = callbackListenerRegistry.listeners

        assertThat(listeners.toList())
                .containsExactly(
                        BatCallbackListener,
                        BarCallbackListener,
                        FooCallbackListener,
                        BazCallbackListener,
                        QuxCallbackListener
                )
    }

    @Test
    fun shouldNotContainDuplicates() {
        callbackListenerRegistry.register(FooCallbackListener)

        callbackListenerRegistry.register(FooCallbackListener, 99)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactly(FooCallbackListener)
    }

    @Test
    fun registeringAgainShouldUpdatePriority() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)

        callbackListenerRegistry.register(QuxCallbackListener, 99)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactly(
                        QuxCallbackListener,
                        BatCallbackListener,
                        BarCallbackListener,
                        FooCallbackListener,
                        BazCallbackListener
                )
    }

    @Test
    fun shouldRemoveCallbackListener() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)

        val removed = callbackListenerRegistry.unregister(QuxCallbackListener)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactlyInAnyOrder(FooCallbackListener)
        assertThat(removed)
                .isTrue()
    }

    @Test
    fun givenUnregisteredListenerItShouldNotRemoveAnyOther() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)

        val removed = callbackListenerRegistry.unregister(BarCallbackListener)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactlyInAnyOrder(QuxCallbackListener, FooCallbackListener)
        assertThat(removed)
                .isFalse()
    }

    @Test
    fun shouldRegisterCallbackListenersWithSamePriority() {
        callbackListenerRegistry.register(QuxCallbackListener, 0)
        callbackListenerRegistry.register(FooCallbackListener, 0)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactlyInAnyOrder(QuxCallbackListener, FooCallbackListener)
    }

    @Priority(5, CallbackListener::class)
    private object FooCallbackListener : CallbackListener

    @Priorities(Priority(6, CallbackListener::class))
    private object BarCallbackListener : CallbackListener

    @Priorities(Priority(7, CallbackListener::class))
    @Priority(3, CallbackListener::class)
    private object BazCallbackListener : CallbackListener

    @Priorities(Priority(8, CallbackListener::class))
    private object BatCallbackListener : CallbackListener

    @Priorities(Priority(9, String::class))
    private object QuxCallbackListener : CallbackListener

    private interface CallbackListener

}