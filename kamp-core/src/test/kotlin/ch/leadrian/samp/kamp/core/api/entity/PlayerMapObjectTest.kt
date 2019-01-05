package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEditPlayerMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerSelectPlayerMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerMapObjectTest {

    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        every { player.isConnected } returns true
    }

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructPlayerMapObject() {
            val playerMapObjectId = PlayerMapObjectId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayerObject(
                            playerid = playerId.value,
                            modelid = 1337,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rX = 4f,
                            rY = 5f,
                            rZ = 6f,
                            DrawDistance = 7f
                    )
                } returns playerMapObjectId.value
            }

            val playerMapObject = PlayerMapObject(
                    player = player,
                    modelId = 1337,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    drawDistance = 7f,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(playerMapObject.id)
                    .isEqualTo(playerMapObjectId)
        }

        @Test
        fun givenCreatePlayerMapObjectReturnsInvalidPlayerMapObjectIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayerObject(
                            playerid = playerId.value,
                            modelid = 1337,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rX = 4f,
                            rY = 5f,
                            rZ = 6f,
                            DrawDistance = 7f
                    )
                } returns SAMPConstants.INVALID_OBJECT_ID
            }

            val caughtThrowable = catchThrowable {
                PlayerMapObject(
                        player = player,
                        modelId = 1337,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                        drawDistance = 7f,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val playerMapObjectId = PlayerMapObjectId.valueOf(50)
        private lateinit var playerMapObject: PlayerMapObject
        private val onPlayerObjectMovedReceiver = mockk<OnPlayerObjectMovedReceiverDelegate>()
        private val onPlayerEditPlayerMapObjectReceiver = mockk<OnPlayerEditPlayerMapObjectReceiverDelegate>()
        private val onPlayerSelectPlayerMapObjectReceiver = mockk<OnPlayerSelectPlayerMapObjectReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.createPlayerObject(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns playerMapObjectId.value
            playerMapObject = PlayerMapObject(
                    player = player,
                    modelId = 1337,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    drawDistance = 7f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    onPlayerObjectMovedReceiver = onPlayerObjectMovedReceiver,
                    onPlayerEditPlayerMapObjectReceiver = onPlayerEditPlayerMapObjectReceiver,
                    onPlayerSelectPlayerMapObjectReceiver = onPlayerSelectPlayerMapObjectReceiver
            )
        }

        @Test
        fun shouldEdit() {
            val playerId = PlayerId.valueOf(69)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every { nativeFunctionExecutor.editPlayerObject(any(), any()) } returns true

            playerMapObject.edit(player)

            verify { nativeFunctionExecutor.editPlayerObject(playerid = playerId.value, objectid = playerMapObjectId.value) }
        }

        @Test
        fun shouldAttachToPlayer() {
            every {
                nativeFunctionExecutor.attachPlayerObjectToPlayer(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true
            val otherPlayer = mockk<Player> {
                every { id } returns PlayerId.valueOf(69)
            }

            playerMapObject.attachTo(
                    player = otherPlayer,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                nativeFunctionExecutor.attachPlayerObjectToPlayer(
                        objectplayer = playerId.value,
                        objectid = playerMapObjectId.value,
                        attachplayer = 69,
                        OffsetX = 1f,
                        OffsetY = 2f,
                        OffsetZ = 3f,
                        rX = 4f,
                        rY = 5f,
                        rZ = 6f
                )
            }
        }

        @Test
        fun shouldAttachToVehicle() {
            every {
                nativeFunctionExecutor.attachPlayerObjectToVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true
            val vehicle = mockk<Vehicle> {
                every { id } returns VehicleId.valueOf(69)
            }

            playerMapObject.attachTo(
                    vehicle = vehicle,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                nativeFunctionExecutor.attachPlayerObjectToVehicle(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value,
                        vehicleid = 69,
                        fOffsetX = 1f,
                        fOffsetY = 2f,
                        fOffsetZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        RotZ = 6f
                )
            }
        }

        @Nested
        inner class CoordinatesTests {

            @Test
            fun shouldGetCoordinates() {
                every {
                    nativeFunctionExecutor.getPlayerObjectPos(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            x = any(),
                            y = any(),
                            z = any()
                    )
                } answers {
                    thirdArg<ReferenceFloat>().value = 1f
                    arg<ReferenceFloat>(3).value = 2f
                    arg<ReferenceFloat>(4).value = 3f
                    true
                }

                val coordinates = playerMapObject.coordinates

                assertThat(coordinates)
                        .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
            }

            @Test
            fun shouldSetCoordinates() {
                every { nativeFunctionExecutor.setPlayerObjectPos(any(), any(), any(), any(), any()) } returns true

                playerMapObject.coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)

                verify {
                    nativeFunctionExecutor.setPlayerObjectPos(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            x = 1f,
                            y = 2f,
                            z = 3f
                    )
                }
            }
        }

        @Nested
        inner class RotationTests {

            @Test
            fun shouldGetRotation() {
                every {
                    nativeFunctionExecutor.getPlayerObjectRot(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            rotX = any(),
                            rotY = any(),
                            rotZ = any()
                    )
                } answers {
                    thirdArg<ReferenceFloat>().value = 1f
                    arg<ReferenceFloat>(3).value = 2f
                    arg<ReferenceFloat>(4).value = 3f
                    true
                }

                val rotation = playerMapObject.rotation

                assertThat(rotation)
                        .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
            }

            @Test
            fun shouldSetRotation() {
                every { nativeFunctionExecutor.setPlayerObjectRot(any(), any(), any(), any(), any()) } returns true

                playerMapObject.rotation = vector3DOf(x = 1f, y = 2f, z = 3f)

                verify {
                    nativeFunctionExecutor.setPlayerObjectRot(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            rotX = 1f,
                            rotY = 2f,
                            rotZ = 3f
                    )
                }
            }
        }

        @Test
        fun shouldDisableCameraCollisions() {
            every { nativeFunctionExecutor.setPlayerObjectNoCameraCol(any(), any()) } returns true

            playerMapObject.disableCameraCollision()

            verify {
                nativeFunctionExecutor.setPlayerObjectNoCameraCol(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value
                )
            }
        }

        @Nested
        inner class MoveToTests {

            @Test
            fun shouldMovePlayerObject() {
                every {
                    nativeFunctionExecutor.movePlayerObject(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            RotX = 4f,
                            RotY = 5f,
                            RotZ = 6f,
                            Speed = 7f
                    )
                } returns 150

                val result = playerMapObject.moveTo(
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                        speed = 7f
                )

                assertThat(result)
                        .isEqualTo(150)
            }

            @Test
            fun givenNoRotationItShouldUseFallbackValues() {
                every {
                    nativeFunctionExecutor.movePlayerObject(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            RotX = -1000f,
                            RotY = -1000f,
                            RotZ = -1000f,
                            Speed = 7f
                    )
                } returns 150

                val result = playerMapObject.moveTo(
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        speed = 7f
                )

                assertThat(result)
                        .isEqualTo(150)
            }
        }

        @Test
        fun shouldStopPlayerObject() {
            every { nativeFunctionExecutor.stopPlayerObject(any(), any()) } returns true

            playerMapObject.stop()

            verify {
                nativeFunctionExecutor.stopPlayerObject(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value
                )
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun isMovingShouldReturnExpectedValue(expectedResult: Boolean) {
            every {
                nativeFunctionExecutor.isPlayerObjectMoving(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value
                )
            } returns expectedResult

            val result = playerMapObject.isMoving

            assertThat(result)
                    .isEqualTo(expectedResult)
        }

        @Test
        fun shouldSetMaterial() {
            every {
                nativeFunctionExecutor.setPlayerObjectMaterial(any(), any(), any(), any(), any(), any(), any())
            } returns true

            playerMapObject.setMaterial(
                    index = 187,
                    modelId = 1337,
                    color = colorOf(0x11223344),
                    textureName = "texture A",
                    txdName = "txd A"
            )

            verify {
                nativeFunctionExecutor.setPlayerObjectMaterial(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value,
                        materialindex = 187,
                        modelid = 1337,
                        materialcolor = 0x44112233,
                        texturename = "texture A",
                        txdname = "txd A"
                )
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldSetMaterialText(isBold: Boolean) {
            every {
                nativeFunctionExecutor.setPlayerObjectMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true

            playerMapObject.setMaterialText(
                    text = "hi",
                    index = 187,
                    size = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize.SIZE_256X128,
                    fontSize = 16,
                    textAlignment = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment.CENTER,
                    fontFace = "Comic Sans",
                    isBold = isBold,
                    fontColor = colorOf(0x11223344),
                    backColor = colorOf(0x55667788)
            )

            verify {
                nativeFunctionExecutor.setPlayerObjectMaterialText(
                        playerid = playerId.value,
                        objectid = playerMapObjectId.value,
                        text = "hi",
                        materialindex = 187,
                        materialsize = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize.SIZE_256X128.value,
                        fontsize = 16,
                        textalignment = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment.CENTER.value,
                        fontface = "Comic Sans",
                        bold = isBold,
                        fontcolor = 0x44112233,
                        backcolor = 0x88556677.toInt()
                )
            }
        }

        @Test
        fun shouldCallOnPlayerMapObjectMovedReceiverDelegate() {
            every { onPlayerObjectMovedReceiver.onPlayerObjectMoved(any()) } just Runs

            playerMapObject.onMoved()

            verify { onPlayerObjectMovedReceiver.onPlayerObjectMoved(playerMapObject) }
        }

        @Test
        fun shouldCallOnPlayerEditPlayerMapObjectReceiverDelegate() {
            every { onPlayerEditPlayerMapObjectReceiver.onPlayerEditPlayerMapObject(any(), any(), any(), any()) } just Runs

            playerMapObject.onEdit(
                    response = ObjectEditResponse.UPDATE,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                onPlayerEditPlayerMapObjectReceiver.onPlayerEditPlayerMapObject(
                        playerMapObject = playerMapObject,
                        response = ObjectEditResponse.UPDATE,
                        offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
                )
            }
        }

        @Test
        fun shouldCallOnPlayerSelectPlayerMapObjectReceiverDelegate() {
            every { onPlayerSelectPlayerMapObjectReceiver.onPlayerSelectPlayerMapObject(any(), any(), any()) } just Runs

            playerMapObject.onSelect(
                    modelId = 1337,
                    coordinates = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                onPlayerSelectPlayerMapObjectReceiver.onPlayerSelectPlayerMapObject(
                        playerMapObject,
                        1337,
                        vector3DOf(x = 4f, y = 5f, z = 6f)
                )
            }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.destroyPlayerObject(any(), any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = playerMapObject.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun givenPlayerIsNotConnectedIsDestroyShouldBeTrue() {
                every { player.isConnected } returns false

                val isDestroyed = playerMapObject.isDestroyed

                assertThat(isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldDestroyPlayerMapObject() {
                val onDestroy = mockk<PlayerMapObject.() -> Unit>(relaxed = true)
                playerMapObject.onDestroy(onDestroy)

                playerMapObject.destroy()

                verifyOrder {
                    onDestroy.invoke(playerMapObject)
                    nativeFunctionExecutor.destroyPlayerObject(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value
                    )
                }
                assertThat(playerMapObject.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<PlayerMapObject.() -> Unit>(relaxed = true)
                playerMapObject.onDestroy(onDestroy)

                playerMapObject.destroy()
                playerMapObject.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(playerMapObject)
                    nativeFunctionExecutor.destroyPlayerObject(
                            playerid = playerId.value,
                            objectid = playerMapObjectId.value
                    )
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                playerMapObject.destroy()

                val caughtThrowable = catchThrowable { playerMapObject.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }
}