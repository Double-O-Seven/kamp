package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.mutablePositionOf
import ch.leadrian.samp.kamp.core.api.data.mutableSpawnInfoOf
import ch.leadrian.samp.kamp.core.api.data.mutableWeaponDataOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.spawnInfoOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerClassTest {

    @Test
    fun shouldCreatePlayerClass() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                addPlayerClassEx(
                        teamid = 13,
                        modelid = SkinModel.ARMY.value,
                        spawn_x = 1f,
                        spawn_y = 2f,
                        spawn_z = 3f,
                        z_angle = 90f,
                        weapon1 = WeaponModel.AK47.value,
                        weapon1_ammo = 450,
                        weapon2 = WeaponModel.M4.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.TEC9.value,
                        weapon3_ammo = 200
                )
            } returns 127
        }
        val spawnInfo = mutableSpawnInfoOf(
                teamId = TeamId.valueOf(13),
                skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                position = mutablePositionOf(1f, 2f, 3f, 90f),
                weapon1 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450),
                weapon2 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300),
                weapon3 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
        )

        val playerClass = PlayerClass(spawnInfo, nativeFunctionExecutor)

        assertThat(playerClass)
                .satisfies {
                    assertThat(it.id)
                            .isEqualTo(PlayerClassId.valueOf(127))
                    assertThat(it.spawnInfo)
                            .isEqualTo(spawnInfoOf(
                                    teamId = TeamId.valueOf(13),
                                    skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                                    position = positionOf(1f, 2f, 3f, 90f),
                                    weapon1 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450),
                                    weapon2 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300),
                                    weapon3 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
                            ))
                }
    }

    @Test
    fun givenNoTeamItShouldCreatePlayerClass() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                addPlayerClass(
                        modelid = SkinModel.ARMY.value,
                        spawn_x = 1f,
                        spawn_y = 2f,
                        spawn_z = 3f,
                        z_angle = 90f,
                        weapon1 = WeaponModel.AK47.value,
                        weapon1_ammo = 450,
                        weapon2 = WeaponModel.M4.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.TEC9.value,
                        weapon3_ammo = 200
                )
            } returns 127
        }
        val spawnInfo = mutableSpawnInfoOf(
                skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                position = mutablePositionOf(1f, 2f, 3f, 90f),
                weapon1 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450),
                weapon2 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300),
                weapon3 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
        )

        val playerClass = PlayerClass(spawnInfo, nativeFunctionExecutor)

        assertThat(playerClass)
                .satisfies {
                    assertThat(it.id)
                            .isEqualTo(PlayerClassId.valueOf(127))
                    assertThat(it.spawnInfo)
                            .isEqualTo(spawnInfoOf(
                                    skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                                    position = positionOf(1f, 2f, 3f, 90f),
                                    weapon1 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450),
                                    weapon2 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300),
                                    weapon3 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
                            ))
                }
    }
}