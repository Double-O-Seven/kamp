package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.*
import ch.leadrian.samp.kamp.api.entity.id.*
import ch.leadrian.samp.kamp.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.api.exception.PlayerOfflineException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.factory.PlayerMapIconFactory
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.runtime.types.ReferenceInt
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

internal class PlayerImplTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private lateinit var player: PlayerImpl

    private val otherPlayerId: PlayerId = PlayerId.valueOf(71)
    private val otherPlayer = mockk<PlayerImpl> {
        every { id } returns otherPlayerId
    }

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val actorRegistry = mockk<ActorRegistry>()
    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val menuRegistry = mockk<MenuRegistry>()
    private val playerMapIconFactory = mockk<PlayerMapIconFactory>()
    private val vehicleRegistry = mockk<VehicleRegistry>()

    @BeforeEach
    fun setUp() {
        player = PlayerImpl(
                id = playerId,
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerRegistry = playerRegistry,
                actorRegistry = actorRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry
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
                nativeFunctionExecutor.setSpawnInfo(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true

            player.setSpawnInfo(spawnInfoOf(
                    teamId = TeamId.valueOf(69),
                    position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f),
                    skinModel = SkinModel.ARMY,
                    weapon1 = weaponDataOf(WeaponModel.TEC9, 150),
                    weapon2 = weaponDataOf(WeaponModel.AK47, 300),
                    weapon3 = weaponDataOf(WeaponModel.DESERT_EAGLE, 20)
            ))

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
                nativeFunctionExecutor.setSpawnInfo(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true

            player.setSpawnInfo(spawnInfoOf(
                    position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f),
                    skinModel = SkinModel.ARMY,
                    weapon1 = weaponDataOf(WeaponModel.TEC9, 150),
                    weapon2 = weaponDataOf(WeaponModel.AK47, 300),
                    weapon3 = weaponDataOf(WeaponModel.DESERT_EAGLE, 20)
            ))

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
    inner class CoordinatesTests {

        @Test
        fun shouldGetCoordinates() {
            every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }

            val coordinates = player.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldSetCoordinates() {
            every { nativeFunctionExecutor.setPlayerPos(any(), any(), any(), any()) } returns true

            player.coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)

            verify { nativeFunctionExecutor.setPlayerPos(playerid = playerId.value, x = 1f, y = 2f, z = 3f) }
        }
    }

    @Nested
    inner class PositionTests {

        @Test
        fun shouldGetPosition() {
            every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }
            every { nativeFunctionExecutor.getPlayerFacingAngle(playerId.value, any()) } answers {
                secondArg<ReferenceFloat>().value = 4f
                true
            }

            val position = player.position

            assertThat(position)
                    .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
        }

        @Test
        fun shouldSetPosition() {
            every { nativeFunctionExecutor.setPlayerPos(any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerFacingAngle(any(), any()) } returns true

            player.position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)

            verify {
                nativeFunctionExecutor.setPlayerPos(playerid = playerId.value, x = 1f, y = 2f, z = 3f)
                nativeFunctionExecutor.setPlayerFacingAngle(playerid = playerId.value, angle = 4f)
            }
        }
    }

    @Nested
    inner class LocationTests {

        @Test
        fun shouldGetLocation() {
            every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }
            every { nativeFunctionExecutor.getPlayerInterior(playerId.value) } returns 69
            every { nativeFunctionExecutor.getPlayerVirtualWorld(playerId.value) } returns 1337

            val location = player.location

            assertThat(location)
                    .isEqualTo(locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1337))
        }

        @Test
        fun shouldSetLocation() {
            every { nativeFunctionExecutor.setPlayerPos(any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerInterior(any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerVirtualWorld(any(), any()) } returns true

            player.location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1337)

            verify {
                nativeFunctionExecutor.setPlayerPos(playerid = playerId.value, x = 1f, y = 2f, z = 3f)
                nativeFunctionExecutor.setPlayerInterior(playerid = playerId.value, interiorid = 69)
                nativeFunctionExecutor.setPlayerVirtualWorld(playerid = playerId.value, worldid = 1337)
            }
        }
    }

    @Nested
    inner class AngledLocationTests {

        @Test
        fun shouldGetAngledLocation() {
            every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }
            every { nativeFunctionExecutor.getPlayerFacingAngle(playerId.value, any()) } answers {
                secondArg<ReferenceFloat>().value = 4f
                true
            }
            every { nativeFunctionExecutor.getPlayerInterior(playerId.value) } returns 69
            every { nativeFunctionExecutor.getPlayerVirtualWorld(playerId.value) } returns 1337

            val angledLocation = player.angledLocation

            assertThat(angledLocation)
                    .isEqualTo(angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 69, worldId = 1337))
        }

        @Test
        fun shouldSetAngledLocation() {
            every { nativeFunctionExecutor.setPlayerPos(any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerFacingAngle(any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerInterior(any(), any()) } returns true
            every { nativeFunctionExecutor.setPlayerVirtualWorld(any(), any()) } returns true

            player.angledLocation = angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 69, worldId = 1337)

            verify {
                nativeFunctionExecutor.setPlayerPos(playerid = playerId.value, x = 1f, y = 2f, z = 3f)
                nativeFunctionExecutor.setPlayerFacingAngle(playerid = playerId.value, angle = 4f)
                nativeFunctionExecutor.setPlayerInterior(playerid = playerId.value, interiorid = 69)
                nativeFunctionExecutor.setPlayerVirtualWorld(playerid = playerId.value, worldid = 1337)
            }
        }
    }

    @Nested
    inner class AngleTests {

        @Test
        fun shouldGetAngle() {
            every { nativeFunctionExecutor.getPlayerFacingAngle(playerId.value, any()) } answers {
                secondArg<ReferenceFloat>().value = 4f
                true
            }

            val angle = player.angle

            assertThat(angle)
                    .isEqualTo(4f)
        }

        @Test
        fun shouldSetAngle() {
            every { nativeFunctionExecutor.setPlayerFacingAngle(any(), any()) } returns true

            player.angle = 4f

            verify { nativeFunctionExecutor.setPlayerFacingAngle(playerid = playerId.value, angle = 4f) }
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

    @Nested
    inner class VelocityTests {

        @Test
        fun shouldGetVelocity() {
            every { nativeFunctionExecutor.getPlayerVelocity(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }

            val velocity = player.velocity

            assertThat(velocity)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldSetVelocity() {
            every { nativeFunctionExecutor.setPlayerVelocity(any(), any(), any(), any()) } returns true

            player.velocity = vector3DOf(x = 1f, y = 2f, z = 3f)

            verify { nativeFunctionExecutor.setPlayerVelocity(playerid = playerId.value, x = 1f, y = 2f, z = 3f) }
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
        every { nativeFunctionExecutor.isPlayerStreamedIn(playerid = playerId.value, forplayerid = otherPlayerId.value) } returns expectedResult

        val result = player.isStreamedIn(otherPlayer)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class HealthTests {

        @Test
        fun shouldGetHealth() {
            every { nativeFunctionExecutor.getPlayerHealth(playerId.value, any()) } answers {
                secondArg<ReferenceFloat>().value = 50f
                true
            }

            val health = player.health

            assertThat(health)
                    .isEqualTo(50f)
        }

        @Test
        fun shouldSetHealth() {
            every { nativeFunctionExecutor.setPlayerHealth(any(), any()) } returns true

            player.health = 50f

            verify { nativeFunctionExecutor.setPlayerHealth(playerid = playerId.value, health = 50f) }
        }
    }

    @Nested
    inner class ArmourTests {

        @Test
        fun shouldGetArmour() {
            every { nativeFunctionExecutor.getPlayerArmour(playerId.value, any()) } answers {
                secondArg<ReferenceFloat>().value = 50f
                true
            }

            val armour = player.armour

            assertThat(armour)
                    .isEqualTo(50f)
        }

        @Test
        fun shouldSetArmour() {
            every { nativeFunctionExecutor.setPlayerArmour(any(), any()) } returns true

            player.armour = 50f

            verify { nativeFunctionExecutor.setPlayerArmour(playerid = playerId.value, armour = 50f) }
        }
    }

    @Test
    fun shouldSetAmmo() {
        every { nativeFunctionExecutor.setPlayerAmmo(any(), any(), any()) } returns true

        player.setAmmo(WeaponModel.TEC9, 150)

        verify { nativeFunctionExecutor.setPlayerAmmo(playerid = playerId.value, weaponid = WeaponModel.TEC9.value, ammo = 150) }
    }

    @Test
    fun shouldGetAmmo() {
        every { nativeFunctionExecutor.getPlayerAmmo(playerId.value) } returns 150

        val ammo = player.ammo

        assertThat(ammo)
                .isEqualTo(150)
    }

    @Test
    fun shouldGetWeaponState() {
        every { nativeFunctionExecutor.getPlayerWeaponState(playerId.value) } returns WeaponState.MORE_BULLETS.value

        val weaponState = player.weaponState

        assertThat(weaponState)
                .isEqualTo(WeaponState.MORE_BULLETS)
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
        fun shouldGetColor() {
            every { nativeFunctionExecutor.getPlayerColor(playerId.value) } returns 0x00FF00FF

            val color = player.color

            assertThat(color)
                    .isEqualTo(colorOf(0x00FF00FF))
        }

        @Test
        fun shouldSetColor() {
            every { nativeFunctionExecutor.setPlayerColor(any(), any()) } returns true

            player.color = colorOf(0x00FF00FF)

            verify { nativeFunctionExecutor.setPlayerColor(playerid = playerId.value, color = 0x00FF00FF) }
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
    inner class ArmedWeaponTests {

        @Test
        fun shouldGetArmedWeapon() {
            every { nativeFunctionExecutor.getPlayerWeapon(playerId.value) } returns WeaponModel.TEC9.value

            val armedWeapon = player.armedWeapon

            assertThat(armedWeapon)
                    .isEqualTo(WeaponModel.TEC9)
        }

        @Test
        fun shouldSetArmedWeapon() {
            every { nativeFunctionExecutor.setPlayerArmedWeapon(any(), any()) } returns true

            player.armedWeapon = WeaponModel.TEC9

            verify { nativeFunctionExecutor.setPlayerArmedWeapon(playerid = playerId.value, weaponid = WeaponModel.TEC9.value) }
        }
    }

    @Test
    fun shouldGetWeaponData() {
        every { nativeFunctionExecutor.getPlayerWeaponData(playerId.value, WeaponSlot.MACHINE_PISTOL.value, any(), any()) } answers {
            thirdArg<ReferenceInt>().value = WeaponModel.TEC9.value
            arg<ReferenceInt>(3).value = 150
            true
        }

        val weaponData = player.getWeaponData(WeaponSlot.MACHINE_PISTOL)

        assertThat(weaponData)
                .isEqualTo(weaponDataOf(WeaponModel.TEC9, 150))
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

    @Nested
    inner class NameTests {

        @Test
        fun givenWasNotYetSetItShouldGetPlayerName() {
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } answers {
                secondArg<ReferenceString>().value = "hans.wurst"
                0
            }

            val name = player.name

            assertThat(name)
                    .isEqualTo("hans.wurst")
        }

        @Test
        fun givenResultingNameIsNullItShouldReturnEmptyString() {
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } returns 0

            val name = player.name

            assertThat(name)
                    .isEmpty()
        }

        @Test
        fun givenNameWasSetItShouldReturnCachedName() {
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } answers {
                secondArg<ReferenceString>().value = "John.Sausage"
                0
            }
            every { nativeFunctionExecutor.setPlayerName(playerId.value, any()) } returns 0
            player.name = "hans.wurst"

            val name = player.name

            assertThat(name)
                    .isEqualTo("hans.wurst")
            verify(exactly = 1) { nativeFunctionExecutor.getPlayerName(any(), any(), any()) }
            verifyOrder {
                nativeFunctionExecutor.getPlayerName(any(), any(), any())
                nativeFunctionExecutor.setPlayerName(any(), any())
            }
        }

        @Test
        fun shouldSetPlayerName() {
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } answers {
                secondArg<ReferenceString>().value = "John.Sausage"
                0
            }
            every { nativeFunctionExecutor.setPlayerName(any(), any()) } returns 0

            player.name = "hans.wurst"

            verify { nativeFunctionExecutor.setPlayerName(playerid = playerId.value, name = "hans.wurst") }
        }

        @Test
        fun shouldExecuteOnChangeHandlers() {
            every { nativeFunctionExecutor.setPlayerName(any(), any()) } returns 0
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } answers {
                secondArg<ReferenceString>().value = "hans.wurst"
                0
            }
            val onNameChange = mockk<Player.(String, String) -> Unit>(relaxed = true)
            player.onNameChange(onNameChange)


            player.name = "John.Sausage"

            verify { onNameChange.invoke(player, "hans.wurst", "John.Sausage") }
        }

        @Test
        fun givenEmptyNameIsSetItShouldThrowAnException() {
            val caughtThrowable = catchThrowable { player.name = "" }

            assertThat(caughtThrowable)
                    .isInstanceOf(InvalidPlayerNameException::class.java)
        }

        @Test
        fun givenInvalidNameIsSetItShouldThrowAnException() {
            every { nativeFunctionExecutor.getPlayerName(playerId.value, any(), SAMPConstants.MAX_PLAYER_NAME) } answers {
                secondArg<ReferenceString>().value = "hans.wurst"
                0
            }
            every { nativeFunctionExecutor.setPlayerName(playerId.value, "???") } returns -1
            val caughtThrowable = catchThrowable { player.name = "???" }

            assertThat(caughtThrowable)
                    .isInstanceOf(InvalidPlayerNameException::class.java)
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

    @Test
    fun shouldGetPlayerKeys() {
        every { nativeFunctionExecutor.getPlayerKeys(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceInt>().value = 50
            thirdArg<ReferenceInt>().value = 75
            arg<ReferenceInt>(3).value = 100
            true
        }

        val playerKeys = player.keys

        assertThat(playerKeys)
                .isInstanceOfSatisfying(PlayerKeysImpl::class.java) {
                    assertThat(it.keys)
                            .isEqualTo(50)
                    assertThat(it.upDown)
                            .isEqualTo(75)
                    assertThat(it.leftRight)
                            .isEqualTo(100)
                    assertThat(it.player)
                            .isSameAs(player)
                }
    }

    @Nested
    inner class TimeTests {

        @Test
        fun shouldGetTime() {
            every { nativeFunctionExecutor.getPlayerTime(playerId.value, any(), any()) } answers {
                secondArg<ReferenceInt>().value = 13
                thirdArg<ReferenceInt>().value = 37
                true
            }

            val time = player.time

            assertThat(time)
                    .isEqualTo(timeOf(hour = 13, minute = 37))
        }

        @Test
        fun shouldSetTime() {
            every { nativeFunctionExecutor.setPlayerTime(any(), any(), any()) } returns true

            player.time = timeOf(hour = 13, minute = 37)

            verify { nativeFunctionExecutor.setPlayerTime(playerid = playerId.value, hour = 13, minute = 37) }
        }
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

            verify { nativeFunctionExecutor.setPlayerWeather(playerid = playerId.value, weather = Weather.SUNNY_DESERT.value) }
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

            verify { nativeFunctionExecutor.setPlayerFightingStyle(playerid = playerId.value, style = FightingStyle.GRABKICK.value) }
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

    @Nested
    inner class PlayAudioStreamTests {

        @Test
        fun shouldPlayAudioStream() {
            every { nativeFunctionExecutor.playAudioStreamForPlayer(any(), any(), any(), any(), any(), any(), any()) } returns true

            player.playAudioStream(
                    url = "http://localhost:8080/song/1",
                    position = sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f),
                    usePosition = true
            )

            verify {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        playerid = playerId.value,
                        url = "http://localhost:8080/song/1",
                        posX = 1f,
                        posY = 2f,
                        posZ = 3f,
                        distance = 4f,
                        usepos = true
                )
            }
        }

        @Test
        fun shouldPlayAudioStreamWithoutPosition() {
            every { nativeFunctionExecutor.playAudioStreamForPlayer(any(), any(), any(), any(), any(), any(), any()) } returns true

            player.playAudioStream("http://localhost:8080/song/1")

            verify {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        playerid = playerId.value,
                        url = "http://localhost:8080/song/1",
                        posX = 0f,
                        posY = 0f,
                        posZ = 0f,
                        distance = 0f,
                        usepos = false
                )
            }
        }
    }

    @Test
    fun shouldStopAudioStream() {
        every { nativeFunctionExecutor.stopAudioStreamForPlayer(any()) } returns true

        player.stopAudioStream()

        verify { nativeFunctionExecutor.stopAudioStreamForPlayer(playerId.value) }
    }

    @Test
    fun shouldSetShopName() {
        every { nativeFunctionExecutor.setPlayerShopName(any(), any()) } returns true

        player.setShopName(ShopName.AMMUN2)

        verify { nativeFunctionExecutor.setPlayerShopName(playerid = playerId.value, shopname = ShopName.AMMUN2.value) }
    }

    @Test
    fun shouldSetSkillLevel() {
        every { nativeFunctionExecutor.setPlayerSkillLevel(any(), any(), any()) } returns true

        player.setSkillLevel(WeaponSkill.MICRO_UZI, 456)

        verify {
            nativeFunctionExecutor.setPlayerSkillLevel(
                    playerid = playerId.value,
                    skill = WeaponSkill.MICRO_UZI.value,
                    level = 456
            )
        }
    }

    @Nested
    inner class SurfingVehicleTests {

        @Test
        fun shouldReturnSurfingVehicle() {
            val vehicleId = 1337
            val vehicle = mockk<VehicleImpl>()
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
            val mapObject = mockk<MapObjectImpl>()
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
    fun shouldLastShotVectors() {
        every { nativeFunctionExecutor.getPlayerLastShotVectors(playerId.value, any(), any(), any(), any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            arg<ReferenceFloat>(4).value = 4f
            arg<ReferenceFloat>(5).value = 5f
            arg<ReferenceFloat>(6).value = 6f
            true
        }

        val lastShotVectors = player.lastShotVectors

        assertThat(lastShotVectors)
                .isEqualTo(LastShotVectors(
                        origin = vector3DOf(x = 1f, y = 2f, z = 3f),
                        hitPosition = vector3DOf(x = 4f, y = 5f, z = 6f)
                ))
    }

    @Test
    fun shouldInitializeAttachedObjectSlots() {
        assertThat(player.attachedObjectSlots)
                .hasSize(10)
        player.attachedObjectSlots.forEachIndexed { index, attachedObjectSlot ->
            assertThat(attachedObjectSlot)
                    .isInstanceOfSatisfying(AttachedObjectSlotImpl::class.java) {
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
                .isInstanceOfSatisfying(PlayerVarsImpl::class.java) {
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
            val vehicle = mockk<VehicleImpl>()
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
    fun shouldRemovePlayerFromVehicle() {
        every { nativeFunctionExecutor.removePlayerFromVehicle(playerId.value) } returns true

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

    @ParameterizedTest
    @CsvSource(
            "false, false, false, false, false",
            "true, true, true, true, true",
            "true, false, false, false, false",
            "false, true, false, false, false",
            "false, false, true, false, false",
            "false, false, false, true, false",
            "false, false, false, false, true"
    )
    fun shouldApplyAnimation(loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, forceSync: Boolean) {
        every {
            nativeFunctionExecutor.applyAnimation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns true

        player.applyAnimation(
                animation = Animation.valueOf(library = "ABC", name = "xyz"),
                fDelta = 1f,
                time = 60,
                loop = loop,
                lockX = lockX,
                lockY = lockY,
                freeze = freeze,
                forceSync = forceSync
        )

        verify {
            nativeFunctionExecutor.applyAnimation(
                    animlib = "ABC",
                    animname = "xyz",
                    playerid = playerId.value,
                    fDelta = 1f,
                    time = 60,
                    loop = loop,
                    lockx = lockX,
                    locky = lockY,
                    freeze = freeze,
                    forcesync = forceSync
            )
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldClearAnimation(forceSync: Boolean) {
        every { nativeFunctionExecutor.clearAnimations(any(), any()) } returns true

        player.clearAnimation(forceSync)

        verify { nativeFunctionExecutor.clearAnimations(playerId.value, forceSync) }
    }

    @Test
    fun shouldGetAnimationIndex() {
        every { nativeFunctionExecutor.getPlayerAnimationIndex(playerId.value) } returns 69

        val animationIndex = player.animationIndex

        assertThat(animationIndex)
                .isEqualTo(69)
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
                nativeFunctionExecutor.setPlayerRaceCheckpoint(any(), any(), any(), any(), any(), any(), any(), any(), any())
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
                nativeFunctionExecutor.setPlayerRaceCheckpoint(any(), any(), any(), any(), any(), any(), any(), any(), any())
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
    fun shouldAllowTeleport(allow: Boolean) {
        every { nativeFunctionExecutor.allowPlayerTeleport(any(), any()) } returns true

        player.allowTeleport(allow)

        verify { nativeFunctionExecutor.allowPlayerTeleport(playerId.value, allow) }
    }

    @Nested
    inner class CameraPositionTests {

        @Test
        fun shouldGetCameraPosition() {
            every { nativeFunctionExecutor.getPlayerCameraPos(playerId.value, any(), any(), any()) } answers {
                secondArg<ReferenceFloat>().value = 1f
                thirdArg<ReferenceFloat>().value = 2f
                arg<ReferenceFloat>(3).value = 3f
                true
            }

            val cameraPosition = player.cameraPosition

            assertThat(cameraPosition)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldSetCameraPosition() {
            every { nativeFunctionExecutor.setPlayerCameraPos(any(), any(), any(), any()) } returns true

            player.cameraPosition = vector3DOf(x = 1f, y = 2f, z = 3f)

            verify {
                nativeFunctionExecutor.setPlayerCameraPos(playerid = playerId.value, x = 1f, y = 2f, z = 3f)
            }
        }
    }

    @Test
    fun shouldSetCameraLookAt() {
        every { nativeFunctionExecutor.setPlayerCameraLookAt(any(), any(), any(), any(), any()) } returns true

        player.setCameraLookAt(vector3DOf(x = 1f, y = 2f, z = 3f), CameraType.CUT)

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
    fun shouldSetCameraBehindPlayer() {
        every { nativeFunctionExecutor.setCameraBehindPlayer(any()) } returns true

        player.setCameraBehind()

        verify { nativeFunctionExecutor.setCameraBehindPlayer(playerId.value) }
    }

    @Test
    fun shouldGetCameraFrontVector() {
        every { nativeFunctionExecutor.getPlayerCameraFrontVector(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val cameraFrontVector = player.cameraFrontVector

        assertThat(cameraFrontVector)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldGetCameraMode() {
        every { nativeFunctionExecutor.getPlayerCameraMode(playerId.value) } returns CameraMode.TRAIN_OR_TRAM.value

        val cameraMode = player.cameraMode

        assertThat(cameraMode)
                .isEqualTo(CameraMode.TRAIN_OR_TRAM)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldEnableCameraTarget(enable: Boolean) {
        every { nativeFunctionExecutor.enablePlayerCameraTarget(any(), any()) } returns true

        player.enableCameraTarget(enable)

        verify { nativeFunctionExecutor.enablePlayerCameraTarget(playerId.value, enable) }
    }

    @Nested
    inner class CameraTargetMapObjectTests {

        @Test
        fun shouldReturnCameraTargetMapObject() {
            val mapObjectId = MapObjectId.valueOf(13)
            val mapObject = mockk<MapObjectImpl>()
            every { nativeFunctionExecutor.getPlayerCameraTargetObject(playerId.value) } returns mapObjectId.value
            every { mapObjectRegistry[mapObjectId.value] } returns mapObject

            val cameraTargetMapObject = player.cameraTargetObject

            assertThat(cameraTargetMapObject)
                    .isSameAs(mapObject)
        }

        @Test
        fun givenNoCameraTargetMapObjectItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetObject(playerId.value) } returns SAMPConstants.INVALID_OBJECT_ID
            every { mapObjectRegistry[SAMPConstants.INVALID_OBJECT_ID] } returns null

            val cameraTargetMapObject = player.cameraTargetObject

            assertThat(cameraTargetMapObject)
                    .isNull()
        }
    }

    @Nested
    inner class CameraTargetPlayerTests {

        @Test
        fun shouldReturnTargetPlayer() {
            every { nativeFunctionExecutor.getPlayerCameraTargetPlayer(playerId.value) } returns otherPlayerId.value
            every { playerRegistry[otherPlayerId.value] } returns otherPlayer

            val targetPlayer = player.cameraTargetPlayer

            assertThat(targetPlayer)
                    .isSameAs(otherPlayer)
        }

        @Test
        fun givenNoTargetPlayerItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetPlayer(playerId.value) } returns SAMPConstants.INVALID_PLAYER_ID
            every { playerRegistry[SAMPConstants.INVALID_PLAYER_ID] } returns null

            val targetPlayer = player.cameraTargetPlayer

            assertThat(targetPlayer)
                    .isNull()
        }
    }

    @Nested
    inner class CameraTargetVehicleTests {

        @Test
        fun shouldReturnTargetPlayer() {
            val vehicleId = VehicleId.valueOf(20)
            val vehicle = mockk<VehicleImpl>()
            every { nativeFunctionExecutor.getPlayerCameraTargetVehicle(playerId.value) } returns vehicleId.value
            every { vehicleRegistry[vehicleId.value] } returns vehicle

            val targetVehicle = player.cameraTargetVehicle

            assertThat(targetVehicle)
                    .isSameAs(vehicle)
        }

        @Test
        fun givenNoTargetPlayerItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetVehicle(playerId.value) } returns SAMPConstants.INVALID_VEHICLE_ID
            every { vehicleRegistry[SAMPConstants.INVALID_VEHICLE_ID] } returns null

            val targetVehicle = player.cameraTargetVehicle

            assertThat(targetVehicle)
                    .isNull()
        }
    }

    @Nested
    inner class CameraTargetActorTests {

        @Test
        fun shouldReturnCameraTargetActor() {
            val actorId = ActorId.valueOf(13)
            val actor = mockk<Actor>()
            every { nativeFunctionExecutor.getPlayerCameraTargetActor(playerId.value) } returns actorId.value
            every { actorRegistry[actorId.value] } returns actor

            val cameraTargetActor = player.cameraTargetActor

            assertThat(cameraTargetActor)
                    .isSameAs(actor)
        }

        @Test
        fun givenNoCameraTargetActorItShouldReturnNull() {
            every { nativeFunctionExecutor.getPlayerCameraTargetActor(playerId.value) } returns SAMPConstants.INVALID_ACTOR_ID
            every { actorRegistry[SAMPConstants.INVALID_ACTOR_ID] } returns null

            val cameraTargetActor = player.cameraTargetActor

            assertThat(cameraTargetActor)
                    .isNull()
        }
    }

    @Test
    fun shouldGetCameraAspectRatio() {
        every { nativeFunctionExecutor.getPlayerCameraAspectRatio(playerId.value) } returns 1.234f

        val aspectRatio = player.cameraAspectRatio

        assertThat(aspectRatio)
                .isEqualTo(1.234f)
    }

    @Test
    fun shouldGetCameraZoom() {
        every { nativeFunctionExecutor.getPlayerCameraZoom(playerId.value) } returns 1.234f

        val zoom = player.cameraZoom

        assertThat(zoom)
                .isEqualTo(1.234f)
    }

    @Test
    fun shouldAttachCameraToObject() {
        every { nativeFunctionExecutor.attachCameraToObject(any(), any()) } returns true
        val mapObjectId = MapObjectId.valueOf(13)
        val mapObject = mockk<MapObject> {
            every { id } returns mapObjectId
        }

        player.attachCameraTo(mapObject)

        verify { nativeFunctionExecutor.attachCameraToObject(playerid = playerId.value, objectid = mapObjectId.value) }
    }

    @Test
    fun shouldAttachCameraToPlayerObject() {
        every { nativeFunctionExecutor.attachCameraToPlayerObject(any(), any()) } returns true
        val playerMapObjectId = PlayerMapObjectId.valueOf(13)
        val playerMapObject = mockk<PlayerMapObject> {
            every { id } returns playerMapObjectId
        }

        player.attachCameraTo(playerMapObject)

        verify { nativeFunctionExecutor.attachCameraToPlayerObject(playerid = playerId.value, playerobjectid = playerMapObjectId.value) }
    }

    @Test
    fun shouldInterpolateCameraPosition() {
        every { nativeFunctionExecutor.interpolateCameraPos(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true

        player.interpolateCameraPosition(
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
    fun shouldInterpolateCameraLookAt() {
        every { nativeFunctionExecutor.interpolateCameraLookAt(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true

        player.interpolateCameraLookAt(
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
            every { nativeFunctionExecutor.setPlayerRaceCheckpoint(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true
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
            every { nativeFunctionExecutor.setPlayerRaceCheckpoint(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true
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
            every { nativeFunctionExecutor.setPlayerRaceCheckpoint(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            every { nativeFunctionExecutor.disablePlayerRaceCheckpoint(any()) } returns true
            player.raceCheckpoint = raceCheckpoint

            val result = player.isInRaceCheckpoint(raceCheckpoint)

            assertThat(result)
                    .isEqualTo(expectedResult)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldEnableStuntBonus(enable: Boolean) {
        every { nativeFunctionExecutor.enableStuntBonusForPlayer(any(), any()) } returns true

        player.enableStuntBonus(enable)

        verify { nativeFunctionExecutor.enableStuntBonusForPlayer(playerid = playerId.value, enable = enable) }
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
            every { nativeFunctionExecutor.createExplosionForPlayer(any(), any(), any(), any(), any(), any()) } returns true

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
            every { nativeFunctionExecutor.createExplosionForPlayer(any(), any(), any(), any(), any(), any()) } returns true

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
                .isInstanceOfSatisfying(PlayerNetworkStatisticsImpl::class.java) {
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
    fun shouldEditMapObject() {
        val mapObjectId = MapObjectId.valueOf(1337)
        val mapObject = mockk<MapObject> {
            every { id } returns mapObjectId
        }
        every { nativeFunctionExecutor.editObject(any(), any()) } returns true

        player.editMapObject(mapObject)

        verify { nativeFunctionExecutor.editObject(playerid = playerId.value, objectid = mapObjectId.value) }
    }

    @Test
    fun shouldEditPlayerMapObject() {
        val playerMapObjectId = PlayerMapObjectId.valueOf(1337)
        val playerMapObject = mockk<PlayerMapObject> {
            every { id } returns playerMapObjectId
        }
        every { nativeFunctionExecutor.editPlayerObject(any(), any()) } returns true

        player.editPlayerMapObject(playerMapObject)

        verify { nativeFunctionExecutor.editPlayerObject(playerid = playerId.value, objectid = playerMapObjectId.value) }
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

    @Test
    fun shouldExecuteOnSpawnHandlers() {
        val onSpawn = mockk<Player.() -> Unit>(relaxed = true)
        player.onSpawn(onSpawn)

        player.onSpawn()

        verify { onSpawn.invoke(player) }
    }

    @Test
    fun shouldExecuteOnDeathHandlers() {
        val onDeath = mockk<Player.(Player?, WeaponModel) -> Unit>(relaxed = true)
        player.onDeath(onDeath)

        player.onDeath(killer = otherPlayer, weapon = WeaponModel.TEC9)

        verify { onDeath.invoke(player, otherPlayer, WeaponModel.TEC9) }
    }

    @Nested
    inner class OnDisconnectTests {

        @Test
        fun shouldExecuteOnDisconnectHandlers() {
            val onDisconnect = mockk<PlayerImpl.(DisconnectReason) -> Unit>(relaxed = true)
            player.onDisconnect(onDisconnect)

            player.onDisconnect(DisconnectReason.QUIT)

            verify { onDisconnect.invoke(player, DisconnectReason.QUIT) }
        }

        @Test
        fun shouldSetIsConnectedToFalse() {
            player.onDisconnect(DisconnectReason.QUIT)

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

            player.onDisconnect(DisconnectReason.QUIT)

            verify {
                mapIcon1.destroy()
                mapIcon2.destroy()
            }
        }

        @Test
        fun shouldNotExecuteTwice() {
            val onDisconnect = mockk<Player.(DisconnectReason) -> Unit>(relaxed = true)
            player.onDisconnect(onDisconnect)

            player.onDisconnect(DisconnectReason.QUIT)
            player.onDisconnect(DisconnectReason.QUIT)

            verify(exactly = 1) {
                onDisconnect.invoke(player, DisconnectReason.QUIT)
            }
        }

        @Test
        fun givenPlayerDisconnectedIdShouldThrowException() {
            player.onDisconnect(DisconnectReason.QUIT)

            val caughtThrowable = catchThrowable { player.id }

            assertThat(caughtThrowable)
                    .isInstanceOf(PlayerOfflineException::class.java)
        }
    }
}
