package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerClassImplTest {

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
                skinModel = SkinModel.ARMY,
                position = mutablePositionOf(1f, 2f, 3f, 90f),
                weapon1 = mutableWeaponDataOf(WeaponModel.AK47, 450),
                weapon2 = mutableWeaponDataOf(WeaponModel.M4, 300),
                weapon3 = mutableWeaponDataOf(WeaponModel.TEC9, 200)
        )

        val playerClass = PlayerClassImpl(spawnInfo, nativeFunctionExecutor)

        assertThat(playerClass)
                .satisfies {
                    assertThat(it.id)
                            .isEqualTo(PlayerClassId.valueOf(127))
                    assertThat(it.spawnInfo)
                            .isEqualTo(spawnInfoOf(
                                    teamId = TeamId.valueOf(13),
                                    skinModel = SkinModel.ARMY,
                                    position = positionOf(1f, 2f, 3f, 90f),
                                    weapon1 = weaponDataOf(WeaponModel.AK47, 450),
                                    weapon2 = weaponDataOf(WeaponModel.M4, 300),
                                    weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
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
                skinModel = SkinModel.ARMY,
                position = mutablePositionOf(1f, 2f, 3f, 90f),
                weapon1 = mutableWeaponDataOf(WeaponModel.AK47, 450),
                weapon2 = mutableWeaponDataOf(WeaponModel.M4, 300),
                weapon3 = mutableWeaponDataOf(WeaponModel.TEC9, 200)
        )

        val playerClass = PlayerClassImpl(spawnInfo, nativeFunctionExecutor)

        assertThat(playerClass)
                .satisfies {
                    assertThat(it.id)
                            .isEqualTo(PlayerClassId.valueOf(127))
                    assertThat(it.spawnInfo)
                            .isEqualTo(spawnInfoOf(
                                    skinModel = SkinModel.ARMY,
                                    position = positionOf(1f, 2f, 3f, 90f),
                                    weapon1 = weaponDataOf(WeaponModel.AK47, 450),
                                    weapon2 = weaponDataOf(WeaponModel.M4, 300),
                                    weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
                            ))
                }
    }
}