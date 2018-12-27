package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SkinModelCommandParameterResolverTest {

    private val skinModelCommandParameterResolver = SkinModelCommandParameterResolver()

    @Test
    fun givenValidModelIdItShouldReturnSkinModel() {
        val skinModel = skinModelCommandParameterResolver.resolve("102")

        assertThat(skinModel)
                .isEqualTo(SkinModel.BALLAS1)
    }

    @Test
    fun givenInvalidModelIdItShouldReturnNull() {
        val skinModel = skinModelCommandParameterResolver.resolve("999999")

        assertThat(skinModel)
                .isNull()
    }

    @Test
    fun givenValidSkinModelNameItShouldReturnSkinModel() {
        val skinModel = skinModelCommandParameterResolver.resolve("Colonel Fuhrberger")

        assertThat(skinModel)
                .isEqualTo(SkinModel.WMOPJ)
    }

    @Test
    fun givenSkinModelEnumNameItShouldReturnSkinModel() {
        val skinModel = skinModelCommandParameterResolver.resolve("WBDYG1")

        assertThat(skinModel)
                .isEqualTo(SkinModel.WBDYG1)
    }

    @Test
    fun givenInvalidSkinModelNameItShouldReturnNull() {
        val skinModel = skinModelCommandParameterResolver.resolve("hahaha")

        assertThat(skinModel)
                .isNull()
    }

}