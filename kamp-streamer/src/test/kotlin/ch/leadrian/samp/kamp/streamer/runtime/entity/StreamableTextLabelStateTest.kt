package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Locale
import java.util.concurrent.CompletableFuture

internal class StreamableTextLabelStateTest {

    private val player = mockk<Player>()
    private val streamableTextLabel = mockk<StreamableTextLabelImpl>()
    private val playerTextLabelService = mockk<PlayerTextLabelService>()
    private val asyncExecutor = mockk<AsyncExecutor>()

    @Nested
    inner class FixedCoordinatesTests {

        @Test
        fun shouldInitializeWithCoordinates() {
            val state = StreamableTextLabelState.FixedCoordinates(
                    streamableTextLabel,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCreatePlayerTextLabel(testLOS: Boolean) {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            streamableTextLabel.apply {
                every { color } returns Colors.RED
                every { streamDistance } returns 13.37f
                every { this@apply.testLOS } returns testLOS
                every { getText(Locale.GERMANY) } returns "Test"
            }
            val expectedPlayerTextLabel = mockk<PlayerTextLabel>()
            every {
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = "Test",
                        color = Colors.RED,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 13.37f,
                        testLOS = testLOS
                )
            } returns expectedPlayerTextLabel
            val state = StreamableTextLabelState.FixedCoordinates(
                    streamableTextLabel,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService
            )

            val playerTextLabel = state.createPlayerTextLabel(player)

            assertThat(playerTextLabel)
                    .isEqualTo(expectedPlayerTextLabel)
        }

    }

    @Nested
    inner class AttachedToVehicleTests {

        private val vehicle = mockk<Vehicle>()

        @Test
        fun shouldGetCoordinatesFromVehicle() {
            every { asyncExecutor.computeOnMainThread(any<() -> Vector3D>()) } answers {
                val action = firstArg<() -> Vector3D>()
                CompletableFuture.completedFuture(action())
            }
            every { vehicle.coordinates } returns vector3DOf(4f, 5f, 6f)
            val state = StreamableTextLabelState.AttachedToVehicle(
                    streamableTextLabel,
                    vehicle,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService,
                    asyncExecutor
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(4f, 5f, 6f))
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCreatePlayerTextLabel(testLOS: Boolean) {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            streamableTextLabel.apply {
                every { color } returns Colors.RED
                every { streamDistance } returns 13.37f
                every { this@apply.testLOS } returns testLOS
                every { getText(Locale.GERMANY) } returns "Test"
            }
            val expectedPlayerTextLabel = mockk<PlayerTextLabel>()
            every {
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = "Test",
                        color = Colors.RED,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 13.37f,
                        testLOS = testLOS,
                        attachedToVehicle = vehicle
                )
            } returns expectedPlayerTextLabel
            val state = StreamableTextLabelState.AttachedToVehicle(
                    streamableTextLabel,
                    vehicle,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService,
                    asyncExecutor
            )

            val playerTextLabel = state.createPlayerTextLabel(player)

            assertThat(playerTextLabel)
                    .isEqualTo(expectedPlayerTextLabel)
        }

    }

    @Nested
    inner class AttachedToPlayerTests {

        private val player = mockk<Player>()

        @Test
        fun shouldGetCoordinatesFromPlayer() {
            every { asyncExecutor.computeOnMainThread(any<() -> Vector3D>()) } answers {
                val action = firstArg<() -> Vector3D>()
                CompletableFuture.completedFuture(action())
            }
            every { player.coordinates } returns vector3DOf(4f, 5f, 6f)
            val state = StreamableTextLabelState.AttachedToPlayer(
                    streamableTextLabel,
                    player,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService,
                    asyncExecutor
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(4f, 5f, 6f))
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCreatePlayerTextLabel(testLOS: Boolean) {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            streamableTextLabel.apply {
                every { color } returns Colors.RED
                every { streamDistance } returns 13.37f
                every { this@apply.testLOS } returns testLOS
                every { getText(Locale.GERMANY) } returns "Test"
            }
            val expectedPlayerTextLabel = mockk<PlayerTextLabel>()
            every {
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = "Test",
                        color = Colors.RED,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 13.37f,
                        testLOS = testLOS,
                        attachedToPlayer = player
                )
            } returns expectedPlayerTextLabel
            val state = StreamableTextLabelState.AttachedToPlayer(
                    streamableTextLabel,
                    player,
                    mutableVector3DOf(1f, 2f, 3f),
                    playerTextLabelService,
                    asyncExecutor
            )

            val playerTextLabel = state.createPlayerTextLabel(player)

            assertThat(playerTextLabel)
                    .isEqualTo(expectedPlayerTextLabel)
        }

    }

}