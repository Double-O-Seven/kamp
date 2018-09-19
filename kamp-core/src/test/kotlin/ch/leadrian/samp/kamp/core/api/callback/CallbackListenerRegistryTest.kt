package ch.leadrian.samp.kamp.core.api.callback

import io.mockk.mockk
import io.mockk.verifySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CallbackListenerRegistryTest {

    private val callbackListenerRegistry = CallbackListenerRegistry(CallbackListener::class)

    @Test
    fun shouldRegisterCallbackListener() {
        callbackListenerRegistry.register(FooCallbackListener)

        assertThat(callbackListenerRegistry.listeners)
                .containsExactly(FooCallbackListener)
    }

    @Test
    fun shouldReturnListenersInExpectedOrder() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)


        val listeners = callbackListenerRegistry.listeners

        assertThat(listeners)
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

        assertThat(callbackListenerRegistry.listeners)
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

        assertThat(callbackListenerRegistry.listeners)
                .containsExactly(
                        QuxCallbackListener,
                        BatCallbackListener,
                        BarCallbackListener,
                        FooCallbackListener,
                        BazCallbackListener
                )
    }

    @Test
    fun shouldExecuteActionForEachListenerInExpectedOrder() {
        val action = mockk<(CallbackListener) -> Unit>(relaxed = true)
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)

        callbackListenerRegistry.forEach(action)

        verifySequence {
            action.invoke(BatCallbackListener)
            action.invoke(BarCallbackListener)
            action.invoke(FooCallbackListener)
            action.invoke(BazCallbackListener)
            action.invoke(QuxCallbackListener)
        }
    }

    @Test
    fun shouldRemoveCallbackListener() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)

        callbackListenerRegistry.unregister(QuxCallbackListener)

        assertThat(callbackListenerRegistry.listeners)
                .containsExactlyInAnyOrder(FooCallbackListener)
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