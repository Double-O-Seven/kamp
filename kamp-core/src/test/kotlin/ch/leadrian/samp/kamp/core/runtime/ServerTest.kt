package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.getInstance
import com.google.inject.Module
import com.netflix.governator.annotations.Configuration
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*
import javax.inject.Inject

internal class ServerTest {

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.getMaxPlayers() } returns 50
        every { nativeFunctionExecutor.initialize() } just Runs
    }

    @Test
    fun shouldStart() {
        val caughtThrowable = catchThrowable { Server.start(nativeFunctionExecutor) }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class AfterStartTests {

        private lateinit var server: Server

        @BeforeEach
        fun setUp() {
            server = Server.start(nativeFunctionExecutor)
        }

        @ParameterizedTest
        @CsvSource(
                "en, US, test.foo, 'Hi there'",
                "en, US, test.bar, 'How are you?'",
                "de, CH, test.foo, 'Hallo'",
                "de, CH, test.bar, 'Wie geht es dir?'"
        )
        fun shouldLoadStringProperties(language: String, country: String, textKey: TextKey, expectedText: String) {
            val locale = Locale(language, country)
            val textProvider = server.injector.getInstance<TextProvider>()

            val text = textProvider.getText(locale, textKey)

            assertThat(text)
                    .isEqualTo(expectedText)
        }

        @Nested
        inner class GameModeTests {

            @Test
            fun shouldInjectGameModeMembers() {
                val gameMode = server.injector.getInstance<GameMode>()

                assertThat(gameMode)
                        .isInstanceOfSatisfying(TestGameMode::class.java) {
                            assertThat(it.fooService)
                                    .isNotNull
                            assertThat(it.foo)
                                    .isEqualTo("Foobar")
                        }
            }

            @Test
            fun shouldInjectGameModeAsSingleton() {
                val gameMode = server.injector.getInstance<GameMode>()

                assertThat(gameMode)
                        .isSameAs(server.injector.getInstance<GameMode>())
            }

        }

        @Nested
        inner class PluginTests {

            @Test
            fun shouldInjectPluginMembers() {
                val fooPlugin = server.injector.getInstance<FooPlugin>()
                val barPlugin = server.injector.getInstance<BarPlugin>()

                assertThat(fooPlugin.bar)
                        .isEqualTo(1337)
                assertThat(barPlugin.barService)
                        .isNotNull
            }

            @Test
            fun shouldInjectPluginsAsSingletons() {
                val fooPlugin = server.injector.getInstance<FooPlugin>()
                val barPlugin = server.injector.getInstance<BarPlugin>()

                assertThat(fooPlugin)
                        .isSameAs(server.injector.getInstance<FooPlugin>())
                assertThat(barPlugin)
                        .isSameAs(server.injector.getInstance<BarPlugin>())
            }

        }

    }

    @Suppress("unused")
    class TestGameMode : GameMode() {

        @Configuration("test.value.foo")
        var foo: String? = null

        @Inject
        var fooService: FooService? = null

        override fun getModules(): List<Module> = emptyList()

        override fun getPlugins(): List<Plugin> = listOf(FooPlugin(), BarPlugin())

    }

    @Suppress("unused")
    private class FooPlugin : Plugin() {

        @Configuration("test.value.bar")
        var bar: Int = 0

        override fun getModules(): List<Module> = emptyList()

    }

    @Suppress("unused")
    private class BarPlugin : Plugin() {

        @Inject
        var barService: BarService? = null

        override fun getModules(): List<Module> = emptyList()

    }

    class FooService

    class BarService

}