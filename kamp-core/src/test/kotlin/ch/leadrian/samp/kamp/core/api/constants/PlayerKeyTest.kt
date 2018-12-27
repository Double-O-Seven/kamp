package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerKeyTest {

    @Test
    fun shouldGetOnFootTextDrawCode() {
        val textDrawCode = PlayerKey.FIRE.onFootTextDrawCode

        assertThat(textDrawCode)
                .isEqualTo("~k~~PED_FIREWEAPON~")
    }

    @Test
    fun shouldGetInVehicleTextDrawCode() {
        val textDrawCode = PlayerKey.JUMP.inVehicleTextDrawCode

        assertThat(textDrawCode)
                .isEqualTo("~k~~VEHICLE_BRAKE~")
    }

    @Test
    fun givenNoOnFootTextDrawCodeItShouldReturnQuestionMarks() {
        val textDrawCode = PlayerKey.LOOK_RIGHT.onFootTextDrawCode

        assertThat(textDrawCode)
                .isEqualTo("???")
    }

    @Test
    fun givenNoInVehicleTextDrawCodeItShouldReturnQuestionMarks() {
        val textDrawCode = PlayerKey.WALK.inVehicleTextDrawCode

        assertThat(textDrawCode)
                .isEqualTo("???")
    }

}