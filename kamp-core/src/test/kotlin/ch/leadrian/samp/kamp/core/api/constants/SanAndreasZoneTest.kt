package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SanAndreasZoneTest {

    @Nested
    inner class GetZoneTests {

        @Nested
        inner class Area3DTests {

            @Test
            fun shouldReturnZone() {
                val coordinates = vector3DOf(x = 850f, y = -1200f, z = 15f)

                val zone = SanAndreasZone.getZone(coordinates)

                assertThat(zone)
                        .isNotNull
                        .satisfies {
                            assertThat(it!!.name)
                                    .isEqualTo("Vinewood")
                        }
            }

            @Test
            fun givenZCoordinateForValidZoneIsOutsideOfRangeItShouldReturnNull() {
                val coordinates = vector3DOf(x = 850f, y = -1200f, z = 9999f)

                val zone = SanAndreasZone.getZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

            @Test
            fun givenCoordinatesOutsideOfSanAndreasItShouldReturnNull() {
                val coordinates = vector3DOf(x = 9999f, y = 9999f, z = 9999f)

                val zone = SanAndreasZone.getZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

        }

        @Nested
        inner class Area2DTests {

            @Test
            fun shouldReturnZone() {
                val coordinates = vector2DOf(x = 850f, y = -1200f)

                val zone = SanAndreasZone.getZone(coordinates)

                assertThat(zone)
                        .isNotNull
                        .satisfies {
                            assertThat(it!!.name)
                                    .isEqualTo("Vinewood")
                        }
            }

            @Test
            fun givenCoordinatesOutsideOfSanAndreasItShouldReturnNull() {
                val coordinates = vector2DOf(x = 9999f, y = 9999f)

                val zone = SanAndreasZone.getZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

        }

    }

    @Nested
    inner class GetMainZoneTests {

        @Nested
        inner class Area3DTests {

            @Test
            fun shouldReturnZone() {
                val coordinates = vector3DOf(x = 850f, y = -1200f, z = 15f)

                val zone = SanAndreasZone.getMainZone(coordinates)

                assertThat(zone)
                        .isNotNull
                        .satisfies {
                            assertThat(it!!.name)
                                    .isEqualTo("Los Santos")
                        }
            }

            @Test
            fun givenZCoordinateForValidZoneIsOutsideOfRangeItShouldReturnNull() {
                val coordinates = vector3DOf(x = 850f, y = -1200f, z = 9999f)

                val zone = SanAndreasZone.getMainZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

            @Test
            fun givenCoordinatesOutsideOfSanAndreasItShouldReturnNull() {
                val coordinates = vector3DOf(x = 9999f, y = 9999f, z = 9999f)

                val zone = SanAndreasZone.getMainZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

        }

        @Nested
        inner class Area2DTests {

            @Test
            fun shouldReturnZone() {
                val coordinates = vector2DOf(x = 850f, y = -1200f)

                val zone = SanAndreasZone.getMainZone(coordinates)

                assertThat(zone)
                        .isNotNull
                        .satisfies {
                            assertThat(it!!.name)
                                    .isEqualTo("Los Santos")
                        }
            }

            @Test
            fun givenCoordinatesOutsideOfSanAndreasItShouldReturnNull() {
                val coordinates = vector2DOf(x = 9999f, y = 9999f)

                val zone = SanAndreasZone.getMainZone(coordinates)

                assertThat(zone)
                        .isNull()
            }

        }

    }

}