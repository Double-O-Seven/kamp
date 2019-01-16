package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableColorOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelStateFactory
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.data.Percentage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.Locale

internal class StreamableTextLabelImplTest {

    private lateinit var streamableTextLabel: StreamableTextLabelImpl
    private val textProvider: TextProvider = mockk()
    private val textLabelStreamer: TextLabelStreamer = mockk()
    private val streamableTextLabelStateFactory: StreamableTextLabelStateFactory = mockk()
    private val onStreamableTextLabelStreamInHandler: OnStreamableTextLabelStreamInHandler = mockk()
    private val onStreamableTextLabelStreamOutHandler: OnStreamableTextLabelStreamOutHandler = mockk()
    private val onStreamableTextLabelStreamInReceiver: OnStreamableTextLabelStreamInReceiverDelegate = mockk()
    private val onStreamableTextLabelStreamOutReceiver: OnStreamableTextLabelStreamOutReceiverDelegate = mockk()
    private val initialState: StreamableTextLabelState.FixedCoordinates = mockk()

    @BeforeEach
    fun setUp() {
        every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns initialState
        streamableTextLabel = StreamableTextLabelImpl(
                coordinates = vector3DOf(1f, 2f, 3f),
                color = Colors.RED,
                textSupplier = { "Test" },
                streamDistance = 10f,
                priority = 69,
                interiorIds = mutableSetOf(1, 2, 3),
                virtualWorldIds = mutableSetOf(4, 5),
                testLOS = true,
                textProvider = textProvider,
                textLabelStreamer = textLabelStreamer,
                streamableTextLabelStateFactory = streamableTextLabelStateFactory,
                onStreamableTextLabelStreamInHandler = onStreamableTextLabelStreamInHandler,
                onStreamableTextLabelStreamOutHandler = onStreamableTextLabelStreamOutHandler,
                onStreamableTextLabelStreamInReceiver = onStreamableTextLabelStreamInReceiver,
                onStreamableTextLabelStreamOutReceiver = onStreamableTextLabelStreamOutReceiver
        )
    }

    @Test
    fun shouldInitializeWithStreamDistance() {
        val streamDistance = streamableTextLabel.streamDistance

        assertThat(streamDistance)
                .isEqualTo(10f)
    }

    @Test
    fun shouldSetDrawDistanceToStreamDistance() {
        val drawDistance = streamableTextLabel.drawDistance

        assertThat(drawDistance)
                .isEqualTo(10f)
    }

    @Test
    fun shouldInitializeWithInteriorIds() {
        val interiorIds = streamableTextLabel.interiorIds

        assertThat(interiorIds)
                .containsExactlyInAnyOrder(1, 2, 3)
    }

