package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.SAMPNativeFunctionHook
import ch.leadrian.samp.kamp.core.api.SAMPNativeFunctionHookFactory
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SAMPNativeFunctionExecutorProviderTest {

    private val baseNativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @Test
    fun givenNoHookFactoriesItShouldReturnBaseNativeFunctionExecutor() {
        val nativeFunctionExecutorProvider = SAMPNativeFunctionExecutorProvider(emptySet(), baseNativeFunctionExecutor)

        val instance = nativeFunctionExecutorProvider.get()

        assertThat(instance)
                .isSameAs(baseNativeFunctionExecutor)
    }

    @Test
    fun shouldReturnNativeFunctionHook() {
        val nativeFunctionHookFactories = mutableSetOf(
                FooHookFactory(5),
                QuxHookFactory(69),
                BarHookFactory(1)
        )
        val nativeFunctionExecutorProvider = SAMPNativeFunctionExecutorProvider(
                nativeFunctionHookFactories,
                baseNativeFunctionExecutor
        )

        val instance = nativeFunctionExecutorProvider.get()

        assertThat(instance)
                .isInstanceOfSatisfying(BarHook::class.java) { barHook ->
                    assertThat(barHook.hookedNativeFunctionExecutor)
                            .isInstanceOfSatisfying(FooHook::class.java) { fooHook ->
                                assertThat(fooHook.hookedNativeFunctionExecutor)
                                        .isInstanceOfSatisfying(QuxHook::class.java) { quxHook ->
                                            assertThat(quxHook.hookedNativeFunctionExecutor)
                                                    .isSameAs(baseNativeFunctionExecutor)
                                        }
                            }
                }
    }

    private class BarHook(nativeFunctionExecutor: SAMPNativeFunctionExecutor) :
            SAMPNativeFunctionHook(nativeFunctionExecutor)

    private class BarHookFactory(override val priority: Int) : SAMPNativeFunctionHookFactory {

        override fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook {
            return BarHook(nativeFunctionExecutor)
        }

    }

    private class FooHook(nativeFunctionExecutor: SAMPNativeFunctionExecutor) :
            SAMPNativeFunctionHook(nativeFunctionExecutor)

    private class FooHookFactory(override val priority: Int) : SAMPNativeFunctionHookFactory {

        override fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook {
            return FooHook(nativeFunctionExecutor)
        }

    }

    private class QuxHook(nativeFunctionExecutor: SAMPNativeFunctionExecutor) :
            SAMPNativeFunctionHook(nativeFunctionExecutor)

    private class QuxHookFactory(override val priority: Int) : SAMPNativeFunctionHookFactory {

        override fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook {
            return QuxHook(nativeFunctionExecutor)
        }

    }

}