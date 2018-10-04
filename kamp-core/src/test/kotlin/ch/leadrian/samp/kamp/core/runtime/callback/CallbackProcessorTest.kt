package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeExitListener
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeInitListener
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.Server
import com.google.inject.Module
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.util.*
import javax.annotation.PreDestroy
import javax.inject.Singleton

internal class CallbackProcessorTest {

    private lateinit var callbackProcessor: CallbackProcessor

    private lateinit var callbackListenerManager: CallbackListenerManager
    private lateinit var server: Server

    private val configProperties = Properties()
    private val dataDirectory = Paths.get(".", "Kamp", "data")

    @BeforeEach
    fun setUp() {
        configProperties["kamp.gamemode.class.name"] = "ch.leadrian.samp.kamp.core.runtime.callback.CallbackProcessorTest\$TestGameMode"
        configProperties["test.value.foo"] = "Foobar"
        configProperties["test.value.bar"] = "1337"
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns 50
            every { initialize() } just Runs
        }
        server = Server.start(nativeFunctionExecutor, configProperties, dataDirectory)
        callbackProcessor = server.injector.getInstance()
        callbackListenerManager = server.injector.getInstance()
    }

    @Nested
    inner class OnProcessTickTests {

        @Test
        fun shouldCallOnProcessTick() {
            val onProcessTickListener = mockk<OnProcessTickListener>(relaxed = true)
            callbackListenerManager.register(onProcessTickListener)

            callbackProcessor.onProcessTick()

            verify { onProcessTickListener.onProcessTick() }
        }

        @Test
        fun shouldCatchException() {
            val onProcessTickListener = mockk<OnProcessTickListener> {
                every { onProcessTick() } throws RuntimeException("test")
            }
            callbackListenerManager.register(onProcessTickListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onProcessTick() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { onProcessTickListener.onProcessTick() }
        }

    }

    @Nested
    inner class OnGameModeInitTests {

        @Test
        fun shouldCallOnGameModeInit() {
            val onGameModeInitListener = mockk<OnGameModeInitListener>(relaxed = true)
            callbackListenerManager.register(onGameModeInitListener)

            callbackProcessor.onGameModeInit()

            verify { onGameModeInitListener.onGameModeInit() }
        }

        @Test
        fun shouldCatchException() {
            val onGameModeInitListener = mockk<OnGameModeInitListener> {
                every { onGameModeInit() } throws RuntimeException("test")
            }
            callbackListenerManager.register(onGameModeInitListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onGameModeInit() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { onGameModeInitListener.onGameModeInit() }
        }

    }

    @Nested
    inner class OnGameModeExitTests {

        @Test
        fun shouldCallOnGameModeExit() {
            val onGameModeExitListener = mockk<OnGameModeExitListener>(relaxed = true)
            callbackListenerManager.register(onGameModeExitListener)

            callbackProcessor.onGameModeExit()

            verify { onGameModeExitListener.onGameModeExit() }
        }

        @Test
        fun shouldCatchException() {
            val onGameModeExitListener = mockk<OnGameModeExitListener> {
                every { onGameModeExit() } throws RuntimeException("test")
            }
            callbackListenerManager.register(onGameModeExitListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onGameModeExit() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { onGameModeExitListener.onGameModeExit() }
        }

        @Test
        fun shouldStopServer() {
            val lifecycleAwareService = server.injector.getInstance<LifecycleAwareService>()

            callbackProcessor.onGameModeExit()

            assertThat(lifecycleAwareService.isShutdown)
                    .isTrue()
        }

    }

    @Suppress("unused")
    @Singleton
    private class LifecycleAwareService {

        var isShutdown: Boolean = false
            private set

        @PreDestroy
        fun shutdown() {
            isShutdown = true
        }

    }

    @Suppress("unused")
    class TestGameMode : GameMode() {

        override fun getPlugins(): List<Plugin> = emptyList()

        override fun getModules(): List<Module> = emptyList()

    }

}