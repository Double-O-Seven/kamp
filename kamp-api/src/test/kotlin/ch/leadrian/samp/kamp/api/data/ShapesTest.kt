package ch.leadrian.samp.kamp.api.data

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage.withPercentage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class ShapesTest {

    @Nested
    inner class RectangleTests {

        @ParameterizedTest
        @ArgumentsSource(RectangleFactoryArgumentsProvider::class)
        fun shouldHaveExpectedArea(factory: (Float, Float, Float, Float) -> Rectangle) {
            val rectangle = factory(-1f, 2f, 5f, 10f)

            val area = rectangle.area

            assertThat(area)
                    .isCloseTo(15f, withPercentage(0.0001))
        }

        @ParameterizedTest
        @ArgumentsSource(RectangleFactoryArgumentsProvider::class)
        fun toRectangleShouldReturnImmutableRectangle(factory: (Float, Float, Float, Float) -> Rectangle) {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val rectangle = factory(minX, maxX, minY, maxY)

            val immutableRectangle: Rectangle = rectangle.toRectangle()

            assertThat(immutableRectangle)
                    .isNotInstanceOf(MutableRectangle::class.java)
                    .satisfies {
                        assertThat(it.minX)
                                .isEqualTo(minX)
                        assertThat(it.maxX)
                                .isEqualTo(maxX)
                        assertThat(it.minY)
                                .isEqualTo(minY)
                        assertThat(it.maxY)
                                .isEqualTo(maxY)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(RectangleFactoryArgumentsProvider::class)
        fun toMutableRectangleShouldReturnMutableRectangle(factory: (Float, Float, Float, Float) -> Rectangle) {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val rectangle = factory(minX, maxX, minY, maxY)

            val mutableRectangle: MutableRectangle = rectangle.toMutableRectangle()

            assertThat(mutableRectangle)
                    .satisfies {
                        assertThat(it.minX)
                                .isEqualTo(minX)
                        assertThat(it.maxX)
                                .isEqualTo(maxX)
                        assertThat(it.minY)
                                .isEqualTo(minY)
                        assertThat(it.maxY)
                                .isEqualTo(maxY)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(RectangleContainsArgumentsProvider::class)
        fun shouldContainCoordinates(rectangle: Rectangle, coordinates: Vector2D) {
            val contains = rectangle.contains(coordinates)

            assertThat(contains)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(RectangleDoesNotContainArgumentsProvider::class)
        fun shouldNotContainCoordinates(rectangle: Rectangle, coordinates: Vector2D) {
            val contains = rectangle.contains(coordinates)

            assertThat(contains)
                    .isFalse()
        }
    }


    @Nested
    inner class BoxTests {

        @ParameterizedTest
        @ArgumentsSource(BoxFactoryArgumentsProvider::class)
        fun shouldHaveExpectedArea(factory: (Float, Float, Float, Float, Float, Float) -> Box) {
            val box = factory(-1f, 2f, 5f, 10f, -20f, 50f)

            val area = box.volume

            assertThat(area)
                    .isCloseTo(1050f, withPercentage(0.0001))
        }


        @ParameterizedTest
        @ArgumentsSource(BoxFactoryArgumentsProvider::class)
        fun toBoxShouldReturnImmutableBox(factory: (Float, Float, Float, Float, Float, Float) -> Box) {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val minZ = -20f
            val maxZ = 50f
            val box = factory(minX, maxX, minY, maxY, minZ, maxZ)

            val immutableBox: Box = box.toBox()

            assertThat(immutableBox)
                    .isNotInstanceOf(MutableBox::class.java)
                    .satisfies {
                        assertThat(it.minX)
                                .isEqualTo(minX)
                        assertThat(it.maxX)
                                .isEqualTo(maxX)
                        assertThat(it.minY)
                                .isEqualTo(minY)
                        assertThat(it.maxY)
                                .isEqualTo(maxY)
                        assertThat(it.minZ)
                                .isEqualTo(minZ)
                        assertThat(it.maxZ)
                                .isEqualTo(maxZ)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(BoxFactoryArgumentsProvider::class)
        fun toMutableBoxShouldReturnMutableBox(factory: (Float, Float, Float, Float, Float, Float) -> Box) {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val minZ = -20f
            val maxZ = 50f
            val box = factory(minX, maxX, minY, maxY, minZ, maxZ)

            val mutableBox: MutableBox = box.toMutableBox()

            assertThat(mutableBox)
                    .satisfies {
                        assertThat(it.minX)
                                .isEqualTo(minX)
                        assertThat(it.maxX)
                                .isEqualTo(maxX)
                        assertThat(it.minY)
                                .isEqualTo(minY)
                        assertThat(it.maxY)
                                .isEqualTo(maxY)
                        assertThat(it.minZ)
                                .isEqualTo(minZ)
                        assertThat(it.maxZ)
                                .isEqualTo(maxZ)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(BoxContainsArgumentsProvider::class)
        fun shouldContainCoordinates(box: Box, coordinates: Vector3D) {
            val contains = box.contains(coordinates)

            assertThat(contains)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(BoxDoesNotContainArgumentsProvider::class)
        fun shouldNotContainCoordinates(box: Box, coordinates: Vector3D) {
            val contains = box.contains(coordinates)

            assertThat(contains)
                    .isFalse()
        }
    }

    private class RectangleFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        create { minX, maxX, minY, maxY -> rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY) },
                        create { minX, maxX, minY, maxY -> mutableRectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY) }
                )

        fun create(factory: (Float, Float, Float, Float) -> Rectangle): Arguments = Arguments.of(factory)

    }

    private class RectangleContainsArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            return Stream.of(
                    vector2DOf(x = 0f, y = 6f),
                    vector2DOf(x = minX, y = minY),
                    vector2DOf(x = minX, y = maxY),
                    vector2DOf(x = maxX, y = minY),
                    vector2DOf(x = maxX, y = maxY)
            ).flatMap { coordinates ->
                Stream.of(
                        rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY),
                        mutableRectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY)
                ).map { rectangle -> Arguments.of(rectangle, coordinates) }
            }
        }

    }

    private class RectangleDoesNotContainArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            return Stream.of(
                    vector2DOf(x = -1.1f, y = 7f),
                    vector2DOf(x = 2.1f, y = 7f),
                    vector2DOf(x = 0f, y = 4.9f),
                    vector2DOf(x = 0f, y = 10.2f),
                    vector2DOf(x = -1.1f, y = 4.9f),
                    vector2DOf(x = 2.1f, y = 4.9f),
                    vector2DOf(x = -1.1f, y = 10.2f),
                    vector2DOf(x = 2.1f, y = 10.2f)
            ).flatMap { coordinates ->
                Stream.of(
                        rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY),
                        mutableRectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY)
                ).map { rectangle -> Arguments.of(rectangle, coordinates) }
            }
        }

    }

    private class BoxFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        create { minX, maxX, minY, maxY, minZ, maxZ ->
                            boxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ)
                        },
                        create { minX, maxX, minY, maxY, minZ, maxZ ->
                            mutableBoxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ)
                        }
                )

        fun create(factory: (Float, Float, Float, Float, Float, Float) -> Box): Arguments = Arguments.of(factory)

    }

    private class BoxContainsArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val minZ = -20f
            val maxZ = 50f
            return Stream.of(
                    vector3DOf(x = 0f, y = 6f, z = 27f),
                    vector3DOf(x = minX, y = minY, z = minZ),
                    vector3DOf(x = minX, y = maxY, z = minZ),
                    vector3DOf(x = maxX, y = minY, z = minZ),
                    vector3DOf(x = maxX, y = maxY, z = minZ),
                    vector3DOf(x = minX, y = minY, z = maxZ),
                    vector3DOf(x = minX, y = maxY, z = maxZ),
                    vector3DOf(x = maxX, y = minY, z = maxZ),
                    vector3DOf(x = maxX, y = maxY, z = maxZ)
            ).flatMap { coordinates ->
                Stream.of(
                        boxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ),
                        mutableBoxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ)
                ).map { rectangle -> Arguments.of(rectangle, coordinates) }
            }
        }

    }

    private class BoxDoesNotContainArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val minZ = -20f
            val maxZ = 50f
            return Stream.of(
                    vector3DOf(x = -1.1f, y = 7f, z = 27f),
                    vector3DOf(x = 2.1f, y = 7f, z = 27f),
                    vector3DOf(x = 0f, y = 4.9f, z = 27f),
                    vector3DOf(x = 0f, y = 10.2f, z = 27f),
                    vector3DOf(x = -1.1f, y = 4.9f, z = 27f),
                    vector3DOf(x = 2.1f, y = 4.9f, z = 27f),
                    vector3DOf(x = -1.1f, y = 10.2f, z = 27f),
                    vector3DOf(x = 2.1f, y = 10.2f, z = 27f),
                    vector3DOf(x = -1.1f, y = 7f, z = 51f),
                    vector3DOf(x = 2.1f, y = 7f, z = 51f),
                    vector3DOf(x = 0f, y = 4.9f, z = 51f),
                    vector3DOf(x = 0f, y = 10.2f, z = 51f),
                    vector3DOf(x = -1.1f, y = 4.9f, z = 51f),
                    vector3DOf(x = 2.1f, y = 4.9f, z = 51f),
                    vector3DOf(x = -1.1f, y = 10.2f, z = 51f),
                    vector3DOf(x = 2.1f, y = 10.2f, z = 51f),
                    vector3DOf(x = -1.1f, y = 7f, z = -23f),
                    vector3DOf(x = 2.1f, y = 7f, z = -23f),
                    vector3DOf(x = 0f, y = 4.9f, z = -23f),
                    vector3DOf(x = 0f, y = 10.2f, z = -23f),
                    vector3DOf(x = -1.1f, y = 4.9f, z = -23f),
                    vector3DOf(x = 2.1f, y = 4.9f, z = -23f),
                    vector3DOf(x = -1.1f, y = 10.2f, z = -23f),
                    vector3DOf(x = 2.1f, y = 10.2f, z = -23f)
            ).flatMap { coordinates ->
                Stream.of(
                        boxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ),
                        mutableBoxOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY, minZ = minZ, maxZ = maxZ)
                ).map { rectangle -> Arguments.of(rectangle, coordinates) }
            }
        }

    }

}