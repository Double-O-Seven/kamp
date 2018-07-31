package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.WeaponModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class WeaponDataTest {

    @Test
    fun toWeaponDataShouldReturnSameInstance() {
        val weaponModel = WeaponModel.AK47
        val ammo = 37
        val expectedWeaponData = weaponDataOf(
                model = weaponModel,
                ammo = ammo
        )

        val weaponData = expectedWeaponData.toWeaponData()

        Assertions.assertThat(weaponData)
                .isSameAs(expectedWeaponData)
    }

    @Test
    fun toMutableWeaponDataShouldReturnMutableWeaponData() {
        val weaponModel = WeaponModel.AK47
        val ammo = 37
        val weaponData = weaponDataOf(
                model = weaponModel,
                ammo = ammo
        )

        val mutableWeaponData = weaponData.toMutableWeaponData()

        Assertions.assertThat(mutableWeaponData)
                .satisfies {
                    Assertions.assertThat(it.model)
                            .isEqualTo(weaponModel)
                    Assertions.assertThat(it.ammo)
                            .isEqualTo(ammo)
                }
    }

}