package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigationElementFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigationFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigator
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.inject.Inject


internal class ViewModuleTest {

    private val modules = arrayOf(
            ViewModule(),
            TestModule()
    )

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(*modules)
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(*modules)
        }

        @Test
        fun shouldInjectViewContext() {
            val viewContext = injector.getInstance<ViewContext>()

            assertThat(viewContext)
                    .isNotNull
        }

        @Test
        fun shouldInjectViewLayoutCalculator() {
            val viewLayoutCalculator = injector.getInstance<ViewLayoutCalculator>()

            assertThat(viewLayoutCalculator)
                    .isNotNull
        }

        @Test
        fun shouldInjectViewAreaCalculator() {
            val viewAreaCalculator = injector.getInstance<ViewAreaCalculator>()

            assertThat(viewAreaCalculator)
                    .isNotNull
        }

        @Test
        fun shouldInjectAbsoluteViewDimensionsCalculator() {
            val absoluteViewDimensionsCalculator = injector.getInstance<AbsoluteViewDimensionsCalculator>()

            assertThat(absoluteViewDimensionsCalculator)
                    .isNotNull
        }

        @Test
        fun shouldInjectViewFactory() {
            val viewFactory = injector.getInstance<ViewFactory>()

            assertThat(viewFactory)
                    .isNotNull
                    .isInstanceOf(DefaultViewFactory::class.java)
        }

        @Test
        fun shouldInjectViewNavigationElementFactory() {
            val viewNavigationElementFactory = injector.getInstance<ViewNavigationElementFactory>()

            assertThat(viewNavigationElementFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectSetOfPlayerExtensionFactories() {
            val fooService = injector.getInstance<FooService>()

            assertThat(fooService.playerExtensionFactories)
                    .hasSize(1)
                    .allSatisfy {
                        assertThat(it)
                                .isInstanceOf(ViewNavigationFactory::class.java)
                    }
        }

        @Test
        fun shouldInjectViewNavigator() {
            val viewNavigator = injector.getInstance<ViewNavigator>()

            assertThat(viewNavigator)
                    .isNotNull
                    .isInstanceOf(ViewNavigator::class.java)
                    .isSameAs(injector.getInstance<ViewNavigator>())
        }
    }

    private class FooService
    @Inject
    constructor(val playerExtensionFactories: Set<@JvmSuppressWildcards EntityExtensionFactory<Player, *>>)

    private class TestModule : KampModule() {

        override fun configure() {
            bind(TextProvider::class.java).toInstance(mockk())
            bind(TextFormatter::class.java).toInstance(mockk())
            bind(PlayerTextDrawService::class.java).toInstance(mockk())
            bind(CallbackListenerManager::class.java).toInstance(mockk())
        }

    }
}