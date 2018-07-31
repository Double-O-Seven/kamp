package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.WeaponModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MutableWeaponDataTest {

    @Test
    fun toWeaponDataShouldReturnWeaponData() {
        val weaponModel = WeaponModel.AK47
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
        val weaponModel = WeaponModel.AK47
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