package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.streamer.AbstractStreamerModule
import ch.leadrian.samp.kamp.streamer.StreamerModule
import ch.leadrian.samp.kamp.streamer.entity.StreamLocation
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MapObjectStreamerTest {

    private lateinit var mapObjectStreamer: MapObjectStreamer

    private lateinit var asyncExecutor: AsyncExecutor
    private val playerService = mockk<PlayerService>()
    private val playerMapObjectService = mockk<PlayerMapObjectService>()
    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val textProvider = mockk<TextProvider>()
    private val timerExecutor = mockk<TimerExecutor>()
    private lateinit var injector: Injector

    @BeforeEach
    fun setUp() {
        every { callbackListenerManager.register(any()) } just Runs
        every { playerService.getMaxPlayers() } returns 50
        asyncExecutor = mockk {
            every { executeOnMainThread(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
        }
        injector = Guice.createInjector(StreamerModule(), TestModule())
        mapObjectStreamer = injector.getInstance()
    }

    @Nested
    inner class CallbackListenerTests {

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            mapObjectStreamer.initialize()

            verify { callbackListenerManager.register(mapObjectStreamer) }
        }

        @Test
        fun shouldRegisterCreatedMapObjectAsCallbackListener() {
            val streamableMapObject = mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            verify { callbackListenerManager.register(streamableMapObject) }
        }

        @Test
        fun destroyShouldUnregisterCreatedMapObjectAsCallbackListener() {
            every { callbackListenerManager.unregister(any()) } just Runs
            val streamableMapObject = mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            streamableMapObject.destroy()

            verify { callbackListenerManager.unregister(streamableMapObject) }
        }

    }

    @Nested
    inner class StreamTests {

        @Test
        fun shouldStreamInMapObjects() {
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject1 = mockk<PlayerMapObject>()
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject1
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            mapObjectStreamer.createMapObject(
                    modelId = 187,
                    priority = 0,
                    streamDistance = 50f,
                    coordinates = vector3DOf(120f, 180f, 40f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            mapObjectStreamer.createMapObject(
                    modelId = 69,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(1000f, 0f, 20f),
                    rotation = vector3DOf(7f, 8f, 9f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 187,
                        drawDistance = 50f,
                        coordinates = vector3DOf(120f, 180f, 40f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldNotStreamInDestroyedMapObject() {
            every { callbackListenerManager.unregister(any()) } just Runs
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject1 = mockk<PlayerMapObject>()
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject1
            val coordinates = vector3DOf(150f, 100f, 20f)
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = coordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            val streamableMapObject = mapObjectStreamer.createMapObject(
                    modelId = 69,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = coordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            streamableMapObject.destroy()

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))

            verify(exactly = 1) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = coordinates,
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun shouldStreamAttachedObject() {
            every { callbackListenerManager.unregister(any()) } just Runs
            val player = mockk<Player> {
                every { isConnected } returns true
                every { coordinates } returns vector3DOf(1000f, 2000f, 100f)
            }
            val playerMapObject1 = mockk<PlayerMapObject>(relaxed = true)
            val playerMapObject2 = mockk<PlayerMapObject>(relaxed = true)
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returnsMany listOf(playerMapObject1, playerMapObject2)
            val playerMapObject = mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            playerMapObject.attachTo(player, vector3DOf(1f, 2f, 3f), vector3DOf(-1f, -2f, -3f))
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(1100f, 1900f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObject1.destroy()
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = vector3DOf(1001f, 2002f, 103f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun shouldStreamMovingObject() {
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            every { callbackListenerManager.unregister(any()) } just Runs
            val player = mockk<Player> {
                every { isConnected } returns true
                every { coordinates } returns vector3DOf(1000f, 2000f, 100f)
            }
            val playerMapObject1 = mockk<PlayerMapObject>(relaxed = true)
            val playerMapObject2 = mockk<PlayerMapObject>(relaxed = true)
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returnsMany listOf(playerMapObject1, playerMapObject2)
            val playerMapObject = mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 250f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            playerMapObject.moveTo(vector3DOf(1150f, 100f, 20f), 1000f)
            Thread.sleep(500)
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            Thread.sleep(600)
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(1100f, 200f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 250f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObject1.destroy()
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 250f,
                        coordinates = vector3DOf(1150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenMapObjectWasMovedItShouldStream() {
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            every { callbackListenerManager.unregister(any()) } just Runs
            val player = mockk<Player> {
                every { isConnected } returns true
                every { coordinates } returns vector3DOf(1000f, 2000f, 100f)
            }
            val playerMapObject1 = mockk<PlayerMapObject>(relaxed = true)
            val playerMapObject2 = mockk<PlayerMapObject>(relaxed = true)
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returnsMany listOf(playerMapObject1, playerMapObject2)
            val playerMapObject = mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 250f,
                    coordinates = vector3DOf(150f, 100f, 20f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            playerMapObject.coordinates = vector3DOf(1150f, 100f, 20f)
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(1100f, 200f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 250f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObject1.destroy()
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 250f,
                        coordinates = vector3DOf(1150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenMapObjectIsOutOfRangeItShouldStreamOut() {
            every { callbackListenerManager.unregister(any()) } just Runs
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject = mockk<PlayerMapObject> {
                every { destroy() } just Runs
            }
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val coordinates = vector3DOf(150f, 100f, 20f)
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = coordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(1000f, 200f, 50f, 1, 0))))

            verify(exactly = 1) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verifyOrder {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = coordinates,
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObject.destroy()
            }
        }

        @Test
        fun shouldStreamInMapObjectsAccordingToPriority() {
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject1 = mockk<PlayerMapObject>()
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject1
            val closestCoordinates = vector3DOf(100f, 200f, 40f)
            mapObjectStreamer.createMapObject(
                    modelId = 187,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = closestCoordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            val farthestCoordinates = vector3DOf(150f, 200f, 50f)
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 1,
                    streamDistance = 300f,
                    coordinates = farthestCoordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            val middleCoordinates = vector3DOf(100f, 220f, 50f)
            mapObjectStreamer.createMapObject(
                    modelId = 69,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = middleCoordinates,
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )
            mapObjectStreamer.capacity = 2

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = farthestCoordinates,
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 187,
                        drawDistance = 300f,
                        coordinates = closestCoordinates,
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun shouldOnlyStreamInMapObjectsWithMatchingOrNoInteriorIds() {
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject1 = mockk<PlayerMapObject>()
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject1
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(1),
                    virtualWorldIds = mutableSetOf()
            )
            mapObjectStreamer.createMapObject(
                    modelId = 187,
                    priority = 0,
                    streamDistance = 50f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorIds = mutableSetOf(2),
                    virtualWorldIds = mutableSetOf()
            )
            mapObjectStreamer.createMapObject(
                    modelId = 69,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = vector3DOf(100f, 200f, 50f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 69,
                        drawDistance = 300f,
                        coordinates = vector3DOf(100f, 200f, 50f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun shouldOnlyStreamInMapObjectsWithMatchingOrNoVirtualWorldIds() {
            val player = mockk<Player> {
                every { isConnected } returns true
            }
            val playerMapObject1 = mockk<PlayerMapObject>()
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject1
            mapObjectStreamer.createMapObject(
                    modelId = 1337,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf(1)
            )
            mapObjectStreamer.createMapObject(
                    modelId = 187,
                    priority = 0,
                    streamDistance = 50f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf(2)
            )
            mapObjectStreamer.createMapObject(
                    modelId = 69,
                    priority = 0,
                    streamDistance = 300f,
                    coordinates = vector3DOf(100f, 200f, 50f),
                    rotation = vector3DOf(1f, 2f, 3f),
                    interiorIds = mutableSetOf(),
                    virtualWorldIds = mutableSetOf()
            )

            mapObjectStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 0, 1))))

            verify(exactly = 2) { playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any()) }
            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 1337,
                        drawDistance = 300f,
                        coordinates = vector3DOf(100f, 200f, 50f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = 69,
                        drawDistance = 300f,
                        coordinates = vector3DOf(100f, 200f, 50f),
                        rotation = vector3DOf(1f, 2f, 3f)
                )
            }
        }
    }

    private inner class TestModule : AbstractStreamerModule() {

        override fun configure() {
            bind(AsyncExecutor::class.java).toInstance(asyncExecutor)
            bind(TimerExecutor::class.java).toInstance(timerExecutor)
            bind(PlayerService::class.java).toInstance(playerService)
            bind(ExecutorServiceFactory::class.java).toInstance(mockk())
            bind(PlayerMapObjectService::class.java).toInstance(playerMapObjectService)
            bind(CallbackListenerManager::class.java).toInstance(callbackListenerManager)
            bind(TextProvider::class.java).toInstance(textProvider)
            binder().requireExplicitBindings()
        }

    }

}