package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class WeaponModelTest {

    @ParameterizedTest
    @ValueSource(strings = ["Shotgun", "shotgun", "SHOTGUN"])
    fun shouldReturnExactlyMatchingWeaponModel(modelName: String) {
        val weaponModel = WeaponModel[modelName]

        assertThat(weaponModel)
                .isEqualTo(WeaponModel.SHOTGUN)
    }

    @Test
    fun givenExactlyOnePartialMatchItShouldReturnIt() {
        val weaponModel = WeaponModel["shot"]

        assertThat(weaponModel)
                .isEqualTo(WeaponModel.SHOTGUN)
    }

    @Test
    fun givenNoMatchItShouldReturnNull() {
        val weaponModel = WeaponModel["hahaha"]

        assertThat(weaponModel)
                .isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["Dildo", "Vibrator"])
    fun givenMultipleMatchesItShouldReturnNull(modelName: String) {
        val weaponModel = WeaponModel[modelName]

        assertThat(weaponModel)
                .isNull()
    }

}