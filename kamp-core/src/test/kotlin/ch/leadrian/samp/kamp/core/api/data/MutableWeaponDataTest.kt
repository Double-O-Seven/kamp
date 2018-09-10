package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MutableWeaponDataTest {

    @Test
    fun toWeaponDataShouldReturnWeaponData() {
        val weaponModel = ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47
        val ammo = 37
        val mutableWeaponData = mutableWeaponDataOf(
                model = weaponModel,
                ammo = ammo
        )

        val weaponData = mutableWeaponData.toWeaponData()

        assertThat(weaponData)
                .isNotInstanceOf(MutableWeaponData::class.java)
                .satisfies {
                    assertThat(it.model)
                            .isEqualTo(weaponModel)
                    assertThat(it.ammo)
                            .isEqualTo(ammo)
                }
    }

    @Test
    fun toMutableWeaponDataShouldReturnSameInstance() {
        val weaponModel = ch.leadrian.samp.kamp.core.api.constants.WeaponModel.AK47
        val ammo = 37
        val expectedMutableWeaponData = mutableWeaponDataOf(
                model = weaponModel,
                ammo = ammo
        )

        val mutableWeaponData = expectedMutableWeaponData.toMutableWeaponData()

        assertThat(mutableWeaponData)
                .isSameAs(expectedMutableWeaponData)
    }
}