package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MutableSpawnInfoTest {

    @Test
    fun toSpawnInfoShouldReturnSpawnInfo() {
        val teamId = TeamId.valueOf(1234)
        val skinModel = SkinModel.ARMY
        val position = positionOf(1f, 2f, 3f, 90f)
        val weapon1 = weaponDataOf(WeaponModel.AK47, 450)
        val weapon2 = weaponDataOf(WeaponModel.M4, 300)
        val weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
        val mutableSpawnInfo = mutableSpawnInfoOf(
                teamId = teamId,
                skinModel = skinModel,
                position = position,
                weapon1 = weapon1,
                weapon2 = weapon2,
                weapon3 = weapon3
        )

        val spawnInfo = mutableSpawnInfo.toSpawnInfo()

        assertThat(spawnInfo)
                .isNotInstanceOf(MutableSpawnInfo::class.java)
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

    @Test
    fun toMutableSpawnInfoShouldReturnSameInstance() {
        val teamId = TeamId.valueOf(1234)
        val skinModel = SkinModel.ARMY
        val position = positionOf(1f, 2f, 3f, 90f)
        val weapon1 = weaponDataOf(WeaponModel.AK47, 450)
        val weapon2 = weaponDataOf(WeaponModel.M4, 300)
        val weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
        val expectedMutableSpawnInfo = mutableSpawnInfoOf(
                teamId = teamId,
                skinModel = skinModel,
                position = position,
                weapon1 = weapon1,
                weapon2 = weapon2,
                weapon3 = weapon3
        )

        val mutableSpawnInfo = expectedMutableSpawnInfo.toMutableSpawnInfo()

        assertThat(mutableSpawnInfo)
                .isSameAs(expectedMutableSpawnInfo)
    }

}