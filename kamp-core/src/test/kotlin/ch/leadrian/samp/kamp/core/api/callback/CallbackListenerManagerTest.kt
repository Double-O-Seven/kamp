package ch.leadrian.samp.kamp.core.api.callback

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CallbackListenerManagerTest {

    @Test
    fun givenMultipleRegistriesWithTheSameListenerClassItShouldThrowAnException() {
        val registry1 = mockk<FooCallbackListenerRegistry> {
            every { listenerClass } returns FooCallback::class
        }
        val registry2 = mockk<FooCallbackListenerRegistry> {
            every { listenerClass } returns FooCallback::class
        }
        val registry3 = mockk<BarCallbackListenerRegistry> {
            every { listenerClass } returns BarCallback::class
        }

        val caughtThrowable = catchThrowable { CallbackListenerManager(setOf(registry1, registry2, registry3)) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Nested
    inner class PostConstructionTests {

        private val fooCallbackListenerRegistry = mockk<FooCallbackListenerRegistry>()
        private val barCallbackListenerRegistry = mockk<BarCallbackListenerRegistry>()
        private lateinit var callbackListenerManager: CallbackListenerManager

        @BeforeEach
        fun setUp() {
            every { fooCallbackListenerRegistry.listenerClass } returns FooCallback::class
            every { barCallbackListenerRegistry.listenerClass } returns BarCallback::class
            callbackListenerManager = CallbackListenerManager(setOf(fooCallbackListenerRegistry, barCallbackListenerRegistry))
        }

        @Nested
        inner class RegisterTests {

            @Test
            fun shouldRegisterListenerWithUnspecifiedInterfaces() {
                every {
                    fooCallbackListenerRegistry.register(any(), any())
                } returns true
                every {
                    barCallbackListenerRegistry.register(any(), any())
                } returns false

                callbackListenerManager.register(FooService, 1337)

                verify {
                    fooCallbackListenerRegistry.register(FooService, 1337)
                    barCallbackListenerRegistry.register(FooService, 1337)
                }
            }

            @Test
            fun shouldRegisterWithSpecifiedInterface() {
                every {
                    fooCallbackListenerRegistry.register(any(), any())
                } returns true

                callbackListenerManager.registerOnlyAs(FooCallback::class, FooService, 1337)

                verify {
                    fooCallbackListenerRegistry.register(FooService, 1337)
                }
                verify(exactly = 0) {
                    barCallbackListenerRegistry.register(any(), any())
                }
            }

            @Test
            fun shouldInlineRegisterWithSpecifiedInterface() {
                every {
                    fooCallbackListenerRegistry.register(any(), any())
                } returns true

                callbackListenerManager.registerOnlyAs<FooCallback>(FooService, 1337)

                verify {
                    fooCallbackListenerRegistry.register(FooService, 1337)
                }
                verify(exactly = 0) {
                    barCallbackListenerRegistry.register(any(), any())
                }
            }

            @Test
            fun givenNoRegistryForSpecifiedClassRegisterShouldThrowException() {
                val caughtThrowable = catchThrowable {
                    callbackListenerManager.registerOnlyAs(QuxService::class, QuxService, 1337)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
                verify(exactly = 0) {
                    fooCallbackListenerRegistry.register(any(), any())
                    barCallbackListenerRegistry.register(any(), any())
                }
            }

            @Test
            fun givenNoRegistryForSpecifiedClassInlineRegisterShouldThrowException() {
                val caughtThrowable = catchThrowable {
                    callbackListenerManager.registerOnlyAs(QuxService, 1337)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
                verify(exactly = 0) {
                    fooCallbackListenerRegistry.register(any(), any())
                    barCallbackListenerRegistry.register(any(), any())
                }
            }
        }

        @Nested
        inner class UnregisterTests {

            @Test
            fun shouldUnregisterListenerWithUnspecifiedInterfaces() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns true
                every {
                    barCallbackListenerRegistry.unregister(any())
                } returns false

                callbackListenerManager.unregister(FooService)

                verify {
                    fooCallbackListenerRegistry.unregister(FooService)
                    barCallbackListenerRegistry.unregister(FooService)
                }
            }

            @Test
            fun givenListenerWasNotUnregisteredFromAnyRegistryItShouldThrowException() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns false
                every {
                    barCallbackListenerRegistry.unregister(any())
                } returns false

                val caughtThrowable = catchThrowable { callbackListenerManager.unregister(FooService) }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
                verify {
                    fooCallbackListenerRegistry.unregister(FooService)
                    barCallbackListenerRegistry.unregister(FooService)
                }
            }

            @Test
            fun shouldUnregisterWithSpecifiedInterface() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns true

                callbackListenerManager.unregisterOnlyAs(FooCallback::class, FooService)

                verify {
                    fooCallbackListenerRegistry.unregister(FooService)
                }
                verify(exactly = 0) {
                    barCallbackListenerRegistry.unregister(any())
                }
            }

            @Test
            fun shouldInlineUnregisterWithSpecifiedInterface() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns true

                callbackListenerManager.unregisterOnlyAs<FooCallback>(FooService)

                verify {
                    fooCallbackListenerRegistry.unregister(FooService)
                }
                verify(exactly = 0) {
                    barCallbackListenerRegistry.unregister(any())
                }
            }

            @Test
            fun givenUnregisterOnlyAsReturnsFalseItShouldThrowException() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns false

                val caughtThrowable = catchThrowable {
                    callbackListenerManager.unregisterOnlyAs(FooCallback::class, FooService)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
            }

            @Test
            fun givenNoRegistryForSpecifiedClassUnregisterOnlyAsShouldThrowException() {
                every {
                    fooCallbackListenerRegistry.unregister(any())
                } returns false

                val caughtThrowable = catchThrowable {
                    callbackListenerManager.unregisterOnlyAs(QuxService::class, QuxService)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
            }

            @Test
            fun givenInlineUnregisterOnlyAsReturnsFalseItShouldThrowException() {
                val caughtThrowable = catchThrowable {
                    callbackListenerManager.unregisterOnlyAs(FooService)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
            }

            @Test
            fun givenInlineNoRegistryForSpecifiedClassUnregisterOnlyAsShouldThrowException() {
                val caughtThrowable = catchThrowable {
                    callbackListenerManager.unregisterOnlyAs(QuxService)
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalStateException::class.java)
            }
        }
    }

    private interface FooCallback

    private interface BarCallback

    private class FooCallbackListenerRegistry : CallbackListenerRegistry<FooCallback>(FooCallback::class)

    private class BarCallbackListenerRegistry : CallbackListenerRegistry<BarCallback>(BarCallback::class)

    private object FooService : FooCallback

    private object QuxService

}