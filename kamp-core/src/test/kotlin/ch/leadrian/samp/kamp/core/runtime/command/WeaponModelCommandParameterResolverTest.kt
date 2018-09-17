package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WeaponModelCommandParameterResolverTest {

    private val weaponModelCommandParameterResolver = WeaponModelCommandParameterResolver()

    @Test
    fun givenValidModelIdItShouldReturnWeaponModel() {
        val weaponModel = weaponModelCommandParameterResolver.resolve(SAMPConstants.WEAPON_AK47.toString())

        assertThat(weaponModel)
                .isEqualTo(WeaponModel.AK47)
    }

    @Test
    fun givenInvalidModelIdItShouldReturnNull() {
        val weaponModel = weaponModelCommandParameterResolver.resolve("999999")

        assertThat(weaponModel)
                .isNull()
    }

    @Test
    fun givenValidWeaponModelNameItShouldReturnWeaponModel() {
        val weaponModel = weaponModelCommandParameterResolver.resolve("Sniper rifle")

        assertThat(weaponModel)
                .isEqualTo(WeaponModel.SNIPER_RIFLE)
    }

    @Test
    fun givenWeaponModelEnumNameItShouldReturnWeaponModel() {
        val weaponModel = weaponModelCommandParameterResolver.resolve("GOLF_CLUB")

        assertThat(weaponModel)
                .isEqualTo(WeaponModel.GOLF_CLUB)
    }

    @Test
    fun givenInvalidWeaponModelNameItShouldReturnNull() {
        val weaponModel = weaponModelCommandParameterResolver.resolve("hahaha")

        assertThat(weaponModel)
                .isNull()
    }

}