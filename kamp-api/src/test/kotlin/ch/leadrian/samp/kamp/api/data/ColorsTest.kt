package ch.leadrian.samp.kamp.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ColorsTest {

    @Nested
    inner class ColorTests {

        @Test
        fun shouldCreateColorWithValue() {
            val color = colorOf(0xAABBCCDD.toInt())

            assertThat(color)
                    .isNotInstanceOf(MutableColor::class.java)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(0xAABBCCDD.toInt())
                        assertThat(it.r)
                                .isEqualTo(0xAA)
                        assertThat(it.g)
                                .isEqualTo(0xBB)
                        assertThat(it.b)
                                .isEqualTo(0xCC)
                        assertThat(it.a)
                                .isEqualTo(0xDD)
                        assertThat(it.rgb)
                                .isEqualTo(0xAABBCC)
                    }
        }

        @Test
        fun shouldCreateColorWithRgba() {
            val color = colorOf(r = 0xAA, g = 0xBB, b = 0xCC, a = 0xDD)

            assertThat(color)
                    .isNotInstanceOf(MutableColor::class.java)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(0xAABBCCDD.toInt())
                        assertThat(it.r)
                                .isEqualTo(0xAA)
                        assertThat(it.g)
                                .isEqualTo(0xBB)
                        assertThat(it.b)
                                .isEqualTo(0xCC)
                        assertThat(it.a)
                                .isEqualTo(0xDD)
                        assertThat(it.rgb)
                                .isEqualTo(0xAABBCC)
                    }
        }

        @Test
        fun shouldReturnHexString() {
            val color = colorOf(0x0ABBCCDD)

            val hexString = color.toHexString()

            assertThat(hexString)
                    .isEqualTo("0abbccdd")
        }

        @Test
        fun shouldReturnEmbeddedString() {
            val color = colorOf(0x0ABBCCDD)

            val hexString = color.toEmbeddedString()

            assertThat(hexString)
                    .isEqualTo("{0abbcc}")
        }
    }

    @Nested
    inner class MutableColorTests {

        @Test
        fun shouldCreateMutableColorWithValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            assertThat(mutableColor)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(0xAABBCCDD.toInt())
                        assertThat(it.r)
                                .isEqualTo(0xAA)
                        assertThat(it.g)
                                .isEqualTo(0xBB)
                        assertThat(it.b)
                                .isEqualTo(0xCC)
                        assertThat(it.a)
                                .isEqualTo(0xDD)
                        assertThat(it.rgb)
                                .isEqualTo(0xAABBCC)
                    }
        }

        @Test
        fun shouldCreateMutableColorWithRgba() {
            val mutableColor = mutableColorOf(r = 0xAA, g = 0xBB, b = 0xCC, a = 0xDD)

            assertThat(mutableColor)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(0xAABBCCDD.toInt())
                        assertThat(it.r)
                                .isEqualTo(0xAA)
                        assertThat(it.g)
                                .isEqualTo(0xBB)
                        assertThat(it.b)
                                .isEqualTo(0xCC)
                        assertThat(it.a)
                                .isEqualTo(0xDD)
                        assertThat(it.rgb)
                                .isEqualTo(0xAABBCC)
                    }
        }

        @Test
        fun shouldReturnHexString() {
            val mutableColor = mutableColorOf(0x0ABBCCDD)

            val hexString = mutableColor.toHexString()

            assertThat(hexString)
                    .isEqualTo("0abbccdd")
        }

        @Test
        fun shouldReturnEmbeddedString() {
            val mutableColor = mutableColorOf(0x0ABBCCDD)

            val hexString = mutableColor.toEmbeddedString()

            assertThat(hexString)
                    .isEqualTo("{0abbcc}")
        }

        @Test
        fun shouldSetRedValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            mutableColor.r = 0x33

            assertThat(mutableColor.value)
                    .isEqualTo(0x33BBCCDD)
        }

        @Test
        fun shouldSetGreenValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            mutableColor.g = 0x33

            assertThat(mutableColor.value)
                    .isEqualTo(0xAA33CCDD.toInt())
        }

        @Test
        fun shouldSetBlueValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            mutableColor.b = 0x33

            assertThat(mutableColor.value)
                    .isEqualTo(0xAABB33DD.toInt())
        }

        @Test
        fun shouldSetAlphaValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            mutableColor.a = 0x33

            assertThat(mutableColor.value)
                    .isEqualTo(0xAABBCC33.toInt())
        }

        @Test
        fun shouldSetRgbValue() {
            val mutableColor = mutableColorOf(0xAABBCCDD.toInt())

            mutableColor.rgb = 0x112233

            assertThat(mutableColor.value)
                    .isEqualTo(0x112233DD)
        }
    }
}