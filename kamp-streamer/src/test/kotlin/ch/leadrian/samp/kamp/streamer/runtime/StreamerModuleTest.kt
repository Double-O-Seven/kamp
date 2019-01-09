package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.streamer.api.service.StreamableMapObjectService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableTextLabelService
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateMachineFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelStateFactory
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.inject.Inject

internal class StreamerModuleTest {

    private val modules = arrayOf(StreamerModule(), TestModule)

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
        fun shouldInjectOnStreamableMapObjectMovedHandlerAsSingleton() {
            val onStreamableMapObjectMovedHandler = injector.getInstance<OnStreamableMapObjectMovedHandler>()

            assertThat(onStreamableMapObjectMovedHandler)
                    .isNotNull
                    .isInstanceOf(OnStreamableMapObjectMovedHandler::class.java)
                    .isSameAs(injector.getInstance<OnStreamableMapObjectMovedHandler>())
        }

        @Test
        fun shouldInjectOnPlayerEditStreamableMapObjectHandlerAsSingleton() {
            val onPlayerEditStreamableMapObjectHandler = injector.getInstance<OnPlayerEditStreamableMapObjectHandler>()

            assertThat(onPlayerEditStreamableMapObjectHandler)
                    .isNotNull
                    .isInstanceOf(OnPlayerEditStreamableMapObjectHandler::class.java)
                    .isSameAs(injector.getInstance<OnPlayerEditStreamableMapObjectHandler>())
        }

        @Test
        fun shouldInjectOnPlayerSelectStreamableMapObjectHandlerAsSingleton() {
            val onPlayerSelectStreamableMapObjectHandler = injector.getInstance<OnPlayerSelectStreamableMapObjectHandler>()

            assertThat(onPlayerSelectStreamableMapObjectHandler)
                    .isNotNull
                    .isInstanceOf(OnPlayerSelectStreamableMapObjectHandler::class.java)
                    .isSameAs(injector.getInstance<OnPlayerSelectStreamableMapObjectHandler>())
        }

        @Test
        fun shouldInjectOnStreamableMapObjectStreamInHandlerAsSingleton() {
            val onStreamableMapObjectStreamInHandler = injector.getInstance<OnStreamableMapObjectStreamInHandler>()

            assertThat(onStreamableMapObjectStreamInHandler)
                    .isNotNull
                    .isInstanceOf(OnStreamableMapObjectStreamInHandler::class.java)
                    .isSameAs(injector.getInstance<OnStreamableMapObjectStreamInHandler>())
        }

        @Test
        fun shouldInjectOnStreamableMapObjectStreamOutHandlerAsSingleton() {
            val onStreamableMapObjectStreamOutHandler = injector.getInstance<OnStreamableMapObjectStreamOutHandler>()

            assertThat(onStreamableMapObjectStreamOutHandler)
                    .isNotNull
                    .isInstanceOf(OnStreamableMapObjectStreamOutHandler::class.java)
                    .isSameAs(injector.getInstance<OnStreamableMapObjectStreamOutHandler>())
        }

        @Test
        fun shouldInjectCallbackListenerRegistries() {
            val barService = injector.getInstance<BarService>()

            val streamerClasses = barService.callbackListenerRegistries.map { it::class }
            assertThat(streamerClasses)
                    .containsExactlyInAnyOrder(
                            OnStreamableMapObjectMovedHandler::class,
                            OnPlayerEditStreamableMapObjectHandler::class,
                            OnPlayerSelectStreamableMapObjectHandler::class,
                            OnStreamableMapObjectStreamInHandler::class,
                            OnStreamableMapObjectStreamOutHandler::class
                    )
        }

        @Test
        fun shouldInjectDistanceBasedPlayerStreamerFactory() {
            val distanceBasedPlayerStreamerFactory = injector.getInstance<DistanceBasedPlayerStreamerFactory>()

            assertThat(distanceBasedPlayerStreamerFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectCoordinatesBasedPlayerStreamerFactory() {
            val coordinatesBasedPlayerStreamerFactory = injector.getInstance<CoordinatesBasedPlayerStreamerFactory>()

            assertThat(coordinatesBasedPlayerStreamerFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectMapObjectStreamerAsSingleton() {
            val mapObjectStreamer = injector.getInstance<MapObjectStreamer>()

            assertThat(mapObjectStreamer)
                    .isNotNull
                    .isInstanceOf(MapObjectStreamer::class.java)
                    .isSameAs(injector.getInstance<MapObjectStreamer>())
        }

        @Test
        fun shouldInjectTextLabelStreamerAsSingleton() {
            val textLabelStreamer = injector.getInstance<TextLabelStreamer>()

            assertThat(textLabelStreamer)
                    .isNotNull
                    .isInstanceOf(TextLabelStreamer::class.java)
                    .isSameAs(injector.getInstance<TextLabelStreamer>())
        }

        @Test
        fun shouldInjectStreamerExecutorAsSingleton() {
            val streamerExecutor = injector.getInstance<StreamerExecutor>()

            assertThat(streamerExecutor)
                    .isNotNull
                    .isInstanceOf(StreamerExecutor::class.java)
                    .isSameAs(injector.getInstance<StreamerExecutor>())
        }

        @Test
        fun shouldInjectStreamableMapObjectService() {
            val streamableMapObjectService = injector.getInstance<StreamableMapObjectService>()

            assertThat(streamableMapObjectService)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableMapObjectFactory() {
            val streamableMapObjectFactory = injector.getInstance<StreamableMapObjectFactory>()

            assertThat(streamableMapObjectFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableMapObjectStateFactory() {
            val streamableMapObjectStateFactory = injector.getInstance<StreamableMapObjectStateFactory>()

            assertThat(streamableMapObjectStateFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableTextLabelService() {
            val streamableTextLabelService = injector.getInstance<StreamableTextLabelService>()

            assertThat(streamableTextLabelService)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableTextLabelFactory() {
            val streamableTextLabelFactory = injector.getInstance<StreamableTextLabelFactory>()

            assertThat(streamableTextLabelFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableTextLabelStateFactory() {
            val streamableTextLabelStateFactory = injector.getInstance<StreamableTextLabelStateFactory>()

            assertThat(streamableTextLabelStateFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamableMapObjectStateMachineFactory() {
            val streamableMapObjectStateMachineFactory = injector.getInstance<StreamableMapObjectStateMachineFactory>()

            assertThat(streamableMapObjectStateMachineFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectTimeProvider() {
            val timeProvider = injector.getInstance<TimeProvider>()

            assertThat(timeProvider)
                    .isNotNull
        }

        @Test
        fun shouldInjectStreamers() {
            val fooService = injector.getInstance<FooService>()

            val streamerClasses = fooService.streamers.map { it::class }
            assertThat(streamerClasses)
                    .containsExactlyInAnyOrder(
                            MapObjectStreamer::class,
                            TextLabelStreamer::class
                    )
        }
    }

    private class FooService
    @Inject
    constructor(val streamers: Set<@JvmSuppressWildcards Streamer>)

    private class BarService
    @Inject
    constructor(val callbackListenerRegistries: Set<@JvmSuppressWildcards CallbackListenerRegistry<*>>)

    private object TestModule : KampModule() {

        override fun configure() {
            bind(TextProvider::class.java).toInstance(mockk())
            bind(AsyncExecutor::class.java).toInstance(mockk())
            bind(TimerExecutor::class.java).toInstance(mockk())
            bind(PlayerMapObjectService::class.java).toInstance(mockk())
            bind(PlayerTextLabelService::class.java).toInstance(mockk())
            bind(PlayerService::class.java).toInstance(mockk())
        }

    }

}