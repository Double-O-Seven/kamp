package ch.leadrian.samp.kamp.core.api.data

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
        fun shouldHaveExpectedWidth(factory: (Float, Float, Float, Float) -> Rectangle) {
            val rectangle = factory(-1f, 2f, 5f, 10f)

            val width = rectangle.width

            assertThat(width)
                    .isCloseTo(3f, withPercentage(0.0001))
        }

        @ParameterizedTest
        @ArgumentsSource(RectangleFactoryArgumentsProvider::class)
        fun shouldHaveExpectedHeight(factory: (Float, Float, Float, Float) -> Rectangle) {
            val rectangle = factory(-1f, 2f, 5f, 10f)

            val height = rectangle.height

            assertThat(height)
                    .isCloseTo(5f, withPercentage(0.0001))
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
        fun shouldHaveExpectedArea(factory: (Float, Float, Float, Float, Float, Float) -> ch.leadrian.samp.kamp.core.api.data.Box) {
            val box = factory(-1f, 2f, 5f, 10f, -20f, 50f)

            val area = box.volume

            assertThat(area)
                    .isCloseTo(1050f, withPercentage(0.0001))
        }

        @ParameterizedTest
        @ArgumentsSource(BoxFactoryArgumentsProvider::class)
        fun toBoxShouldReturnImmutableBox(factory: (Float, Float, Float, Float, Float, Float) -> ch.leadrian.samp.kamp.core.api.data.Box) {
            val minX = -1f
            val maxX = 2f
            val minY = 5f
            val maxY = 10f
            val minZ = -20f
            val maxZ = 50f
            val box = factory(minX, maxX, minY, maxY, minZ, maxZ)

            val immutableBox: ch.leadrian.samp.kamp.core.api.data.Box = box.toBox()

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
        fun toMutableBoxShouldReturnMutableBox(factory: (Float, Float, Float, Float, Float, Float) -> ch.leadrian.samp.kamp.core.api.data.Box) {
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
        fun shouldContainCoordinates(box: ch.leadrian.samp.kamp.core.api.data.Box, coordinates: Vector3D) {
            val contains = box.contains(coordinates)

            assertThat(contains)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(BoxDoesNotContainArgumentsProvider::class)
        fun shouldNotContainCoordinates(box: ch.leadrian.samp.kamp.core.api.data.Box, coordinates: Vector3D) {
            val contains = box.contains(coordinates)

            assertThat(contains)
                    .isFalse()
        }
    }

    @Nested
    inner class CircleTests {

        @ParameterizedTest
        @ArgumentsSource(CircleFactoryArgumentsProvider::class)
        fun shouldHaveExpectedArea(factory: (Float, Float, Float) -> Circle) {
            val circle = factory(2f, 5f, 3.5f)

            val area = circle.area

            assertThat(area)
                    .isCloseTo(38.4845100065f, withPercentage(0.0001))
        }

        @ParameterizedTest
        @ArgumentsSource(CircleFactoryArgumentsProvider::class)
        fun toCircleShouldReturnImmutableCircle(factory: (Float, Float, Float) -> Circle) {
            val x = 2f
            val y = 5f
            val radius = 3.5f
            val circle = factory(x, y, radius)

            val immutableCircle: Circle = circle.toCircle()

            assertThat(immutableCircle)
                    .isNotInstanceOf(MutableCircle::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.radius)
                                .isEqualTo(radius)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(CircleFactoryArgumentsProvider::class)
        fun toMutableCircleShouldReturnMutableCircle(factory: (Float, Float, Float) -> Circle) {
            val x = 2f
            val y = 5f
            val radius = 3.5f
            val circle = factory(x, y, radius)

            val mutableCircle: MutableCircle = circle.toMutableCircle()

            assertThat(mutableCircle)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.radius)
                                .isEqualTo(radius)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(CircleContainsArgumentsProvider::class)
        fun shouldContainCoordinates(circle: Circle, coordinates: Vector2D) {
            val contains = circle.contains(coordinates)

            assertThat(contains)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(CircleDoesNotContainArgumentsProvider::class)
        fun shouldNotContainCoordinates(circle: Circle, coordinates: Vector2D) {
            val contains = circle.contains(coordinates)

            assertThat(contains)
                    .isFalse()
        }
    }

    @Nested
    inner class SphereTests {

        @ParameterizedTest
        @ArgumentsSource(SphereFactoryArgumentsProvider::class)
        fun shouldHaveExpectedArea(factory: (Float, Float, Float, Float) -> Sphere) {
            val sphere = factory(2f, 5f, -1f, 3.5f)

            val area = sphere.volume

            assertThat(area)
                    .isCloseTo(179.594f, withPercentage(0.001))
        }

        @ParameterizedTest
        @ArgumentsSource(SphereFactoryArgumentsProvider::class)
        fun toSphereShouldReturnImmutableSphere(factory: (Float, Float, Float, Float) -> Sphere) {
            val x = 2f
            val y = 5f
            val z = -1f
            val radius = 3.5f
            val sphere = factory(x, y, z, radius)

            val immutableSphere: Sphere = sphere.toSphere()

            assertThat(immutableSphere)
                    .isNotInstanceOf(MutableSphere::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.radius)
                                .isEqualTo(radius)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(SphereFactoryArgumentsProvider::class)
        fun toMutableSphereShouldReturnMutableSphere(factory: (Float, Float, Float, Float) -> Sphere) {
            val x = 2f
            val y = 5f
            val z = -1f
            val radius = 3.5f
            val sphere = factory(x, y, z, radius)

            val mutableSphere: MutableSphere = sphere.toMutableSphere()

            assertThat(mutableSphere)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.radius)
                                .isEqualTo(radius)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(SphereContainsArgumentsProvider::class)
        fun shouldContainCoordinates(sphere: Sphere, coordinates: Vector3D) {
            val contains = sphere.contains(coordinates)

            assertThat(contains)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(SphereDoesNotContainArgumentsProvider::class)
        fun shouldNotContainCoordinates(sphere: Sphere, coordinates: Vector3D) {
            val contains = sphere.contains(coordinates)

            assertThat(contains)
                    .isFalse()
        }
    }

    private class RectangleFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        create { minX, maxX, minY, maxY ->
                            rectangleOf(
                                    minX = minX,
                                    maxX = maxX,
                                    minY = minY,
                                    maxY = maxY
                            )
                        },
                        create { minX, maxX, minY, maxY ->
                            mutableRectangleOf(
                                    minX = minX,
                                    maxX = maxX,
                                    minY = minY,
                                    maxY = maxY
                            )
                        }
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

        fun create(factory: (Float, Float, Float, Float, Float, Float) -> ch.leadrian.samp.kamp.core.api.data.Box): Arguments = Arguments.of(
                factory
        )

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
                ).map { box -> Arguments.of(box, coordinates) }
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
                ).map { box -> Arguments.of(box, coordinates) }
            }
        }

    }

    private class CircleFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        create { x, y, radius -> circleOf(x = x, y = y, radius = radius) },
                        create { x, y, radius -> circleOf(coordinates = vector2DOf(x = x, y = y), radius = radius) },
                        create { x, y, radius -> mutableCircleOf(x = x, y = y, radius = radius) },
                        create { x, y, radius ->
                            mutableCircleOf(
                                    coordinates = vector2DOf(x = x, y = y),
                                    radius = radius
                            )
                        }
                )

        fun create(factory: (Float, Float, Float) -> Circle): Arguments = Arguments.of(factory)

    }

    private class CircleContainsArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val x = -1f
            val y = 2f
            val radius = 5f
            return Stream.of(
                    vector2DOf(x = -1f, y = 2f),
                    vector2DOf(x = -6f, y = 2f),
                    vector2DOf(x = 4f, y = 2f),
                    vector2DOf(x = -1f, y = 7f),
                    vector2DOf(x = -1f, y = -3f),
                    vector2DOf(x = -4f, y = 2f),
                    vector2DOf(x = 3f, y = 2f),
                    vector2DOf(x = -1f, y = 6f),
                    vector2DOf(x = -1f, y = -2f),
                    vector2DOf(x = -2f, y = 3f),
                    vector2DOf(x = -2f, y = 1f),
                    vector2DOf(x = -3f, y = 3f),
                    vector2DOf(x = -3f, y = 1f)
            ).flatMap { coordinates ->
                Stream.of(
                        circleOf(x = x, y = y, radius = radius),
                        mutableCircleOf(x = x, y = y, radius = radius)
                ).map { circle -> Arguments.of(circle, coordinates) }
            }
        }

    }

    private class CircleDoesNotContainArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val x = -1f
            val y = 2f
            val radius = 5f
            return Stream.of(
                    vector2DOf(x = -6.1f, y = 2f),
                    vector2DOf(x = 4.1f, y = 2f),
                    vector2DOf(x = -1f, y = 7.1f),
                    vector2DOf(x = -1f, y = -3.1f),
                    vector2DOf(x = -6.1f, y = 7.1f),
                    vector2DOf(x = 4.1f, y = 7.1f),
                    vector2DOf(x = -6.1f, y = -3.1f),
                    vector2DOf(x = 4.1f, y = -3.1f)
            ).flatMap { coordinates ->
                Stream.of(
                        circleOf(x = x, y = y, radius = radius),
                        mutableCircleOf(x = x, y = y, radius = radius)
                ).map { circle -> Arguments.of(circle, coordinates) }
            }
        }

    }

    private class SphereFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        create { x, y, z, radius -> sphereOf(x = x, y = y, z = z, radius = radius) },
                        create { x, y, z, radius ->
                            sphereOf(
                                    coordinates = vector3DOf(x = x, y = y, z = z),
                                    radius = radius
                            )
                        },
                        create { x, y, z, radius -> sphereOf(circle = circleOf(x = x, y = y, radius = radius), z = z) },
                        create { x, y, z, radius -> circleOf(x = x, y = y, radius = radius).toSphere(z) },
                        create { x, y, z, radius -> mutableSphereOf(x = x, y = y, z = z, radius = radius) },
                        create { x, y, z, radius ->
                            mutableSphereOf(
                                    coordinates = vector3DOf(x = x, y = y, z = z),
                                    radius = radius
                            )
                        },
                        create { x, y, z, radius ->
                            mutableSphereOf(
                                    circle = circleOf(x = x, y = y, radius = radius),
                                    z = z
                            )
                        },
                        create { x, y, z, radius -> circleOf(x = x, y = y, radius = radius).toMutableSphere(z) }
                )

        fun create(factory: (Float, Float, Float, Float) -> Sphere): Arguments = Arguments.of(factory)

    }

    private class SphereContainsArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val x = -1f
            val y = 2f
            val z = 1f
            val radius = 5f
            return Stream.of(
                    vector3DOf(x = -1f, y = 2f, z = 1f),
                    vector3DOf(x = -6f, y = 2f, z = 1f),
                    vector3DOf(x = 4f, y = 2f, z = 1f),
                    vector3DOf(x = -1f, y = 7f, z = 1f),
                    vector3DOf(x = -1f, y = -3f, z = 1f),
                    vector3DOf(x = -1f, y = 2f, z = 6f),
                    vector3DOf(x = -1f, y = 2f, z = -4f),
                    vector3DOf(x = -4f, y = 2f, z = 1f),
                    vector3DOf(x = 3f, y = 2f, z = 1f),
                    vector3DOf(x = -1f, y = 6f, z = 1f),
                    vector3DOf(x = -1f, y = -2f, z = 1f),
                    vector3DOf(x = -2f, y = 3f, z = 1f),
                    vector3DOf(x = -2f, y = 1f, z = 1f),
                    vector3DOf(x = -3f, y = 3f, z = 1f),
                    vector3DOf(x = -3f, y = 1f, z = 1f),
                    vector3DOf(x = -4f, y = 2f, z = 2f),
                    vector3DOf(x = 3f, y = 2f, z = 2f),
                    vector3DOf(x = -1f, y = 6f, z = 2f),
                    vector3DOf(x = -1f, y = -2f, z = 2f),
                    vector3DOf(x = -2f, y = 3f, z = 2f),
                    vector3DOf(x = -2f, y = 1f, z = 2f),
                    vector3DOf(x = -3f, y = 3f, z = 2f),
                    vector3DOf(x = -3f, y = 1f, z = 2f),
                    vector3DOf(x = -4f, y = 2f, z = 0f),
                    vector3DOf(x = 3f, y = 2f, z = 0f),
                    vector3DOf(x = -1f, y = 6f, z = 0f),
                    vector3DOf(x = -1f, y = -2f, z = 0f),
                    vector3DOf(x = -2f, y = 3f, z = 0f),
                    vector3DOf(x = -2f, y = 1f, z = 0f),
                    vector3DOf(x = -3f, y = 3f, z = 0f),
                    vector3DOf(x = -3f, y = 1f, z = 0f)
            ).flatMap { coordinates ->
                Stream.of(
                        sphereOf(x = x, y = y, z = z, radius = radius),
                        mutableSphereOf(x = x, y = y, z = z, radius = radius)
                ).map { sphere -> Arguments.of(sphere, coordinates) }
            }
        }

    }

    private class SphereDoesNotContainArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            val x = -1f
            val y = 2f
            val z = 1f
            val radius = 5f
            return Stream.of(
                    vector3DOf(x = -6.1f, y = 2f, z = -1.2f),
                    vector3DOf(x = 4.1f, y = 2f, z = -1.2f),
                    vector3DOf(x = -1f, y = 7.1f, z = -1.2f),
                    vector3DOf(x = -1f, y = -3.1f, z = -1.2f),
                    vector3DOf(x = -6.1f, y = 7.1f, z = -1.2f),
                    vector3DOf(x = 4.1f, y = 7.1f, z = -1.2f),
                    vector3DOf(x = -6.1f, y = -3.1f, z = -1.2f),
                    vector3DOf(x = 4.1f, y = -3.1f, z = -1.2f),
                    vector3DOf(x = -6.1f, y = 2f, z = 6.1f),
                    vector3DOf(x = 4.1f, y = 2f, z = 6.1f),
                    vector3DOf(x = -1f, y = 7.1f, z = 6.1f),
                    vector3DOf(x = -1f, y = -3.1f, z = 6.1f),
                    vector3DOf(x = -6.1f, y = 7.1f, z = 6.1f),
                    vector3DOf(x = 4.1f, y = 7.1f, z = 6.1f),
                    vector3DOf(x = -6.1f, y = -3.1f, z = 6.1f),
                    vector3DOf(x = 4.1f, y = -3.1f, z = 6.1f),
                    vector3DOf(x = -6.1f, y = 2f, z = -4.1f),
                    vector3DOf(x = 4.1f, y = 2f, z = -4.1f),
                    vector3DOf(x = -1f, y = 7.1f, z = -4.1f),
                    vector3DOf(x = -1f, y = -3.1f, z = -4.1f),
                    vector3DOf(x = -6.1f, y = 7.1f, z = -4.1f),
                    vector3DOf(x = 4.1f, y = 7.1f, z = -4.1f),
                    vector3DOf(x = -6.1f, y = -3.1f, z = -4.1f),
                    vector3DOf(x = 4.1f, y = -3.1f, z = -4.1f)
            ).flatMap { coordinates ->
                Stream.of(
                        sphereOf(x = x, y = y, z = z, radius = radius),
                        mutableSphereOf(x = x, y = y, z = z, radius = radius)
                ).map { sphere -> Arguments.of(sphere, coordinates) }
            }
        }

    }

}