package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CrimeReport
import ch.leadrian.samp.kamp.core.api.constants.DefaultPlayerColors
import ch.leadrian.samp.kamp.core.api.constants.ExplosionType
import ch.leadrian.samp.kamp.core.api.constants.FightingStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.constants.PlayerRecordingType
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.ShopName
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.SpecialAction
import ch.leadrian.samp.kamp.core.api.constants.SpectateType
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.Weather
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.mutableColorOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.data.spawnInfoOf
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerMapIconFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.Called
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
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.net.URL
import java.util.Locale

internal class PlayerTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private lateinit var player: Player

    private val otherPlayerId: PlayerId = PlayerId.valueOf(71)
    private val otherPlayer = mockk<Player> {
        every { id } returns otherPlayerId
    }

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val actorRegistry = mockk<ActorRegistry>()
    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val menuRegistry = mockk<MenuRegistry>()
    private val playerMapIconFactory = mockk<PlayerMapIconFactory>()
    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val onPlayerNameChangeHandler = mockk<OnPlayerNameChangeHandler>()

    @BeforeEach
    fun setUp() {
        player = Player(
                id = playerId,
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerRegistry = playerRegistry,
                actorRegistry = actorRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry,
                onPlayerNameChangeHandler = onPlayerNameChangeHandler
        )
    }

    @Nested
    inner class LocaleTests {

        @Test
        fun shouldInitializeLocaleWithSystemDefault() {
            assertThat(player.locale)
                    .isEqualTo(Locale.getDefault())
        }

        @Test
        fun shouldUpdateLocale() {
            player.locale = Locale.GERMANY

            player.locale = Locale.CHINA

            assertThat(player.locale)
                    .isEqualTo(Locale.CHINA)
        }
    }

    @Test
    fun shouldInitializeIsConnectedWithTrue() {
        assertThat(player.isConnected)
                .isTrue()
    }

    @Test
    fun shouldInitializeDialogNavigation() {
        assertThat(player.dialogNavigation.player)
                .isSameAs(player)
    }

    @Test
    fun shouldInitializeEntityExtensionContainer() {
        assertThat(player.extensions.entity)
                .isEqualTo(player)
    }

    @Test
    fun shouldInitializePlayerWeapons() {
        assertThat(player.weapons.player)
                .isSameAs(player)
    }

    @Test
    fun shouldInitializePlayerCamera() {
        assertThat(player.camera.player)
                .isSameAs(player)
    }

    @Test
    fun shouldInitializePlayerAudioStream() {
        assertThat(player.audioStream.player)
                .isSameAs(player)
    }

    @Test
    fun shouldInitializePlayerAnimation() {
        assertThat(player.animation.player)
                .isSameAs(player)
    }

    @Test
    fun shouldSpawnPlayer() {
        every { nativeFunctionExecutor.spawnPlayer(any()) } returns true

        player.spawn()

        verify { nativeFunctionExecutor.spawnPlayer(playerId.value) }
    }

    @Nested
    inner class SetSpawnInfoTests {

        @Test
        fun shouldSetSpawnInfo() {
            every {
                nativeFunctionExecutor.setSpawnInfo(
                        any(),
                        any(),
                        any(),
                        any(),
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

            player.setSpawnInfo(
                    spawnInfoOf(
                            teamId = TeamId.valueOf(69),
                            position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f),
                            skinModel = SkinModel.ARMY,
                            weapon1 = weaponDataOf(WeaponModel.TEC9, 150),
                            weapon2 = weaponDataOf(WeaponModel.AK47, 300),
                            weapon3 = weaponDataOf(WeaponModel.DESERT_EAGLE, 20)
                    )
            )

            verify {
                nativeFunctionExecutor.setSpawnInfo(
                        playerid = playerId.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        rotation = 4f,
                        skin = SkinModel.ARMY.value,
                        team = 69,
                        weapon1 = WeaponModel.TEC9.value,
                        weapon1_ammo = 150,
                        weapon2 = WeaponModel.AK47.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.DESERT_EAGLE.value,
                        weapon3_ammo = 20
                )
            }
        }

        @Test
        fun givenNoTeamItShouldSetSpawnInfo() {
            every {
                nativeFunctionExecutor.setSpawnInfo(
                        any(),
                        any(),
                        any(),
                        any(),
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

            player.setSpawnInfo(
                    spawnInfoOf(
                            position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f),
                            skinModel = SkinModel.ARMY,
                            weapon1 = weaponDataOf(WeaponModel.TEC9, 150),
                            weapon2 = weaponDataOf(WeaponModel.AK47, 300),
                            weapon3 = weaponDataOf(WeaponModel.DESERT_EAGLE, 20)
                    )
            )

            verify {
                nativeFunctionExecutor.setSpawnInfo(
                        playerid = playerId.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        rotation = 4f,
                        skin = SkinModel.ARMY.value,
                        team = SAMPConstants.NO_TEAM,
                        weapon1 = WeaponModel.TEC9.value,
                        weapon1_ammo = 150,
                        weapon2 = WeaponModel.AK47.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.DESERT_EAGLE.value,
                        weapon3_ammo = 20
                )
            }
        }
    }

    @Nested
    inner class InteriorIdTests {

        @Test
        fun shouldGetInteriorId() {
            every { nativeFunctionExecutor.getPlayerInterior(playerId.value) } returns 69

            val interiorId = player.interiorId

            assertThat(interiorId)
                    .isEqualTo(69)
        }

        @Test
        fun shouldSetInteriorId() {
            every { nativeFunctionExecutor.setPlayerInterior(any(), any()) } returns true

            player.interiorId = 69

            verify { nativeFunctionExecutor.setPlayerInterior(playerid = playerId.value, interiorid = 69) }
        }
    }

    @Nested
    inner class VirtualWorldIdTests {

        @Test
        fun shouldGetVirtualWorldId() {
            every { nativeFunctionExecutor.getPlayerVirtualWorld(playerId.value) } returns 1337

            val virtualWorldId = player.virtualWorldId

            assertThat(virtualWorldId)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldSetVirtualWorldId() {
            every { nativeFunctionExecutor.setPlayerVirtualWorld(any(), any()) } returns true

            player.virtualWorldId = 1337

            verify { nativeFunctionExecutor.setPlayerVirtualWorld(playerid = playerId.value, worldid = 1337) }
        }
    }

    @Test
    fun shouldSetCoordinatesFindZ() {
        every { nativeFunctionExecutor.setPlayerPosFindZ(any(), any(), any(), any()) } returns true

        player.setCoordinatesFindZ(vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setPlayerPosFindZ(playerid = playerId.value, x = 1f, y = 2f, z = 3f) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isStreamedInShouldReturnResult(expectedResult: Boolean) {
        every {
            nativeFunctionExecutor.isPlayerStreamedIn(
                    playerid = playerId.value,
                    forplayerid = otherPlayerId.value
            )
        } returns expectedResult

        val result = player.isStreamedIn(otherPlayer)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class TargetPlayerTests {

        @Test
        fun shouldReturnTargetPlayer() {
            every { nativeFunctionExecutor.getPlayerTargetPlayer(playerId.value) } returns otherPlayerId.value
            every { playerRegistry[otherPlayerId.value] } returns otherPlayer

            val targetPlayer = player.targetPlayer

            assertThat(targetPlayer)
                    .isSameAs(otherPlayer)
        }

        @Test
        fun givenNoTargetPlayerItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerTargetPlayer(playerId.value) } returns SAMPConstants.INVALID_PLAYER_ID
            every { playerRegistry[SAMPConstants.INVALID_PLAYER_ID] } returns null

            val targetPlayer = player.targetPlayer

            assertThat(targetPlayer)
                    .isNull()
        }
    }

    @Nested
    inner class TargetActorTests {

        @Test
        fun shouldReturnTargetActor() {
            val actorId = ActorId.valueOf(13)
            val actor = mockk<Actor>()
            every { nativeFunctionExecutor.getPlayerTargetActor(playerId.value) } returns actorId.value
            every { actorRegistry[actorId.value] } returns actor

            val targetActor = player.targetActor

            assertThat(targetActor)
                    .isSameAs(actor)
        }

        @Test
        fun givenNoTargetActorItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerTargetActor(playerId.value) } returns SAMPConstants.INVALID_ACTOR_ID
            every { actorRegistry[SAMPConstants.INVALID_ACTOR_ID] } returns null

            val targetActor = player.targetActor

            assertThat(targetActor)
                    .isNull()
        }
    }

    @Nested
    inner class TeamIdTests {

        @Test
        fun shouldGetTeamId() {
            every { nativeFunctionExecutor.getPlayerTeam(playerId.value) } returns 69

            val teamId = player.teamId

            assertThat(teamId)
                    .isEqualTo(TeamId.valueOf(69))
        }

        @Test
        fun shouldSetTeamId() {
            every { nativeFunctionExecutor.setPlayerTeam(any(), any()) } returns true

            player.teamId = TeamId.valueOf(69)

            verify { nativeFunctionExecutor.setPlayerTeam(playerid = playerId.value, teamid = 69) }
        }
    }

    @Nested
    inner class ScoreTests {

        @Test
        fun shouldGetScore() {
            every { nativeFunctionExecutor.getPlayerScore(playerId.value) } returns 69

            val score = player.score

            assertThat(score)
                    .isEqualTo(69)
        }

        @Test
        fun shouldSetScore() {
            every { nativeFunctionExecutor.setPlayerScore(any(), any()) } returns true

            player.score = 69

            verify { nativeFunctionExecutor.setPlayerScore(playerid = playerId.value, score = 69) }
        }
    }

    @Nested
    inner class DrunkLevelTests {

        @Test
        fun shouldGetDrunkLevel() {
            every { nativeFunctionExecutor.getPlayerDrunkLevel(playerId.value) } returns 69

            val drunkLevel = player.drunkLevel

            assertThat(drunkLevel)
                    .isEqualTo(69)
        }

        @Test
        fun shouldSetDrunkLevel() {
            every { nativeFunctionExecutor.setPlayerDrunkLevel(any(), any()) } returns true

            player.drunkLevel = 69

            verify { nativeFunctionExecutor.setPlayerDrunkLevel(playerid = playerId.value, level = 69) }
        }
    }

    @Nested
    inner class ColorTests {

        @Test
        fun givenColorWasNotSetItShouldReturnDefaultColor() {
            val color = player.color

            assertThat(color)
                    .isEqualTo(DefaultPlayerColors[playerId])
        }

        @Test
        fun shouldSetColor() {
            every { nativeFunctionExecutor.setPlayerColor(any(), any()) } returns true

            player.color = mutableColorOf(0x00FF00FF)

            verify { nativeFunctionExecutor.setPlayerColor(playerid = playerId.value, color = 0x00FF00FF) }
            assertThat(player.color)
                    .isEqualTo(colorOf(0x00FF00FF))
        }
    }

    @Nested
    inner class SkinModelTests {

        @Test
        fun shouldGetSkinModel() {
            every { nativeFunctionExecutor.getPlayerSkin(playerId.value) } returns SkinModel.ARMY.value

            val skinModel = player.skin

            assertThat(skinModel)
                    .isEqualTo(SkinModel.ARMY)
        }

        @Test
        fun shouldSetSkinModel() {
            every { nativeFunctionExecutor.setPlayerSkin(any(), any()) } returns true

            player.skin = SkinModel.ARMY

            verify { nativeFunctionExecutor.setPlayerSkin(playerid = playerId.value, skinid = SkinModel.ARMY.value) }
        }
    }

    @Nested
    inner class MoneyTests {

        @Test
        fun shouldGetMoney() {
            every { nativeFunctionExecutor.getPlayerMoney(playerId.value) } returns 69

            val money = player.money

            assertThat(money)
                    .isEqualTo(69)
        }

        @ParameterizedTest
        @CsvSource(
                "100, 150, 50",
                "200, 150, -50",
                "0, 0, 0"
        )
        fun shouldSetMoney(currentMoney: Int, targetMoney: Int, expectedGivenMoney: Int) {
            every { nativeFunctionExecutor.getPlayerMoney(playerId.value) } returns currentMoney
            every { nativeFunctionExecutor.givePlayerMoney(any(), any()) } returns true

            player.money = targetMoney

            verify { nativeFunctionExecutor.givePlayerMoney(playerid = playerId.value, money = expectedGivenMoney) }
        }

        @Test
        fun shouldGiveMoney() {
            every { nativeFunctionExecutor.givePlayerMoney(any(), any()) } returns true

            player.giveMoney(500)

            verify { nativeFunctionExecutor.givePlayerMoney(playerid = playerId.value, money = 500) }
        }

        @Test
        fun shouldResetMoney() {
            every { nativeFunctionExecutor.resetPlayerMoney(any()) } returns true

            player.resetMoney()

            verify { nativeFunctionExecutor.resetPlayerMoney(playerId.value) }
        }
    }

    @Nested
    inner class IpAddressTests {

        @Test
        fun shouldGetIpAddress() {
            every { nativeFunctionExecutor.getPlayerIp(playerId.value, any(), 16) } answers {
                secondArg<ReferenceString>().value = "127.0.0.1"
                true
            }

            val ipAddress = player.ipAddress

            assertThat(ipAddress)
                    .isEqualTo("127.0.0.1")
        }

        @Test
        fun givenIpAddressIsNullItShouldUseFallback() {
            every { nativeFunctionExecutor.getPlayerIp(playerId.value, any(), 16) } returns true

            val ipAddress = player.ipAddress

            assertThat(ipAddress)
                    .isEqualTo("255.255.255.255")
        }
    }

    @Nested
    inner class GpciTests {

        @Test
        fun shouldGetGpci() {
            every { nativeFunctionExecutor.gpci(playerId.value, any(), 41) } answers {
                secondArg<ReferenceString>().value = "ABC1234"
                true
            }

            val gpci = player.gpci

            assertThat(gpci)
                    .isEqualTo("ABC1234")
        }

        @Test
        fun givenGpciIsNullItShouldReturnEmptyString() {
            every { nativeFunctionExecutor.gpci(playerId.value, any(), 41) } returns true

            val gpci = player.gpci

            assertThat(gpci)
                    .isEmpty()
        }
    }

    @Test
    fun shouldGetPlayerState() {
        every { nativeFunctionExecutor.getPlayerState(playerId.value) } returns PlayerState.ON_FOOT.value

        val playerState = player.state

        assertThat(playerState)
                .isEqualTo(PlayerState.ON_FOOT)
    }

    @Test
    fun shouldGetPlayerPing() {
        every { nativeFunctionExecutor.getPlayerPing(playerId.value) } returns 31

        val ping = player.ping

        assertThat(ping)
                .isEqualTo(31)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldTogglePlayerClock(toggle: Boolean) {
        every { nativeFunctionExecutor.togglePlayerClock(any(), any()) } returns true

        player.toggleClock(toggle)

        verify { nativeFunctionExecutor.togglePlayerClock(playerid = playerId.value, toggle = toggle) }
    }

    @Nested
    inner class SetWeatherTests {

        @Test
        fun shouldSetWeatherByEnum() {
            every { nativeFunctionExecutor.setPlayerWeather(any(), any()) } returns true

            player.setWeather(Weather.SUNNY_DESERT)

            verify {
                nativeFunctionExecutor.setPlayerWeather(
                        playerid = playerId.value,
                        weather = Weather.SUNNY_DESERT.value
                )
            }
        }

        @Test
        fun shouldSetWeatherByInt() {
            every { nativeFunctionExecutor.setPlayerWeather(any(), any()) } returns true

            player.setWeather(13)

            verify { nativeFunctionExecutor.setPlayerWeather(playerid = playerId.value, weather = 13) }
        }
    }

    @Test
    fun shouldForceClassSelection() {
        every { nativeFunctionExecutor.forceClassSelection(any()) } returns true

        player.forceClassSelection()

        verify { nativeFunctionExecutor.forceClassSelection(playerId.value) }
    }

    @Nested
    inner class WantedLevelTests {

        @Test
        fun shouldGetWantedLevel() {
            every { nativeFunctionExecutor.getPlayerWantedLevel(playerId.value) } returns 69

            val wantedLevel = player.wantedLevel

            assertThat(wantedLevel)
                    .isEqualTo(69)
        }

        @Test
        fun shouldSetWantedLevel() {
            every { nativeFunctionExecutor.setPlayerWantedLevel(any(), any()) } returns true

            player.wantedLevel = 69

            verify { nativeFunctionExecutor.setPlayerWantedLevel(playerid = playerId.value, level = 69) }
        }
    }

    @Nested
    inner class FightingStyleTests {

        @Test
        fun shouldGetFightingStyle() {
            every { nativeFunctionExecutor.getPlayerFightingStyle(playerId.value) } returns FightingStyle.GRABKICK.value

            val fightingStyle = player.fightingStyle

            assertThat(fightingStyle)
                    .isEqualTo(FightingStyle.GRABKICK)
        }

        @Test
        fun shouldSetFightingStyle() {
            every { nativeFunctionExecutor.setPlayerFightingStyle(any(), any()) } returns true

            player.fightingStyle = FightingStyle.GRABKICK

            verify {
                nativeFunctionExecutor.setPlayerFightingStyle(
                        playerid = playerId.value,
                        style = FightingStyle.GRABKICK.value
                )
            }
        }
    }

    @Test
    fun shouldPlayCrimeReport() {
        every { nativeFunctionExecutor.playCrimeReportForPlayer(any(), any(), any()) } returns true

        player.playCrimeReport(otherPlayer, CrimeReport.CODE_10_17)

        verify {
            nativeFunctionExecutor.playCrimeReportForPlayer(
                    playerid = playerId.value,
                    suspectid = otherPlayerId.value,
                    crime = CrimeReport.CODE_10_17.value
            )
        }
    }

    @Test
    fun shouldSetShopName() {
        every { nativeFunctionExecutor.setPlayerShopName(any(), any()) } returns true

        player.setShopName(ShopName.AMMUN2)

        verify { nativeFunctionExecutor.setPlayerShopName(playerid = playerId.value, shopname = ShopName.AMMUN2.value) }
    }

    @Nested
    inner class SurfingVehicleTests {

        @Test
        fun shouldReturnSurfingVehicle() {
            val vehicleId = 1337
            val vehicle = mockk<Vehicle>()
            every { nativeFunctionExecutor.getPlayerSurfingVehicleID(playerId.value) } returns vehicleId
            every { vehicleRegistry[vehicleId] } returns vehicle

            val surfingVehicle = player.surfingVehicle

            assertThat(surfingVehicle)
                    .isSameAs(vehicle)
        }

        @Test
        fun givenNoSurfingVehicleItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerSurfingVehicleID(playerId.value) } returns SAMPConstants.INVALID_VEHICLE_ID
            every { vehicleRegistry[SAMPConstants.INVALID_VEHICLE_ID] } returns null

            val surfingVehicle = player.surfingVehicle

            assertThat(surfingVehicle)
                    .isNull()
        }
    }

    @Nested
    inner class SurfingMapObjectTests {

        @Test
        fun shouldReturnSurfingMapObject() {
            val mapObjectId = 1337
            val mapObject = mockk<MapObject>()
            every { nativeFunctionExecutor.getPlayerSurfingObjectID(playerId.value) } returns mapObjectId
            every { mapObjectRegistry[mapObjectId] } returns mapObject

            val surfingMapObject = player.surfingObject

            assertThat(surfingMapObject)
                    .isSameAs(mapObject)
        }

        @Test
        fun givenNoSurfingMapObjectItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerSurfingObjectID(playerId.value) } returns SAMPConstants.INVALID_OBJECT_ID
            every { mapObjectRegistry[SAMPConstants.INVALID_OBJECT_ID] } returns null

            val surfingMapObject = player.surfingObject

            assertThat(surfingMapObject)
                    .isNull()
        }
    }

    @Test
    fun shouldRemoveBuilding() {
        every { nativeFunctionExecutor.removeBuildingForPlayer(any(), any(), any(), any(), any(), any()) } returns true

        player.removeBuilding(1337, sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f))

        verify {
            nativeFunctionExecutor.removeBuildingForPlayer(
                    playerid = playerId.value,
                    modelid = 1337,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f,
                    fRadius = 4f
            )
        }
    }

    @Test
    fun shouldInitializeAttachedObjectSlots() {
        assertThat(player.attachedObjectSlots)
                .hasSize(10)
        player.attachedObjectSlots.forEachIndexed { index, attachedObjectSlot ->
            assertThat(attachedObjectSlot)
                    .isInstanceOfSatisfying(AttachedObjectSlot::class.java) {
                        assertThat(it.index)
                                .isEqualTo(index)
                        assertThat(it.player)
                                .isSameAs(player)
                    }
        }
    }

    @Test
    fun shouldInitializePlayerVars() {
        assertThat(player.playerVars)
                .isInstanceOfSatisfying(PlayerVars::class.java) {
                    assertThat(it.player)
                            .isSameAs(player)
                }
    }

    @Test
    fun shouldSetChatBubble() {
        every { nativeFunctionExecutor.setPlayerChatBubble(any(), any(), any(), any(), any()) } returns true

        player.setChatBubble(text = "Hi there", color = Colors.RED, drawDistance = 50f, expireTime = 360)

        verify {
            nativeFunctionExecutor.setPlayerChatBubble(
                    playerid = playerId.value,
                    color = Colors.RED.value,
                    text = "Hi there",
                    drawdistance = 50f,
                    expiretime = 360
            )
        }
    }

    @Nested
    inner class VehicleTests {

        @Test
        fun shouldReturnVehicle() {
            val vehicleId = 1337
            val vehicle = mockk<Vehicle>()
            every { nativeFunctionExecutor.getPlayerVehicleID(playerId.value) } returns vehicleId
            every { vehicleRegistry[vehicleId] } returns vehicle

            val playerVehicle = player.vehicle

            assertThat(playerVehicle)
                    .isSameAs(vehicle)
        }

        @Test
        fun givenNoVehicleItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerVehicleID(playerId.value) } returns SAMPConstants.INVALID_VEHICLE_ID
            every { vehicleRegistry[SAMPConstants.INVALID_VEHICLE_ID] } returns null

            val playerVehicle = player.vehicle

            assertThat(playerVehicle)
                    .isNull()
        }
    }

    @Nested
    inner class VehicleSeatTests {

        @ParameterizedTest
        @ValueSource(ints = [0, 1, 2, 3])
        fun shouldReturnVehicleSeat(expectedSeat: Int) {
            every { nativeFunctionExecutor.getPlayerVehicleSeat(playerId.value) } returns expectedSeat

            val seat = player.vehicleSeat

            assertThat(seat)
                    .isSameAs(expectedSeat)
        }

        @Test
        fun givenNoVehicleSeatItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerVehicleSeat(playerId.value) } returns -1

            val seat = player.vehicleSeat

            assertThat(seat)
                    .isNull()
        }
    }

    @Test
    fun shouldPutPlayerInVehicle() {
        val vehicleId = VehicleId.valueOf(1337)
        val vehicle = mockk<Vehicle> {
            every { id } returns vehicleId
        }
        every { nativeFunctionExecutor.putPlayerInVehicle(any(), any(), any()) } returns true

        player.putInVehicle(vehicle, 3)

        verify {
            nativeFunctionExecutor.putPlayerInVehicle(
                    playerid = playerId.value,
                    vehicleid = vehicleId.value,
                    seatid = 3
            )
        }
    }

    @Test
    fun shouldRemovePlayerFromVehicle() {
        every { nativeFunctionExecutor.removePlayerFromVehicle(any()) } returns true

        player.removeFromVehicle()

        verify { nativeFunctionExecutor.removePlayerFromVehicle(playerId.value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldTogglePlayerControllable(toggle: Boolean) {
        every { nativeFunctionExecutor.togglePlayerControllable(any(), any()) } returns true

        player.toggleControllable(toggle)

        verify { nativeFunctionExecutor.togglePlayerControllable(playerId.value, toggle) }
    }

    @Test
    fun shouldPlaySound() {
        every { nativeFunctionExecutor.playerPlaySound(any(), any(), any(), any(), any()) } returns true

        player.playSound(1337, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify {
            nativeFunctionExecutor.playerPlaySound(
                    playerid = playerId.value,
                    soundid = 1337,
                    x = 1f,
                    y = 2f,
                    z = 3f
            )
        }
    }

    @Nested
    inner class SpecialActionTests {

        @Test
        fun shouldGetSpecialAction() {
            every { nativeFunctionExecutor.getPlayerSpecialAction(playerId.value) } returns SpecialAction.USE_JETPACK.value

            val specialAction = player.specialAction

            assertThat(specialAction)
                    .isEqualTo(SpecialAction.USE_JETPACK)
        }

        @Test
        fun shouldSetSpecialAction() {
            every { nativeFunctionExecutor.setPlayerSpecialAction(any(), any()) } returns true

            player.specialAction = SpecialAction.USE_JETPACK

            verify { nativeFunctionExecutor.setPlayerSpecialAction(playerId.value, SpecialAction.USE_JETPACK.value) }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldDisableRemoteVehicleCollisions(disable: Boolean) {
        every { nativeFunctionExecutor.disableRemoteVehicleCollisions(any(), any()) } returns true

        player.disableRemoteVehicleCollisions(disable)

        verify { nativeFunctionExecutor.disableRemoteVehicleCollisions(playerId.value, disable) }
    }

    @Nested
    inner class CheckpointTests {

        @Test
        fun shouldSetCheckpoint() {
            every { nativeFunctionExecutor.setPlayerCheckpoint(any(), any(), any(), any(), any()) } returns true
            val checkpoint = mockk<Checkpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { isDestroyed } returns false
            }

            player.checkpoint = checkpoint

            verify {
                nativeFunctionExecutor.setPlayerCheckpoint(
                        playerid = playerId.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        size = 4f
                )
            }
            assertThat(player.checkpoint)
                    .isSameAs(checkpoint)
        }

        @Test
        fun givenCheckpointIsDestroyedItShouldThrowAnException() {
            val checkpoint = mockk<Checkpoint> {
                every { isDestroyed } returns true
            }

            val caughtThrowable = catchThrowable { player.checkpoint = checkpoint }

            assertThat(caughtThrowable)
                    .isInstanceOf(AlreadyDestroyedException::class.java)
        }

        @Test
        fun givenNullIsSetAsCheckpointItShouldDisableCheckpoint() {
            every { nativeFunctionExecutor.disablePlayerCheckpoint(any()) } returns true

            player.checkpoint = null

            verify { nativeFunctionExecutor.disablePlayerCheckpoint(playerId.value) }
            val checkpoint = player.checkpoint
            // IntelliJ doesn't like it when we're directly using player.checkpoint, after it has been set to null...
            assertThat(checkpoint)
                    .isNull()
        }
    }

    @Nested
    inner class RaceCheckpointTests {

        @Test
        fun shouldSetRaceCheckpoint() {
            every {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
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
            val raceCheckpoint = mockk<RaceCheckpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { nextCoordinates } returns vector3DOf(x = 5f, y = 6f, z = 7f)
                every { type } returns RaceCheckpointType.AIR_NORMAL
                every { isDestroyed } returns false
            }

            player.raceCheckpoint = raceCheckpoint

            verify {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
                        playerid = playerId.value,
                        type = RaceCheckpointType.AIR_NORMAL.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        size = 4f,
                        nextx = 5f,
                        nexty = 6f,
                        nextz = 7f
                )
            }
            assertThat(player.raceCheckpoint)
                    .isSameAs(raceCheckpoint)
        }

        @Test
        fun givenRaceCheckpointIsDestroyedItShouldThrowAnException() {
            val raceCheckpoint = mockk<RaceCheckpoint> {
                every { isDestroyed } returns true
            }

            val caughtThrowable = catchThrowable { player.raceCheckpoint = raceCheckpoint }

            assertThat(caughtThrowable)
                    .isInstanceOf(AlreadyDestroyedException::class.java)
        }

        @Test
        fun givenNoNextCheckpointItShouldUseCurrentCoordinates() {
            every {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
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
            val raceCheckpoint = mockk<RaceCheckpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { nextCoordinates } returns null
                every { type } returns RaceCheckpointType.AIR_NORMAL
                every { isDestroyed } returns false
            }

            player.raceCheckpoint = raceCheckpoint

            verify {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
                        playerid = playerId.value,
                        type = RaceCheckpointType.AIR_NORMAL.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        size = 4f,
                        nextx = 1f,
                        nexty = 2f,
                        nextz = 3f
                )
            }
            assertThat(player.raceCheckpoint)
                    .isSameAs(raceCheckpoint)
        }

        @Test
        fun givenNullIsSetAsRaceCheckpointItShouldDisableRaceCheckpoint() {
            every { nativeFunctionExecutor.disablePlayerRaceCheckpoint(any()) } returns true

            player.raceCheckpoint = null

            verify { nativeFunctionExecutor.disablePlayerRaceCheckpoint(playerId.value) }
            val raceCheckpoint = player.raceCheckpoint
            // IntelliJ doesn't like it when we're directly using player.raceCheckpoint, after it has been set to null...
            assertThat(raceCheckpoint)
                    .isNull()
        }
    }

    @Nested
    inner class WorldBoundsTests {

        @Test
        fun shouldSetWorldBounds() {
            every { nativeFunctionExecutor.setPlayerWorldBounds(any(), any(), any(), any(), any()) } returns true

            player.worldBounds = rectangleOf(minX = -20f, maxX = 30f, minY = 50f, maxY = 60f)

            verify {
                nativeFunctionExecutor.setPlayerWorldBounds(
                        playerid = playerId.value,
                        x_min = -20f,
                        x_max = 30f,
                        y_min = 50f,
                        y_max = 60f
                )
            }
            assertThat(player.worldBounds)
                    .isEqualTo(rectangleOf(minX = -20f, maxX = 30f, minY = 50f, maxY = 60f))
        }

        @Test
        fun shouldSetWorldBoundsToNullShouldRestoreDefaultBounds() {
            every { nativeFunctionExecutor.setPlayerWorldBounds(any(), any(), any(), any(), any()) } returns true

            player.worldBounds = null

            verify {
                nativeFunctionExecutor.setPlayerWorldBounds(
                        playerid = playerId.value,
                        x_min = -20_000f,
                        x_max = 20_000f,
                        y_min = -20_000f,
                        y_max = 20_000f
                )
            }
            val worldBounds = player.worldBounds
            // IntelliJ doesn't like it when we're directly using player.worldBounds, after it has been set to null...
            assertThat(worldBounds)
                    .isNull()
        }
    }

    @Test
    fun shouldShowPlayerMarker() {
        every { nativeFunctionExecutor.setPlayerMarkerForPlayer(any(), any(), any()) } returns true

        player.showPlayerMarker(otherPlayer, Colors.RED)

        verify {
            nativeFunctionExecutor.setPlayerMarkerForPlayer(
                    playerid = playerId.value,
                    showplayerid = otherPlayerId.value,
                    color = Colors.RED.value
            )
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldShowPlayerNameTag(show: Boolean) {
        every { nativeFunctionExecutor.showPlayerNameTagForPlayer(any(), any(), any()) } returns true

        player.showPlayerNameTag(otherPlayer, show)

        verify {
            nativeFunctionExecutor.showPlayerNameTagForPlayer(
                    playerid = playerId.value,
                    showplayerid = otherPlayerId.value,
                    show = show
            )
        }
    }

    @Nested
    inner class MapIconTests {

        @Test
        fun shouldCreateMapIcon() {
            every { playerMapIconFactory.create(any(), any(), any(), any(), any(), any()) } returns mockk()

            player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(50),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            verify {
                playerMapIconFactory.create(
                        player = player,
                        playerMapIconId = PlayerMapIconId.valueOf(50),
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        color = Colors.RED,
                        type = MapIconType.BALLAS,
                        style = MapIconStyle.GLOBAL
                )
            }
        }

        @Test
        fun shouldReturnCreatedMapIcon() {
            val expectedMapIcon = mockk<PlayerMapIcon>()
            every { playerMapIconFactory.create(any(), any(), any(), any(), any(), any()) } returns expectedMapIcon

            val mapIcon = player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(50),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            assertThat(mapIcon)
                    .isEqualTo(expectedMapIcon)
        }

        @Test
        fun shouldStoreCreatedMapIcon() {
            val playerMapIconId = PlayerMapIconId.valueOf(50)
            val expectedMapIcon = mockk<PlayerMapIcon> {
                every { id } returns playerMapIconId
            }
            every { playerMapIconFactory.create(any(), any(), any(), any(), any(), any()) } returns expectedMapIcon

            val mapIcon = player.createMapIcon(
                    playerMapIconId = playerMapIconId,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            assertThat(player.mapIcons)
                    .containsExactly(mapIcon)
        }

        @Test
        fun shouldUnregstierMapIcon() {
            val playerMapIconId = PlayerMapIconId.valueOf(50)
            val expectedMapIcon = mockk<PlayerMapIcon> {
                every { id } returns playerMapIconId
            }
            every { playerMapIconFactory.create(any(), any(), any(), any(), any(), any()) } returns expectedMapIcon
            val mapIcon = player.createMapIcon(
                    playerMapIconId = playerMapIconId,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            player.unregisterMapIcon(mapIcon)

            assertThat(player.mapIcons)
                    .isEmpty()
        }

        @Test
        fun shouldNotOverwriteMapIconWithDifferentId() {
            val existingMapIcon = mockk<PlayerMapIcon> {
                every { id } returns PlayerMapIconId.valueOf(75)
            }
            val newMapIcon = mockk<PlayerMapIcon> {
                every { id } returns PlayerMapIconId.valueOf(50)
            }
            every {
                playerMapIconFactory.create(any(), any(), any(), any(), any(), any())
            } returnsMany listOf(existingMapIcon, newMapIcon)
            player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(75),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            val mapIcon = player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(50),
                    coordinates = vector3DOf(x = 4f, y = 5f, z = 6f),
                    color = Colors.BLUE,
                    type = MapIconType.AIR_YARD,
                    style = MapIconStyle.LOCAL
            )

            assertThat(player.mapIcons)
                    .containsExactlyInAnyOrder(mapIcon, existingMapIcon)
        }

        @Test
        fun shouldOverwriteMapIconWithSameId() {
            val playerMapIconId = PlayerMapIconId.valueOf(50)
            val existingMapIcon = mockk<PlayerMapIcon> {
                every { id } returns playerMapIconId
                every { destroy() } just Runs
            }
            val newMapIcon = mockk<PlayerMapIcon> {
                every { id } returns playerMapIconId
            }
            every {
                playerMapIconFactory.create(any(), any(), any(), any(), any(), any())
            } returnsMany listOf(existingMapIcon, newMapIcon)
            player.createMapIcon(
                    playerMapIconId = playerMapIconId,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )

            player.createMapIcon(
                    playerMapIconId = playerMapIconId,
                    coordinates = vector3DOf(x = 4f, y = 5f, z = 6f),
                    color = Colors.BLUE,
                    type = MapIconType.AIR_YARD,
                    style = MapIconStyle.LOCAL
            )

            assertThat(player.mapIcons)
                    .containsExactlyInAnyOrder(newMapIcon)
            verify { existingMapIcon.destroy() }
            verify(exactly = 0) { newMapIcon.destroy() }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsInVehicle(expectedResult: Boolean) {
        val vehicleId = VehicleId.valueOf(20)
        val vehicle = mockk<Vehicle> {
            every { id } returns vehicleId
        }
        every {
            nativeFunctionExecutor.isPlayerInVehicle(playerid = playerId.value, vehicleid = vehicleId.value)
        } returns expectedResult

        val result = player.isInVehicle(vehicle)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsInAnyVehicle(expectedResult: Boolean) {
        every {
            nativeFunctionExecutor.isPlayerInAnyVehicle(playerId.value)
        } returns expectedResult

        val result = player.isInAnyVehicle

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsInAnyCheckpoint(expectedResult: Boolean) {
        every {
            nativeFunctionExecutor.isPlayerInCheckpoint(playerId.value)
        } returns expectedResult

        val result = player.isInAnyCheckpoint

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class IsPlayerInCheckpointTests {

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentCheckpointIsNullItShouldReturnFalse(isInAnyCheckpoint: Boolean) {
            val checkpoint = mockk<Checkpoint>()
            every {
                nativeFunctionExecutor.isPlayerInCheckpoint(playerId.value)
            } returns isInAnyCheckpoint
            every { nativeFunctionExecutor.setPlayerCheckpoint(any(), any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.disablePlayerCheckpoint(any()) } returns true
            player.checkpoint = null

            val result = player.isInCheckpoint(checkpoint)

            assertThat(result)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentCheckpointIsDifferentCheckpointItShouldReturnFalse(isInAnyCheckpoint: Boolean) {
            val checkpoint = mockk<Checkpoint>()
            val otherCheckpoint = mockk<Checkpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { isDestroyed } returns false
            }
            every {
                nativeFunctionExecutor.isPlayerInCheckpoint(playerId.value)
            } returns isInAnyCheckpoint
            every { nativeFunctionExecutor.setPlayerCheckpoint(any(), any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.disablePlayerCheckpoint(any()) } returns true
            player.checkpoint = otherCheckpoint

            val result = player.isInCheckpoint(checkpoint)

            assertThat(result)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentCheckpointIsGivenCheckpointItShouldReturnExpectedResult(expectedResult: Boolean) {
            val checkpoint = mockk<Checkpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { isDestroyed } returns false
            }
            every {
                nativeFunctionExecutor.isPlayerInCheckpoint(playerId.value)
            } returns expectedResult
            every { nativeFunctionExecutor.setPlayerCheckpoint(any(), any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.disablePlayerCheckpoint(any()) } returns true
            player.checkpoint = checkpoint

            val result = player.isInCheckpoint(checkpoint)

            assertThat(result)
                    .isEqualTo(expectedResult)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsInAnyRaceCheckpoint(expectedResult: Boolean) {
        every {
            nativeFunctionExecutor.isPlayerInRaceCheckpoint(playerId.value)
        } returns expectedResult

        val result = player.isInAnyRaceCheckpoint

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class IsPlayerInRaceCheckpointTests {

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentRaceCheckpointIsNullItShouldReturnFalse(isInAnyRaceCheckpoint: Boolean) {
            val raceCheckpoint = mockk<RaceCheckpoint>()
            every {
                nativeFunctionExecutor.isPlayerInRaceCheckpoint(playerId.value)
            } returns isInAnyRaceCheckpoint
            every {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
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
            every { nativeFunctionExecutor.disablePlayerRaceCheckpoint(any()) } returns true
            player.raceCheckpoint = null

            val result = player.isInRaceCheckpoint(raceCheckpoint)

            assertThat(result)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentRaceCheckpointIsDifferentRaceCheckpointItShouldReturnFalse(isInAnyRaceCheckpoint: Boolean) {
            val raceCheckpoint = mockk<RaceCheckpoint>()
            val otherRaceCheckpoint = mockk<RaceCheckpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { nextCoordinates } returns vector3DOf(x = 5f, y = 6f, z = 7f)
                every { type } returns RaceCheckpointType.AIR_NORMAL
                every { isDestroyed } returns false
            }
            every {
                nativeFunctionExecutor.isPlayerInRaceCheckpoint(playerId.value)
            } returns isInAnyRaceCheckpoint
            every {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
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
            every { nativeFunctionExecutor.disablePlayerRaceCheckpoint(any()) } returns true
            player.raceCheckpoint = otherRaceCheckpoint

            val result = player.isInRaceCheckpoint(raceCheckpoint)

            assertThat(result)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenCurrentRaceCheckpointIsGivenRaceCheckpointItShouldReturnExpectedResult(expectedResult: Boolean) {
            val raceCheckpoint = mockk<RaceCheckpoint> {
                every { coordinates } returns vector3DOf(x = 1f, y = 2f, z = 3f)
                every { size } returns 4f
                every { nextCoordinates } returns vector3DOf(x = 5f, y = 6f, z = 7f)
                every { type } returns RaceCheckpointType.AIR_NORMAL
                every { isDestroyed } returns false
            }
            every {
                nativeFunctionExecutor.isPlayerInRaceCheckpoint(playerId.value)
            } returns expectedResult
            every {
                nativeFunctionExecutor.setPlayerRaceCheckpoint(
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
            every { nativeFunctionExecutor.disablePlayerRaceCheckpoint(any()) } returns true
            player.raceCheckpoint = raceCheckpoint

            val result = player.isInRaceCheckpoint(raceCheckpoint)

            assertThat(result)
                    .isEqualTo(expectedResult)
        }
    }

    @Test
    fun shouldEnableStuntBonus() {
        every { nativeFunctionExecutor.enableStuntBonusForPlayer(any(), any()) } returns true

        player.enableStuntBonus()

        verify { nativeFunctionExecutor.enableStuntBonusForPlayer(playerId.value, true) }
    }

    @Test
    fun shouldDisableStuntBonus() {
        every { nativeFunctionExecutor.enableStuntBonusForPlayer(any(), any()) } returns true

        player.disableStuntBonus()

        verify { nativeFunctionExecutor.enableStuntBonusForPlayer(playerId.value, false) }
    }

    @Nested
    inner class SpectatePlayerTests {

        @ParameterizedTest
        @EnumSource(SpectateType::class)
        fun givenPlayerIsNotSpectatingItShouldToggleSpectatingAndSpectateOtherPlayer(type: SpectateType) {
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectatePlayer(any(), any(), any()) } returns true

            player.spectate(otherPlayer, type)

            assertThat(player.isSpectating)
                    .isTrue()
            verifyOrder {
                nativeFunctionExecutor.togglePlayerSpectating(playerId.value, true)
                nativeFunctionExecutor.playerSpectatePlayer(
                        playerid = playerId.value,
                        targetplayerid = otherPlayerId.value,
                        mode = type.value
                )
            }
        }

        @ParameterizedTest
        @EnumSource(SpectateType::class)
        fun givenPlayerIsSpectatingItShouldNotToggleSpectatingAgainAndSpectateOtherPlayer(type: SpectateType) {
            val spectatedPlayerId = PlayerId.valueOf(123)
            val spectatedPlayer = mockk<Player> {
                every { id } returns spectatedPlayerId
            }
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectatePlayer(any(), any(), any()) } returns true
            player.spectate(spectatedPlayer, type)

            player.spectate(otherPlayer, type)

            assertThat(player.isSpectating)
                    .isTrue()
            verifyOrder {
                nativeFunctionExecutor.togglePlayerSpectating(playerId.value, true)
                nativeFunctionExecutor.playerSpectatePlayer(
                        playerid = playerId.value,
                        targetplayerid = spectatedPlayerId.value,
                        mode = type.value
                )
                nativeFunctionExecutor.playerSpectatePlayer(
                        playerid = playerId.value,
                        targetplayerid = otherPlayerId.value,
                        mode = type.value
                )
            }
        }

        @Test
        fun shouldStopSpectating() {
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectatePlayer(any(), any(), any()) } returns true
            player.spectate(otherPlayer, SpectateType.FIXED)

            player.stopSpectating()

            assertThat(player.isSpectating)
                    .isFalse()
            verifyOrder { nativeFunctionExecutor.togglePlayerSpectating(playerId.value, false) }
        }
    }

    @Nested
    inner class SpectateVehicleTests {

        @ParameterizedTest
        @EnumSource(SpectateType::class)
        fun givenPlayerIsNotSpectatingItShouldToggleSpectatingAndSpectateOtherPlayer(type: SpectateType) {
            val vehicleId = VehicleId.valueOf(20)
            val vehicle = mockk<Vehicle> {
                every { id } returns vehicleId
            }
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectateVehicle(any(), any(), any()) } returns true

            player.spectate(vehicle, type)

            assertThat(player.isSpectating)
                    .isTrue()
            verifyOrder {
                nativeFunctionExecutor.togglePlayerSpectating(playerId.value, true)
                nativeFunctionExecutor.playerSpectateVehicle(
                        playerid = playerId.value,
                        targetvehicleid = vehicleId.value,
                        mode = type.value
                )
            }
        }

        @ParameterizedTest
        @EnumSource(SpectateType::class)
        fun givenPlayerIsSpectatingItShouldNotToggleSpectatingAgainAndSpectateOtherPlayer(type: SpectateType) {
            val oldVehicleId = VehicleId.valueOf(20)
            val oldVehicle = mockk<Vehicle> {
                every { id } returns oldVehicleId
            }
            val newVehicleId = VehicleId.valueOf(20)
            val newVehicle = mockk<Vehicle> {
                every { id } returns newVehicleId
            }
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectateVehicle(any(), any(), any()) } returns true
            player.spectate(oldVehicle, type)

            player.spectate(newVehicle, type)

            assertThat(player.isSpectating)
                    .isTrue()
            verifyOrder {
                nativeFunctionExecutor.togglePlayerSpectating(playerId.value, true)
                nativeFunctionExecutor.playerSpectateVehicle(
                        playerid = playerId.value,
                        targetvehicleid = oldVehicleId.value,
                        mode = type.value
                )
                nativeFunctionExecutor.playerSpectateVehicle(
                        playerid = playerId.value,
                        targetvehicleid = newVehicleId.value,
                        mode = type.value
                )
            }
        }

        @Test
        fun shouldStopSpectating() {
            val vehicleId = VehicleId.valueOf(20)
            val vehicle = mockk<Vehicle> {
                every { id } returns vehicleId
            }
            every { nativeFunctionExecutor.togglePlayerSpectating(any(), any()) } returns true
            every { nativeFunctionExecutor.playerSpectateVehicle(any(), any(), any()) } returns true
            player.spectate(vehicle, SpectateType.FIXED)

            player.stopSpectating()

            assertThat(player.isSpectating)
                    .isFalse()
            verifyOrder { nativeFunctionExecutor.togglePlayerSpectating(playerId.value, false) }
        }
    }

    @ParameterizedTest
    @EnumSource(PlayerRecordingType::class)
    fun shouldStartRecording(type: PlayerRecordingType) {
        every { nativeFunctionExecutor.startRecordingPlayerData(any(), any(), any()) } returns true

        player.startRecording(type, "test")

        verify {
            nativeFunctionExecutor.startRecordingPlayerData(
                    playerid = playerId.value,
                    recordtype = type.value,
                    recordname = "test"
            )
        }
    }

    @Test
    fun shouldStopRecording() {
        every { nativeFunctionExecutor.stopRecordingPlayerData(any()) } returns true

        player.stopRecording()

        verify { nativeFunctionExecutor.stopRecordingPlayerData(playerId.value) }
    }

    @Nested
    inner class CreateExplosionTests {

        @Test
        fun shouldCreateExplosionUsingSphere() {
            every {
                nativeFunctionExecutor.createExplosionForPlayer(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true

            player.createExplosion(ExplosionType.NORMAL_3, sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f))

            verify {
                nativeFunctionExecutor.createExplosionForPlayer(
                        playerid = playerId.value,
                        type = ExplosionType.NORMAL_3.value,
                        X = 1f,
                        Y = 2f,
                        Z = 3f,
                        Radius = 4f
                )
            }
        }

        @Test
        fun shouldCreateExplosionUsingVector() {
            every {
                nativeFunctionExecutor.createExplosionForPlayer(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true

            player.createExplosion(ExplosionType.NORMAL_3, vector3DOf(x = 1f, y = 2f, z = 3f), 4f)

            verify {
                nativeFunctionExecutor.createExplosionForPlayer(
                        playerid = playerId.value,
                        type = ExplosionType.NORMAL_3.value,
                        X = 1f,
                        Y = 2f,
                        Z = 3f,
                        Radius = 4f
                )
            }
        }
    }

    @Nested
    inner class IsAdminTests {

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldReturnExpectedResult(expectedResult: Boolean) {
            every { nativeFunctionExecutor.isPlayerAdmin(playerId.value) } returns expectedResult

            val result = player.isAdmin

            assertThat(result)
                    .isEqualTo(expectedResult)
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenPlayerIsNotAdminItShouldCheckAgainOnSecondCall(expectedResult: Boolean) {
            every { nativeFunctionExecutor.isPlayerAdmin(playerId.value) } returnsMany listOf(false, expectedResult)
            player.isAdmin

            val result = player.isAdmin

            assertThat(result)
                    .isEqualTo(expectedResult)
            verify(exactly = 2) { nativeFunctionExecutor.isPlayerAdmin(playerId.value) }
        }

        @Test
        fun givenPlayerIsAdminItShouldRememberResult() {
            every { nativeFunctionExecutor.isPlayerAdmin(playerId.value) } returns true
            player.isAdmin

            val result = player.isAdmin

            assertThat(result)
                    .isTrue()
            verify(exactly = 1) { nativeFunctionExecutor.isPlayerAdmin(playerId.value) }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsNPC(expectedResult: Boolean) {
        every { nativeFunctionExecutor.isPlayerNPC(playerId.value) } returns expectedResult

        val result = player.isNPC

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherPlayerIsHuman(isNPC: Boolean) {
        every { nativeFunctionExecutor.isPlayerNPC(playerId.value) } returns isNPC

        val result = player.isHuman

        assertThat(result)
                .isEqualTo(!isNPC)
    }

    @Test
    fun shouldKickPlayer() {
        every { nativeFunctionExecutor.kick(any()) } returns true

        player.kick()

        verify { nativeFunctionExecutor.kick(playerId.value) }
    }

    @Nested
    inner class BanTests {

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun givenBlankReasonItShouldBanPlayer(reason: String) {
            every { nativeFunctionExecutor.ban(any()) } returns true

            player.ban(reason)

            verify { nativeFunctionExecutor.ban(playerId.value) }
        }

        @Test
        fun givenNoReasonAsReasonItShouldBanPlayer() {
            every { nativeFunctionExecutor.ban(any()) } returns true

            player.ban()

            verify { nativeFunctionExecutor.ban(playerId.value) }
        }

        @Test
        fun givenNonBlankReasonAsReasonItShouldBanPlayer() {
            every { nativeFunctionExecutor.banEx(any(), any()) } returns true

            player.ban("test")

            verify { nativeFunctionExecutor.banEx(playerId.value, "test") }
        }
    }

    @Nested
    inner class VersionTests {

        @Test
        fun shouldReturnVersion() {
            every {
                nativeFunctionExecutor.getPlayerVersion(playerid = playerId.value, version = any(), len = 24)
            } answers {
                secondArg<ReferenceString>().value = "0.3.7"
                true
            }

            val version = player.version

            assertThat(version)
                    .isEqualTo("0.3.7")
        }

        @Test
        fun givenVersionIsNullItShouldReturnEmptyString() {
            every {
                nativeFunctionExecutor.getPlayerVersion(playerid = playerId.value, version = any(), len = 24)
            } returns true

            val version = player.version

            assertThat(version)
                    .isEmpty()
        }
    }

    @Test
    fun shouldInitializeNetworkStatistics() {
        val networkStatistics = player.networkStatistics

        assertThat(networkStatistics)
                .isInstanceOfSatisfying(PlayerNetworkStatistics::class.java) {
                    assertThat(it.player)
                            .isSameAs(player)
                }
    }

    @Test
    fun shouldSelectTextDraw() {
        every { nativeFunctionExecutor.selectTextDraw(any(), any()) } returns true

        player.selectTextDraw(Colors.RED)

        verify { nativeFunctionExecutor.selectTextDraw(playerid = playerId.value, hovercolor = Colors.RED.value) }
    }

    @Test
    fun shouldCancelSelectTextDraw() {
        every { nativeFunctionExecutor.cancelSelectTextDraw(any()) } returns true

        player.cancelSelectTextDraw()

        verify { nativeFunctionExecutor.cancelSelectTextDraw(playerId.value) }
    }

    @Test
    fun shouldSelectMapObject() {
        every { nativeFunctionExecutor.selectObject(any()) } returns true

        player.selectMapObject()

        verify { nativeFunctionExecutor.selectObject(playerId.value) }
    }

    @Test
    fun shouldCancelSelectMapObject() {
        every { nativeFunctionExecutor.cancelEdit(any()) } returns true

        player.cancelEditMapObject()

        verify { nativeFunctionExecutor.cancelEdit(playerId.value) }
    }

    @Nested
    inner class SendDeathMessageTests {

        private val victim = mockk<Player>()
        private val victimId = PlayerId.valueOf(75)

        @BeforeEach
        fun setUp() {
            every { victim.id } returns victimId
        }

        @Test
        fun shouldDeathMessageWithKiller() {
            val killerId = PlayerId.valueOf(25)
            val killer = mockk<Player> {
                every { id } returns killerId
            }
            every { nativeFunctionExecutor.sendDeathMessageToPlayer(any(), any(), any(), any()) } returns true

            player.sendDeathMessage(victim = victim, weapon = WeaponModel.AK47, killer = killer)

            verify {
                nativeFunctionExecutor.sendDeathMessageToPlayer(
                        playerid = playerId.value,
                        killer = killerId.value,
                        weapon = WeaponModel.AK47.value,
                        killee = victimId.value
                )
            }
        }

        @Test
        fun shouldDeathMessageWithoutKiller() {
            every { nativeFunctionExecutor.sendDeathMessageToPlayer(any(), any(), any(), any()) } returns true

            player.sendDeathMessage(victim = victim, weapon = WeaponModel.AK47, killer = null)

            verify {
                nativeFunctionExecutor.sendDeathMessageToPlayer(
                        playerid = playerId.value,
                        killer = SAMPConstants.INVALID_PLAYER_ID,
                        weapon = WeaponModel.AK47.value,
                        killee = victimId.value
                )
            }
        }

    }

    @Nested
    inner class MenuTests {

        @Test
        fun shouldReturnMenu() {
            val menuId = 1337
            val menu = mockk<Menu>()
            every { nativeFunctionExecutor.getPlayerMenu(playerId.value) } returns menuId
            every { menuRegistry[menuId] } returns menu

            val playerMenu = player.menu

            assertThat(playerMenu)
                    .isSameAs(menu)
        }

        @Test
        fun givenNoMenuItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerMenu(playerId.value) } returns SAMPConstants.INVALID_MENU
            every { menuRegistry[SAMPConstants.INVALID_MENU] } returns null

            val playerMenu = player.menu

            assertThat(playerMenu)
                    .isNull()
        }
    }

    @Nested
    inner class OnDisconnectTests {

        @Test
        fun shouldSetIsConnectedToFalse() {
            player.onDisconnect()

            assertThat(player.isConnected)
                    .isFalse()
        }

        @Test
        fun shouldDestroyMapIcons() {
            val mapIcon1 = mockk<PlayerMapIcon> {
                every { id } returns PlayerMapIconId.valueOf(75)
                every { destroy() } just Runs
            }
            val mapIcon2 = mockk<PlayerMapIcon> {
                every { id } returns PlayerMapIconId.valueOf(50)
                every { destroy() } just Runs
            }
            every {
                playerMapIconFactory.create(any(), any(), any(), any(), any(), any())
            } returnsMany listOf(mapIcon1, mapIcon2)
            player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(75),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    color = Colors.RED,
                    type = MapIconType.BALLAS,
                    style = MapIconStyle.GLOBAL
            )
            player.createMapIcon(
                    playerMapIconId = PlayerMapIconId.valueOf(50),
                    coordinates = vector3DOf(x = 4f, y = 5f, z = 6f),
                    color = Colors.BLUE,
                    type = MapIconType.AIR_YARD,
                    style = MapIconStyle.LOCAL
            )

            player.onDisconnect()

            verify {
                mapIcon1.destroy()
                mapIcon2.destroy()
            }
        }

        @Test
        fun givenPlayerDisconnectedIdShouldThrowException() {
            player.onDisconnect()

            val caughtThrowable = catchThrowable { player.id }

            assertThat(caughtThrowable)
                    .isInstanceOf(PlayerOfflineException::class.java)
        }

        @Test
        fun shouldDestroyExtensions() {
            player.onDisconnect()

            val isDestroyed = player.extensions.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

        @Nested
        inner class RequireConnectedTests {

            @Nested
            inner class NonLambdaTests {

                @Test
                fun givenPlayerIsOfflineItShouldThrowAnException() {
                    player.onDisconnect()

                    val caughtThrowable = catchThrowable { player.requireConnected() }

                    assertThat(caughtThrowable)
                            .isInstanceOf(PlayerOfflineException::class.java)
                }

                @Test
                fun givenPlayerIsConnectedItShouldNotThrowAnException() {
                    val returnedPlayer = player.requireConnected()

                    assertThat(returnedPlayer)
                            .isSameAs(player)
                }
            }

            @Nested
            inner class LambdaTests {

                @Test
                fun givenPlayerIsOfflineItShouldThrowAnException() {
                    val block = mockk<Player.() -> Unit>(relaxed = true)
                    player.onDisconnect()

                    val caughtThrowable = catchThrowable { player.requireConnected(block) }

                    assertThat(caughtThrowable)
                            .isInstanceOf(PlayerOfflineException::class.java)
                    verify { block wasNot Called }
                }

                @Test
                fun givenPlayerIsConnectedItShouldNotThrowAnException() {
                    val block = mockk<Player.() -> Int> {
                        every { this@mockk.invoke(player) } returns 1337
                    }

                    val result = player.requireConnected(block)

                    assertThat(result)
                            .isEqualTo(1337)
                }
            }
        }

        @Nested
        inner class IfConnectedTests {

            @Test
            fun givenPlayerIsConnectedItShouldReturnExpectedValue() {
                val result = player.ifConnected { 1337 }

                assertThat(result)
                        .isEqualTo(1337)
            }

            @Test
            fun givenPlayerIsOfflineItShouldReturnNull() {
                player.onDisconnect()

                val result = player.ifConnected { 1337 }

                assertThat(result)
                        .isNull()
            }

        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isInRangeShouldReturnTrueIfAndOnlyIfIsPlayerInRangeOfPointReturnsTrue(expectedIsInRange: Boolean) {
        every {
            nativeFunctionExecutor.isPlayerInRangeOfPoint(
                    playerId.value,
                    69f,
                    1f,
                    2f,
                    3f
            )
        } returns expectedIsInRange

        val isInRange = player.isInRange(vector3DOf(1f, 2f, 3f), 69f)

        assertThat(isInRange)
                .isEqualTo(expectedIsInRange)
    }

    @Nested
    inner class RedirectDownloadTests {

        @Test
        fun shouldCallNativeFunctionExecutorWithStringURL() {
            every { nativeFunctionExecutor.redirectDownload(any(), any()) } returns true

            player.redirectDownload("www.google.com")

            verify { nativeFunctionExecutor.redirectDownload(playerId.value, "www.google.com") }
        }

        @Test
        fun shouldCallNativeFunctionExecutorWithURL() {
            every { nativeFunctionExecutor.redirectDownload(any(), any()) } returns true

            player.redirectDownload(URL("http://www.google.com"))

            verify { nativeFunctionExecutor.redirectDownload(playerId.value, "http://www.google.com") }
        }

    }
}
