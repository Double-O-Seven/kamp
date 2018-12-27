package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class SkinModelTest {

    @ParameterizedTest
    @ValueSource(strings = ["Maccer", "maccer", "MACCER"])
    fun shouldReturnExactlyMatchingSkinModel(modelName: String) {
        val skinModel = SkinModel[modelName]

        assertThat(skinModel)
                .isEqualTo(SkinModel.MACCER)
    }

    @Test
    fun givenExactlyOnePartialMatchItShouldReturnIt() {
        val skinModel = SkinModel["mac"]

        assertThat(skinModel)
                .isEqualTo(SkinModel.MACCER)
    }

    @Test
    fun givenNoMatchItShouldReturnNull() {
        val skinModel = SkinModel["hahaha"]

        assertThat(skinModel)
                .isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["Normal Ped", "Golfer"])
    fun givenMultipleMatchesItShouldReturnNull(modelName: String) {
        val skinModel = SkinModel[modelName]

        assertThat(skinModel)
                .isNull()
    }

}