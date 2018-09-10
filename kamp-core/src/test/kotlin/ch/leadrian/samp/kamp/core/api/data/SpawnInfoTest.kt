package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SpawnInfoTest {

    @Test
    fun spawnInfoOfShouldOnlyHaveImmutableMemberValues() {
        val teamId = TeamId.valueOf(1234)
        val skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY

        val spawnInfo = spawnInfoOf(
                teamId = teamId,
                skinModel = skinModel,
                position = mutablePositionOf(1f, 2f, 3f, 90f),
                weapon1 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450),
                weapon2 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300),
                weapon3 = mutableWeaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
        )

        assertThat(spawnInfo)
                .satisfies {
                    assertThat(it.position)
                            .isEqualTo(positionOf(1f, 2f, 3f, 90f))
                    assertThat(it.weapon1)
                            .isEqualTo(weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450))
                    assertThat(it.weapon2)
                            .isEqualTo(weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300))
                    assertThat(it.weapon3)
                            .isEqualTo(weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200))
                }
    }

    @Test
    fun toSpawnInfoShouldReturnSameInstance() {
        val teamId = TeamId.valueOf(1234)
        val skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY
        val position = positionOf(1f, 2f, 3f, 90f)
        val weapon1 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450)
        val weapon2 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300)
        val weapon3 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
        val expectedSpawnInfo = spawnInfoOf(
                teamId = teamId,
                skinModel = skinModel,
                position = position,
                weapon1 = weapon1,
                weapon2 = weapon2,
                weapon3 = weapon3
        )

        val spawnInfo = expectedSpawnInfo.toSpawnInfo()

        assertThat(spawnInfo)
                .isSameAs(expectedSpawnInfo)
    }

    @Test
    fun toMutableSpawnInfoShouldReturnMutableSpawnInfo() {
        val teamId = TeamId.valueOf(1234)
        val skinModel = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY
        val position = positionOf(1f, 2f, 3f, 90f)
        val weapon1 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47, 450)
        val weapon2 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.M4, 300)
        val weapon3 = weaponDataOf(ch.leadrian.samp.kamp.core.api.constants.WeaponModel.TEC9, 200)
        val spawnInfo = spawnInfoOf(
                teamId = teamId,
                skinModel = skinModel,
                position = position,
                weapon1 = weapon1,
                weapon2 = weapon2,
                weapon3 = weapon3
        )

        val mutableSpawnInfo = spawnInfo.toMutableSpawnInfo()

        assertThat(mutableSpawnInfo)
                .satisfies {
                    assertThat(it.teamId)
                            .isEqualTo(teamId)
                    assertThat(it.skinModel)
                            .isEqualTo(skinModel)
                    assertThat(it.position)
                            .isEqualTo(position)
                    assertThat(it.weapon1)
                            .isEqualTo(weapon1)
                    assertThat(it.weapon2)
                            .isEqualTo(weapon2)
                    assertThat(it.weapon3)
                            .isEqualTo(weapon3)
                }
    }

}