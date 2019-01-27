package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CameraMode
import ch.leadrian.samp.kamp.core.api.constants.CameraType
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerCameraTest {

    private lateinit var playerCamera: PlayerCamera

    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val actorRegistry = mockk<ActorRegistry>()

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerCamera = PlayerCamera(
                player,
                nativeFunctionExecutor,
                mapObjectRegistry,
                vehicleRegistry,
                playerRegistry,
                actorRegistry
        )
    }

    @Test
    fun shouldSetLookAt() {
        every { nativeFunctionExecutor.setPlayerCameraLookAt(any(), any(), any(), any(), any()) } returns true

        playerCamera.lookAt(vector3DOf(x = 1f, y = 2f, z = 3f), CameraType.CUT)

        verify {
            nativeFunctionExecutor.setPlayerCameraLookAt(
                    playerid = playerId.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    cut = CameraType.CUT.value
            )
        }
    }

    @Test
    fun shouldSetCameraBehind() {
        every { nativeFunctionExecutor.setCameraBehindPlayer(any()) } returns true

        playerCamera.setBehind()

        verify { nativeFunctionExecutor.setCameraBehindPlayer(playerId.value) }
    }

    @Test
    fun shouldGetCameraMode() {
        every { nativeFunctionExecutor.getPlayerCameraMode(playerId.value) } returns CameraMode.TRAIN_OR_TRAM.value

        val mode = playerCamera.mode

        assertThat(mode)
                .isEqualTo(CameraMode.TRAIN_OR_TRAM)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldEnableCameraTarget(enabled: Boolean) {
        every { nativeFunctionExecutor.enablePlayerCameraTarget(any(), any()) } returns true

        playerCamera.isTargetEnabled = enabled

        assertThat(playerCamera.isTargetEnabled)
                .isEqualTo(enabled)
        verify { nativeFunctionExecutor.enablePlayerCameraTarget(playerId.value, enabled) }
    }

    @Nested
    inner class TargetMapObjectTests {

        @Test
        fun shouldReturnTargetMapObject() {
            val mapObjectId = MapObjectId.valueOf(13)
            val mapObject = mockk<MapObject>()
            every { nativeFunctionExecutor.getPlayerCameraTargetObject(playerId.value) } returns mapObjectId.value
            every { mapObjectRegistry[mapObjectId.value] } returns mapObject

            val targetMapObject = playerCamera.targetMapObject

            assertThat(targetMapObject)
                    .isSameAs(mapObject)
        }

        @Test
        fun givenNoTargetMapObjectItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetObject(playerId.value) } returns SAMPConstants.INVALID_OBJECT_ID
            every { mapObjectRegistry[SAMPConstants.INVALID_OBJECT_ID] } returns null

            val targetMapObject = playerCamera.targetMapObject

            assertThat(targetMapObject)
                    .isNull()
        }
    }

    @Nested
    inner class TargetPlayerTests {

        private val otherPlayerId = PlayerId.valueOf(69)
        private val otherPlayer = mockk<Player>()

        @Test
        fun setUp() {
            every { otherPlayer.id } returns otherPlayerId
        }

        @Test
        fun shouldReturnTargetPlayer() {
            every { nativeFunctionExecutor.getPlayerCameraTargetPlayer(playerId.value) } returns otherPlayerId.value
            every { playerRegistry[otherPlayerId.value] } returns otherPlayer

            val targetPlayer = playerCamera.targetPlayer

            assertThat(targetPlayer)
                    .isSameAs(otherPlayer)
        }

        @Test
        fun givenNoTargetPlayerItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetPlayer(playerId.value) } returns SAMPConstants.INVALID_PLAYER_ID
            every { playerRegistry[SAMPConstants.INVALID_PLAYER_ID] } returns null

            val targetPlayer = playerCamera.targetPlayer

            assertThat(targetPlayer)
                    .isNull()
        }
    }

    @Nested
    inner class TargetVehicleTests {

        @Test
        fun shouldReturnTargetPlayer() {
            val vehicleId = VehicleId.valueOf(20)
            val vehicle = mockk<Vehicle>()
            every { nativeFunctionExecutor.getPlayerCameraTargetVehicle(playerId.value) } returns vehicleId.value
            every { vehicleRegistry[vehicleId.value] } returns vehicle

            val targetVehicle = playerCamera.targetVehicle

            assertThat(targetVehicle)
                    .isSameAs(vehicle)
        }

        @Test
        fun givenNoTargetPlayerItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetVehicle(playerId.value) } returns SAMPConstants.INVALID_VEHICLE_ID
            every { vehicleRegistry[SAMPConstants.INVALID_VEHICLE_ID] } returns null

            val targetVehicle = playerCamera.targetVehicle

            assertThat(targetVehicle)
                    .isNull()
        }
    }

    @Nested
    inner class TargetActorTests {

        @Test
        fun shouldReturnTargetActor() {
            val actorId = ActorId.valueOf(13)
            val actor = mockk<Actor>()
            every { nativeFunctionExecutor.getPlayerCameraTargetActor(playerId.value) } returns actorId.value
            every { actorRegistry[actorId.value] } returns actor

            val targetActor = playerCamera.targetActor

            assertThat(targetActor)
                    .isSameAs(actor)
        }

        @Test
        fun givenNoTargetActorItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetActor(playerId.value) } returns SAMPConstants.INVALID_ACTOR_ID
            every { actorRegistry[SAMPConstants.INVALID_ACTOR_ID] } returns null

            val targetActor = playerCamera.targetActor

            assertThat(targetActor)
                    .isNull()
        }
    }

    @Test
    fun shouldGetAspectRatio() {
        every { nativeFunctionExecutor.getPlayerCameraAspectRatio(playerId.value) } returns 1.234f

        val aspectRatio = playerCamera.aspectRatio

        assertThat(aspectRatio)
                .isEqualTo(1.234f)
    }

    @Test
    fun shouldGetZoom() {
        every { nativeFunctionExecutor.getPlayerCameraZoom(playerId.value) } returns 1.234f

        val zoom = playerCamera.zoom

        assertThat(zoom)
                .isEqualTo(1.234f)
    }

    @Test
    fun shouldAttachToMapObject() {
        every { nativeFunctionExecutor.attachCameraToObject(any(), any()) } returns true
        val mapObjectId = MapObjectId.valueOf(13)
        val mapObject = mockk<MapObject> {
            every { id } returns mapObjectId
        }

        playerCamera.attachTo(mapObject)

        verify { nativeFunctionExecutor.attachCameraToObject(playerid = playerId.value, objectid = mapObjectId.value) }
    }

    @Test
    fun shouldAttachToPlayerObject() {
        every { nativeFunctionExecutor.attachCameraToPlayerObject(any(), any()) } returns true
        val playerMapObjectId = PlayerMapObjectId.valueOf(13)
        val playerMapObject = mockk<PlayerMapObject> {
            every { id } returns playerMapObjectId
        }

        playerCamera.attachTo(playerMapObject)

        verify {
            nativeFunctionExecutor.attachCameraToPlayerObject(
                    playerid = playerId.value,
                    playerobjectid = playerMapObjectId.value
            )
        }
    }

    @Test
    fun shouldInterpolateCoordinates() {
        every {
            nativeFunctionExecutor.interpolateCameraPos(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } returns true

        playerCamera.interpolateCoordinates(
                from = vector3DOf(x = 1f, y = 2f, z = 3f),
                to = vector3DOf(x = 4f, y = 5f, z = 6f),
                type = CameraType.CUT,
                time = 150
        )

        verify {
            nativeFunctionExecutor.interpolateCameraPos(
                    playerid = playerId.value,
                    FromX = 1f,
                    FromY = 2f,
                    FromZ = 3f,
                    ToX = 4f,
                    ToY = 5f,
                    ToZ = 6f,
                    cut = CameraType.CUT.value,
                    time = 150
            )
        }
    }

    @Test
    fun shouldInterpolateLookAt() {
        every {
            nativeFunctionExecutor.interpolateCameraLookAt(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } returns true

        playerCamera.interpolateLookAt(
                from = vector3DOf(x = 1f, y = 2f, z = 3f),
                to = vector3DOf(x = 4f, y = 5f, z = 6f),
                type = CameraType.CUT,
                time = 150
        )

        verify {
            nativeFunctionExecutor.interpolateCameraLookAt(
                    playerid = playerId.value,
                    FromX = 1f,
                    FromY = 2f,
                    FromZ = 3f,
                    ToX = 4f,
                    ToY = 5f,
                    ToZ = 6f,
                    cut = CameraType.CUT.value,
                    time = 150
            )
        }
    }
}