    @Test
    fun shouldInitializeWithVirtualWorldIds() {
        val virtualWorldIds = streamableTextLabel.virtualWorldIds

        assertThat(virtualWorldIds)
                .containsExactlyInAnyOrder(4, 5)
    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldInitializeWithColor() {
            val color = streamableTextLabel.color

            assertThat(color)
                    .isEqualTo(Colors.RED)
        }

        @Test
        fun shouldSetColor() {
            streamableTextLabel.color = mutableColorOf(0x112233FF)

            val color = streamableTextLabel.color

            assertThat(color)
                    .isEqualTo(colorOf(0x112233FF))
        }

        @Test
        fun givenItIsStreamedInItShouldUpdatePlayerTextDraws() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            val forPlayer = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            val playerTextLabel = mockk<PlayerTextLabel> {
                every { update(any(), any()) } just Runs
            }
            every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
            streamableTextLabel.onStreamIn(forPlayer)

            streamableTextLabel.color = Colors.BLUE

            verify { playerTextLabel.update("Test", Colors.BLUE) }
        }
    }

    @Nested
    inner class CoordinatesTests {

        private val fixedCoordinates = mockk<StreamableTextLabelState.FixedCoordinates>()

        @BeforeEach
        fun setUp() {
            every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns fixedCoordinates
            every { textLabelStreamer.onBoundingBoxChange(any()) } just Runs
            every { textLabelStreamer.onStateChange(any()) } just Runs
        }

        @Test
        fun stateShouldReturnCoordinates() {
            every { initialState.coordinates } returns vector3DOf(11f, 22f, 33f)

            val coordinates = streamableTextLabel.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun setCoordinatesShouldTransitionStateToFixedCoordinatesAndCallStreamer() {
            streamableTextLabel.coordinates = vector3DOf(11f, 22f, 33f)

            verify {
                streamableTextLabelStateFactory.createFixedCoordinates(streamableTextLabel, vector3DOf(11f, 22f, 33f))
                textLabelStreamer.onStateChange(streamableTextLabel)
                textLabelStreamer.onBoundingBoxChange(streamableTextLabel)
            }
        }

        @Test
        fun shouldReturnUpdatedCoordinates() {
            every { fixedCoordinates.coordinates } returns vector3DOf(11f, 22f, 33f)

            streamableTextLabel.coordinates = vector3DOf(11f, 22f, 33f)

            assertThat(streamableTextLabel.coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun givenCoordinatesWereChangedIsAttachedShouldBeFalse() {
            streamableTextLabel.coordinates = vector3DOf(11f, 22f, 33f)

            val isAttached = streamableTextLabel.isAttached

            assertThat(isAttached)
                    .isFalse()
        }

    }

    @Nested
    inner class TextTests {

        @BeforeEach
        fun setUp() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
        }

        @Test
        fun shouldInitializeWithText() {
            val text = streamableTextLabel.text

            assertThat(text)
                    .isEqualTo("Test")
        }

        @Nested
        inner class PropertySetterTests {

            @Test
            fun shouldSetText() {
                streamableTextLabel.text = "Test 1234"

                assertThat(streamableTextLabel.text)
                        .isEqualTo("Test 1234")
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.text = "Test 1234"

                verify { playerTextLabel.update("Test 1234", Colors.RED) }
            }
        }

        @Nested
        inner class TextSupplierTests {

            @Test
            fun shouldSetText() {
                streamableTextLabel.text { locale -> "Locale is $locale" }

                assertThat(streamableTextLabel.getText(Locale.GERMANY))
                        .isEqualTo("Locale is de_DE")
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.text { locale -> "Locale is $locale" }

                verify { playerTextLabel.update("Locale is de_DE", Colors.RED) }
            }
        }

        @Nested
        inner class TextKeyTests {

            @Test
            fun shouldSetText() {
                val textKey = TextKey("test.foo")
                val locale = Locale.GERMANY
                every { textProvider.getText(locale, textKey) } returns "Test 1234"

                streamableTextLabel.setText(textKey)

                assertThat(streamableTextLabel.getText(locale))
                        .isEqualTo("Test 1234")
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                val textKey = TextKey("test.foo")
                every { textProvider.getText(Locale.GERMANY, textKey) } returns "Test 1234"
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.setText(textKey)

                verify { playerTextLabel.update("Test 1234", Colors.RED) }
            }
        }
    }

    @Nested
    inner class UpdateTests {

        @BeforeEach
        fun setUp() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
        }

        @Nested
        inner class StringLiteralTests {

            @Test
            fun shouldSetText() {
                streamableTextLabel.update("Test 1234", Colors.BLUE)

                assertAll(
                        { assertThat(streamableTextLabel.text).isEqualTo("Test 1234") },
                        { assertThat(streamableTextLabel.color).isEqualTo(Colors.BLUE) }
                )
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.update("Test 1234", Colors.BLUE)

                verify { playerTextLabel.update("Test 1234", Colors.BLUE) }
            }
        }

        @Nested
        inner class TextSupplierTests {

            @Test
            fun shouldSetText() {
                streamableTextLabel.update(Colors.BLUE) { locale -> "Locale is $locale" }

                assertAll(
                        { assertThat(streamableTextLabel.getText(Locale.GERMANY)).isEqualTo("Locale is de_DE") },
                        { assertThat(streamableTextLabel.color).isEqualTo(Colors.BLUE) }
                )
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.update(Colors.BLUE) { locale -> "Locale is $locale" }

                verify { playerTextLabel.update("Locale is de_DE", Colors.BLUE) }
            }
        }

        @Nested
        inner class TextKeyTests {

            @Test
            fun shouldSetTextWithTextKey() {
                val textKey = TextKey("test.foo")
                val locale = Locale.GERMANY
                every { textProvider.getText(locale, textKey) } returns "Test 1234"

                streamableTextLabel.update(textKey, Colors.BLUE)

                assertAll(
                        { assertThat(streamableTextLabel.getText(locale)).isEqualTo("Test 1234") },
                        { assertThat(streamableTextLabel.color).isEqualTo(Colors.BLUE) }
                )
            }

            @Test
            fun shouldUpdateStreamedInPlayerTextLabels() {
                val forPlayer = mockk<Player> {
                    every { locale } returns Locale.GERMANY
                }
                val playerTextLabel = mockk<PlayerTextLabel> {
                    every { update(any(), any()) } just Runs
                }
                every { initialState.createPlayerTextLabel(forPlayer) } returns playerTextLabel
                val textKey = TextKey("test.foo")
                every { textProvider.getText(Locale.GERMANY, textKey) } returns "Test 1234"
                streamableTextLabel.onStreamIn(forPlayer)

                streamableTextLabel.update(textKey, Colors.BLUE)

                verify { playerTextLabel.update("Test 1234", Colors.BLUE) }
            }
        }
    }

    @Nested
    inner class AttachToVehicleTests {

        private val vehicle: Vehicle = mockk()
        private val attachedToVehicle = mockk<StreamableTextLabelState.AttachedToVehicle>()

        @BeforeEach
        fun setUp() {
            every {
                streamableTextLabelStateFactory.createAttachedToVehicle(any(), any(), any())
            } returns attachedToVehicle
            every { vehicle.addOnDestroyListener(any()) } just Runs
            every { textLabelStreamer.onStateChange(any()) } just Runs
        }

        @Test
        fun shouldTransitionToAttachedToVehicleState() {
            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            verify {
                streamableTextLabelStateFactory.createAttachedToVehicle(
                        streamableTextLabel,
                        vector3DOf(1f, 2f, 3f),
                        vehicle
                )
            }
        }

        @Test
        fun shouldCallTextLabelStreamer() {
            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            verify { textLabelStreamer.onStateChange(streamableTextLabel) }
        }

        @Test
        fun shouldAddStreamableTextLabelAsOnDestroyListener() {
            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            verify { vehicle.addOnDestroyListener(streamableTextLabel) }
        }

        @Test
        fun shouldReplacePlayerTextLabel() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            val forPlayer = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            val playerTextLabel1 = mockk<PlayerTextLabel> {
                every { destroy() } just Runs
            }
            val playerTextLabel2 = mockk<PlayerTextLabel>()
            every {
                initialState.createPlayerTextLabel(forPlayer)
            } returns playerTextLabel1
            every { attachedToVehicle.createPlayerTextLabel(any()) } returns playerTextLabel2
            every { attachedToVehicle.coordinates } returns vector3DOf(123f, 456f, 789f)
            streamableTextLabel.onStreamIn(forPlayer)

            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            verify {
                playerTextLabel1.destroy()
                attachedToVehicle.createPlayerTextLabel(forPlayer)
            }
            assertThat(streamableTextLabel.coordinates)
                    .isEqualTo(vector3DOf(123f, 456f, 789f))
        }

        @Test
        fun givenAttachedToWasCalledIsAttachedShouldBeTrue() {
            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            val isAttached = streamableTextLabel.isAttached

            assertThat(isAttached)
                    .isTrue()
        }

        @Nested
        inner class GivenItIsAttachedToVehicle {

            @BeforeEach
            fun setUp() {
                streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))
                every { attachedToVehicle.vehicle } returns vehicle
                every { vehicle.removeOnDestroyListener(any()) } just Runs
            }

            @Test
            fun onDestroyWithVehicleShouldTransitionToFixedCoordinates() {
                every { attachedToVehicle.coordinates } returns vector3DOf(11f, 22f, 33f)
                every { textLabelStreamer.onBoundingBoxChange(any()) } just Runs
                every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns mockk()

                streamableTextLabel.onDestroy(vehicle)

                verify {
                    streamableTextLabelStateFactory.createFixedCoordinates(
                            streamableTextLabel,
                            vector3DOf(11f, 22f, 33f)
                    )
                }
            }

            @Test
            fun destroyForStreamableTextLabelShouldRemoveOnDestroyListener() {
                streamableTextLabel.destroy()

                verify { vehicle.removeOnDestroyListener(streamableTextLabel) }
            }

            @Test
            fun onDestroyWithOtherVehicleShouldDoNothing() {
                clearMocks(streamableTextLabelStateFactory)
                every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns mockk()

                streamableTextLabel.onDestroy(mockk<Vehicle>())

                verify(exactly = 0) {
                    streamableTextLabelStateFactory.createFixedCoordinates(any(), any())
                }
            }

            @Test
            fun setCoordinatesShouldRemoveStreamableTextLabelAsOnDestroyListener() {
                every { textLabelStreamer.onBoundingBoxChange(any()) } just Runs
                every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns mockk()

                streamableTextLabel.coordinates = vector3DOf(1f, 2f, 3f)

                verify { vehicle.removeOnDestroyListener(streamableTextLabel) }
            }

            @Test
            fun attachToPlayerShouldRemoveStreamableTextLabelAsOnDestroyListener() {
                every { streamableTextLabelStateFactory.createAttachedToPlayer(any(), any(), any()) } returns mockk()

                streamableTextLabel.attachTo(mockk<Player>(), vector3DOf(1f, 2f, 3f))

                verify { vehicle.removeOnDestroyListener(streamableTextLabel) }
            }

            @Test
            fun attachToAnotherVehicleShouldRemoveStreamableTextLabelAsOnDestroyListener() {
                val otherVehicle = mockk<Vehicle> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every { streamableTextLabelStateFactory.createAttachedToVehicle(any(), any(), any()) } returns mockk()

                streamableTextLabel.attachTo(otherVehicle, vector3DOf(1f, 2f, 3f))

                verify { vehicle.removeOnDestroyListener(streamableTextLabel) }
            }

        }
    }

    @Nested
    inner class AttachToPlayerTests {

        private val attachPlayer: Player = mockk()
        private val attachedToPlayer = mockk<StreamableTextLabelState.AttachedToPlayer>()

        @BeforeEach
        fun setUp() {
            every {
                streamableTextLabelStateFactory.createAttachedToPlayer(any(), any(), any())
            } returns attachedToPlayer
            every { textLabelStreamer.onStateChange(any()) } just Runs
        }

        @Test
        fun shouldTransitionToAttachedToPlayerState() {
            streamableTextLabel.attachTo(attachPlayer, vector3DOf(1f, 2f, 3f))

            verify {
                streamableTextLabelStateFactory.createAttachedToPlayer(
                        streamableTextLabel,
                        vector3DOf(1f, 2f, 3f),
                        attachPlayer
                )
            }
        }

        @Test
        fun shouldCallTextLabelStreamer() {
            streamableTextLabel.attachTo(attachPlayer, vector3DOf(1f, 2f, 3f))

            verify { textLabelStreamer.onStateChange(streamableTextLabel) }
        }

        @Test
        fun shouldReplacePlayerTextLabel() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            val forPlayer = mockk<Player> {
                every { locale } returns Locale.GERMANY
            }
            val playerTextLabel1 = mockk<PlayerTextLabel> {
                every { destroy() } just Runs
            }
            val playerTextLabel2 = mockk<PlayerTextLabel>()
            every {
                initialState.createPlayerTextLabel(forPlayer)
            } returns playerTextLabel1
            every { attachedToPlayer.createPlayerTextLabel(any()) } returns playerTextLabel2
            every { attachedToPlayer.coordinates } returns vector3DOf(123f, 456f, 789f)
            streamableTextLabel.onStreamIn(forPlayer)

            streamableTextLabel.attachTo(attachPlayer, vector3DOf(1f, 2f, 3f))

            verify {
                playerTextLabel1.destroy()
                attachedToPlayer.createPlayerTextLabel(forPlayer)
            }
            assertThat(streamableTextLabel.coordinates)
                    .isEqualTo(vector3DOf(123f, 456f, 789f))
        }

        @Test
        fun givenAttachedToWasCalledIsAttachedShouldBeTrue() {
            streamableTextLabel.attachTo(attachPlayer, vector3DOf(1f, 2f, 3f))

            val isAttached = streamableTextLabel.isAttached

            assertThat(isAttached)
                    .isTrue()
        }

        @Nested
        inner class OnPlayerDisconnectTests {

            private val player: Player = mockk()

            @BeforeEach
            fun setUp() {
                streamableTextLabel.attachTo(attachPlayer, vector3DOf(1f, 2f, 3f))
                every { attachedToPlayer.player } returns player
            }

            @Test
            fun givenPlayerIsDisconnectedItShouldTransitionToFixedCoordinates() {
                every { attachedToPlayer.coordinates } returns vector3DOf(11f, 22f, 33f)
                every { textLabelStreamer.onBoundingBoxChange(any()) } just Runs
                every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns mockk()

                streamableTextLabel.onPlayerDisconnect(player, DisconnectReason.QUIT)

                verify {
                    streamableTextLabelStateFactory.createFixedCoordinates(
                            streamableTextLabel,
                            vector3DOf(11f, 22f, 33f)
                    )
                }
            }

            @Test
            fun givenOtherPlayerIsDisconnectedItShouldTransitionToFixedCoordinates() {
                val otherPlayer: Player = mockk()
                clearMocks(streamableTextLabelStateFactory)
                every { streamableTextLabelStateFactory.createFixedCoordinates(any(), any()) } returns mockk()

                streamableTextLabel.onPlayerDisconnect(otherPlayer, DisconnectReason.QUIT)

                verify(exactly = 0) {
                    streamableTextLabelStateFactory.createFixedCoordinates(any(), any())
                }
            }

        }

    }

    @Nested
    inner class DistanceToTests {

        @Test
        fun givenNoInteriorIdsAndNoVirtualWorldIdsItShouldReturnDistance() {
            streamableTextLabel.interiorIds = mutableSetOf()
            streamableTextLabel.virtualWorldIds = mutableSetOf()
            every { initialState.coordinates } returns vector3DOf(10f, 2f, 3f)
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)

            val distance = streamableTextLabel.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenMatchingVirtualWorldIdsAndNoInteriorIdsItShouldReturnDistance() {
            streamableTextLabel.interiorIds = mutableSetOf()
            streamableTextLabel.virtualWorldIds = mutableSetOf(45)
            every { initialState.coordinates } returns vector3DOf(10f, 2f, 3f)
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)

            val distance = streamableTextLabel.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenMatchingInteriorIdsAndNoVirtualWorldIdsItShouldReturnDistance() {
            streamableTextLabel.interiorIds = mutableSetOf(1)
            streamableTextLabel.virtualWorldIds = mutableSetOf()
            every { initialState.coordinates } returns vector3DOf(10f, 2f, 3f)
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)

            val distance = streamableTextLabel.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenTextLabelIsNotInInteriorItShouldReturnFloatMax() {
            streamableTextLabel.virtualWorldIds = mutableSetOf()
            streamableTextLabel.interiorIds = mutableSetOf(0)

            val distance = streamableTextLabel.distanceTo(locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45))

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenTextLabelIsNotInVirtualWorldItShouldReturnFloatMax() {
            streamableTextLabel.virtualWorldIds = mutableSetOf(0)
            streamableTextLabel.interiorIds = mutableSetOf()

            val distance = streamableTextLabel.distanceTo(locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45))

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }
    }

    @Nested
    inner class OnStreamInTests {

        private val player: Player = mockk()
        private val playerTextLabel: PlayerTextLabel = mockk()

        @BeforeEach
        fun setUp() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { initialState.createPlayerTextLabel(any()) } returns playerTextLabel
        }

        @Test
        fun shouldInitiallyNotBeStreamedIn() {
            val isStreamedIn = streamableTextLabel.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun onStreamInShouldCallCallbackHandlerAndReceiver() {
            streamableTextLabel.onStreamIn(player)

            verify {
                onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(streamableTextLabel, player)
                onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(streamableTextLabel, player)
            }
        }

        @Test
        fun givenOnStreamInWasCalledIsStreamedInShouldBeTrue() {
            streamableTextLabel.onStreamIn(player)

            val isStreamedIn = streamableTextLabel.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun givenItIsAlreadyStreamedInOnStreamInShouldThrowAnException() {
            streamableTextLabel.onStreamIn(player)

            val caughtThrowable = catchThrowable { streamableTextLabel.onStreamIn(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable text label is already streamed in")
        }

        @Test
        fun shouldCreatePlayerTextLabel() {
            streamableTextLabel.onStreamIn(player)

            verify { initialState.createPlayerTextLabel(player) }
        }
    }

    @Nested
    inner class OnStreamOutTests {

        private val player: Player = mockk()

        @Test
        fun givenItIsNotStreamedInItShouldThrowAnException() {
            val caughtThrowable = catchThrowable { streamableTextLabel.onStreamOut(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable text label was not streamed in")
        }

        @Nested
        inner class GivenItIsStreamedIn {

            private val playerTextLabel: PlayerTextLabel = mockk()

            @BeforeEach
            fun setUp() {
                every { playerTextLabel.destroy() } just Runs
                every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
                every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
                every { onStreamableTextLabelStreamOutHandler.onStreamableTextLabelStreamOut(any(), any()) } just Runs
                every { onStreamableTextLabelStreamOutReceiver.onStreamableTextLabelStreamOut(any(), any()) } just Runs
                every { initialState.createPlayerTextLabel(any()) } returns playerTextLabel
                streamableTextLabel.onStreamIn(player)
            }

            @Test
            fun itShouldCallCallbackHandlerAndReceiver() {
                streamableTextLabel.onStreamOut(player)

                verify {
                    onStreamableTextLabelStreamOutHandler.onStreamableTextLabelStreamOut(streamableTextLabel, player)
                    onStreamableTextLabelStreamOutReceiver.onStreamableTextLabelStreamOut(streamableTextLabel, player)
                }
            }

            @Test
            fun itShouldDestroyPlayerTextLabel() {
                streamableTextLabel.onStreamOut(player)

                verify { playerTextLabel.destroy() }
            }

            @Test
            fun isStreamedInShouldReturnFalse() {
                streamableTextLabel.onStreamOut(player)

                val isStreamedIn = streamableTextLabel.isStreamedIn(player)

                assertThat(isStreamedIn)
                        .isFalse()
            }

        }

    }

    @Test
    fun shouldReturnBoundingBox() {
        every { initialState.coordinates } returns vector3DOf(1f, 2f, 3f)

        val boundingBox = streamableTextLabel.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(-9.0, -8.0, -7.0, 11.0, 12.0, 13.0))
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun givenItIsAttachedToVehicleAndStreamedInItShouldRemoveOnDestroyListener() {
            val vehicle: Vehicle = mockk {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            val attachedToVehicle: StreamableTextLabelState.AttachedToVehicle = mockk {
                every { this@mockk.vehicle } returns vehicle
            }
            every {
                streamableTextLabelStateFactory.createAttachedToVehicle(any(), any(), any())
            } returns attachedToVehicle
            every { textLabelStreamer.onStateChange(any()) } just Runs
            streamableTextLabel.attachTo(vehicle, vector3DOf(1f, 2f, 3f))

            streamableTextLabel.destroy()

            verify { vehicle.removeOnDestroyListener(streamableTextLabel) }
        }

        @Test
        fun shouldDestroyStreamedInVehiclePlayerTextLabels() {
            every { onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            every { onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(any(), any()) } just Runs
            val player: Player = mockk()
            val playerTextLabel: PlayerTextLabel = mockk {
                every { destroy() } just Runs
            }
            every { initialState.createPlayerTextLabel(any()) } returns playerTextLabel
            streamableTextLabel.onStreamIn(player)

            streamableTextLabel.destroy()

            assertAll(
                    { assertThat(streamableTextLabel.isStreamedIn(player)).isFalse() },
                    { verify { playerTextLabel.destroy() } }
            )
        }
    }

    @Nested
    inner class VisibilityTests {

        @Test
        fun isVisibleShouldReturnTrueByDefault() {
            val player = mockk<Player>()

            val isVisible = streamableTextLabel.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenTrueConditionIsVisibleShouldReturnTrue() {
            val player = mockk<Player>()
            streamableTextLabel.visibleWhen { p -> p == player }

            val isVisible = streamableTextLabel.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenFalseConditionIsVisibleShouldReturnFalse() {
            val player = mockk<Player>()
            streamableTextLabel.visibleWhen { p -> p != player }

            val isVisible = streamableTextLabel.isVisible(player)

            assertThat(isVisible)
                    .isFalse()
        }

    }
}