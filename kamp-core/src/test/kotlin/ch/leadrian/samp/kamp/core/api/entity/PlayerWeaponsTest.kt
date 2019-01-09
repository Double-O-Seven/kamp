package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponSkill
import ch.leadrian.samp.kamp.core.api.constants.WeaponSlot
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerWeaponsTest {

    private lateinit var playerWeapons: PlayerWeapons

    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerWeapons = PlayerWeapons(player, nativeFunctionExecutor)
    }

    @Nested
    inner class ArmedTests {

        @Test
        fun shouldGetArmedWeapon() {
            every { nativeFunctionExecutor.getPlayerWeapon(playerId.value) } returns WeaponModel.TEC9.value

            val armedWeapon = playerWeapons.armed

            assertThat(armedWeapon)
                    .isEqualTo(WeaponModel.TEC9)
        }

        @Test
        fun shouldSetArmedWeapon() {
            every { nativeFunctionExecutor.setPlayerArmedWeapon(any(), any()) } returns true

            playerWeapons.armed = WeaponModel.TEC9

            verify {
                nativeFunctionExecutor.setPlayerArmedWeapon(
                        playerid = playerId.value,
                        weaponid = WeaponModel.TEC9.value
                )
            }
        }
    }

    @Test
    fun shouldGiveWeapon() {
        every { nativeFunctionExecutor.givePlayerWeapon(any(), any(), any()) } returns true

        playerWeapons.give(WeaponModel.TEC9, 1337)

        verify {
            nativeFunctionExecutor.givePlayerWeapon(
                    playerid = playerId.value,
                    weaponid = WeaponModel.TEC9.value,
                    ammo = 1337
            )
        }
    }

    @Test
    fun shouldSetAmmo() {
        every { nativeFunctionExecutor.setPlayerAmmo(any(), any(), any()) } returns true

        playerWeapons.setAmmo(WeaponModel.TEC9, 1337)

        verify {
            nativeFunctionExecutor.setPlayerAmmo(
                    playerid = playerId.value,
                    weaponid = WeaponModel.TEC9.value,
                    ammo = 1337
            )
        }
    }

    @Test
    fun shouldGetAmmo() {
        every { nativeFunctionExecutor.getPlayerAmmo(playerId.value) } returns 150

        val ammo = playerWeapons.ammo

        assertThat(ammo)
                .isEqualTo(150)
    }

    @Test
    fun shouldGetWeaponState() {
        every { nativeFunctionExecutor.getPlayerWeaponState(playerId.value) } returns WeaponState.MORE_BULLETS.value

        val weaponState = playerWeapons.state

        assertThat(weaponState)
                .isEqualTo(WeaponState.MORE_BULLETS)
    }

    @Test
    fun shouldResetWeapons() {
        every { nativeFunctionExecutor.resetPlayerWeapons(any()) } returns true

        playerWeapons.reset()

        verify { nativeFunctionExecutor.resetPlayerWeapons(playerId.value) }
    }

    @Test
    fun shouldGetWeaponData() {
        every {
            nativeFunctionExecutor.getPlayerWeaponData(
                    playerId.value,
                    WeaponSlot.MACHINE_PISTOL.value,
                    any(),
                    any()
            )
        } answers {
            thirdArg<ReferenceInt>().value = WeaponModel.TEC9.value
            arg<ReferenceInt>(3).value = 150
            true
        }

        val weaponData = playerWeapons[WeaponSlot.MACHINE_PISTOL]

        assertThat(weaponData)
                .isEqualTo(weaponDataOf(WeaponModel.TEC9, 150))
    }

    @Test
    fun shouldSetSkillLevel() {
        every { nativeFunctionExecutor.setPlayerSkillLevel(any(), any(), any()) } returns true

        playerWeapons.setSkillLevel(WeaponSkill.MICRO_UZI, 456)

        verify {
            nativeFunctionExecutor.setPlayerSkillLevel(
                    playerid = playerId.value,
                    skill = WeaponSkill.MICRO_UZI.value,
                    level = 456
            )
        }
    }
}