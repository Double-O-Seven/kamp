package ch.leadrian.samp.kamp.api.data

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VectorsTest {

    @Nested
    inner class Vector2DTests {

        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun shouldCreateVector2DWithExpectedValues(factory: (Float, Float) -> Vector2D) {
            val x = 13.37f
            val y = 69f

            val vector2D: Vector2D = factory(x, y)

            assertThat(vector2D)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun shouldConvert2DToMutableVector2D(factory: (Float, Float) -> Vector2D) {
            val x = 13.37f
            val y = 69f
            val vector2D: Vector2D = factory(x, y)

            val mutableVector2D: MutableVector2D = vector2D.toMutableVector2D()

            assertThat(mutableVector2D)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun shouldConvertVector2DToImmutableVector2D(factory: (Float, Float) -> Vector2D) {
            val x = 13.37f
            val y = 69f
            val vector2D: Vector2D = factory(x, y)

            val immutableVector2D: Vector2D = vector2D.toVector2D()

            assertThat(immutableVector2D)
                    .isNotInstanceOf(MutableVector2D::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun shouldCalculateDistanceToVector2D(factory: (Float, Float) -> Vector2D) {
            val vector2D: Vector2D = factory(-7f, -4f)
            val otherVector2D = vector2DOf(x = 17f, y = 6.5f)

            val distance = vector2D.distanceTo(otherVector2D)

            assertThat(distance)
                    .isCloseTo(26.196374f, Percentage.withPercentage(0.001))
        }


        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun givenVectorInRangeItShouldReturnTrue(factory: (Float, Float) -> Vector2D) {
            val vector2D: Vector2D = factory(-7f, -4f)
            val otherVector2D = vector2DOf(x = 17f, y = 6.5f)

            val isInRange = vector2D.isInRange(otherVector2D, 27f)

            assertThat(isInRange)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(Vector2DFactoryArgumentsProvider::class)
        fun givenVectorNotInRangeItShouldReturnFalse(factory: (Float, Float) -> Vector2D) {
            val vector2D: Vector2D = factory(-7f, -4f)
            val otherVector2D = vector2DOf(x = 17f, y = 6.5f)

            val isInRange = vector2D.isInRange(otherVector2D, 25f)

            assertThat(isInRange)
                    .isFalse()
        }
    }

    @Nested
    inner class Vector3DTests {

        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun shouldCreateVector3DWithExpectedValues(factory: (Float, Float, Float) -> Vector3D) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f

            val vector3D: Vector3D = factory(x, y, z)

            assertThat(vector3D)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun shouldConvertVector3DToMutableVector3D(factory: (Float, Float, Float) -> Vector3D) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val vector3D: Vector3D = factory(x, y, z)

            val mutableVector3D: MutableVector3D = vector3D.toMutableVector3D()

            assertThat(mutableVector3D)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun shouldConvertVector3DToImmutableVector3D(factory: (Float, Float, Float) -> Vector3D) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val vector3D: Vector3D = factory(x, y, z)

            val immutableVector3D: Vector3D = vector3D.toVector3D()

            assertThat(immutableVector3D)
                    .isNotInstanceOf(MutableVector3D::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun shouldCalculateDistanceToVector3D(factory: (Float, Float, Float) -> Vector3D) {
            val vector3D: Vector3D = factory(7f, 4f, 3f)
            val otherVector3D = vector3DOf(x = 17f, y = 6f, z = 2f)

            val distance = vector3D.distanceTo(otherVector3D)

            assertThat(distance)
                    .isCloseTo(10.246951f, Percentage.withPercentage(0.001))
        }


        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun givenVectorInRangeItShouldReturnTrue(factory: (Float, Float, Float) -> Vector3D) {
            val vector3D: Vector3D = factory(7f, 4f, 3f)
            val otherVector3D = vector3DOf(x = 17f, y = 6f, z = 2f)

            val isInRange = vector3D.isInRange(otherVector3D, 11f)

            assertThat(isInRange)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(Vector3DFactoryArgumentsProvider::class)
        fun givenVectorNotInRangeItShouldReturnFalse(factory: (Float, Float, Float) -> Vector3D) {
            val vector3D: Vector3D = factory(7f, 4f, 3f)
            val otherVector3D = vector3DOf(x = 17f, y = 6f, z = 2f)

            val isInRange = vector3D.isInRange(otherVector3D, 9f)

            assertThat(isInRange)
                    .isFalse()
        }
    }

    @Nested
    inner class PositionTests {

        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun shouldCreatePositionWithExpectedValues(factory: (Float, Float, Float, Float) -> Position) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val angle = 123f

            val position: Position = factory(x, y, z, angle)

            assertThat(position)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.angle)
                                .isEqualTo(angle)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun shouldConvertPositionToMutablePosition(factory: (Float, Float, Float, Float) -> Position) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val angle = 123f
            val position: Position = factory(x, y, z, angle)

            val mutablePosition: MutablePosition = position.toMutablePosition()

            assertThat(mutablePosition)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.angle)
                                .isEqualTo(angle)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun shouldConvertPositionToImmutablePosition(factory: (Float, Float, Float, Float) -> Position) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val position: Position = factory(x, y, z, 0f)

            val immutablePosition: Position = position.toPosition()

            assertThat(immutablePosition)
                    .isNotInstanceOf(MutablePosition::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun shouldCalculateDistanceToPosition(factory: (Float, Float, Float, Float) -> Position) {
            val position: Position = factory(7f, 4f, 3f, 0f)
            val otherPosition = positionOf(x = 17f, y = 6f, z = 2f, angle = 0f)

            val distance = position.distanceTo(otherPosition)

            assertThat(distance)
                    .isCloseTo(10.246951f, Percentage.withPercentage(0.001))
        }


        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun givenVectorInRangeItShouldReturnTrue(factory: (Float, Float, Float, Float) -> Position) {
            val position: Position = factory(7f, 4f, 3f, 0f)
            val otherPosition = positionOf(x = 17f, y = 6f, z = 2f, angle = 0f)

            val isInRange = position.isInRange(otherPosition, 11f)

            assertThat(isInRange)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(PositionFactoryArgumentsProvider::class)
        fun givenVectorNotInRangeItShouldReturnFalse(factory: (Float, Float, Float, Float) -> Position) {
            val position: Position = factory(7f, 4f, 3f, 0f)
            val otherPosition = positionOf(x = 17f, y = 6f, z = 2f, angle = 0f)

            val isInRange = position.isInRange(otherPosition, 9f)

            assertThat(isInRange)
                    .isFalse()
        }
    }

    @Nested
    inner class LocationTests {

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun shouldCreateLocationWithExpectedValues(factory: (Float, Float, Float, Int, Int) -> Location) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234

            val location: Location = factory(x, y, z, interiorId, worldId)

            assertThat(location)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun shouldConvertLocationToMutableLocation(factory: (Float, Float, Float, Int, Int) -> Location) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234
            val location: Location = factory(x, y, z, interiorId, worldId)

            val mutableLocation: MutableLocation = location.toMutableLocation()

            assertThat(mutableLocation)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun shouldConvertLocationToImmutableLocation(factory: (Float, Float, Float, Int, Int) -> Location) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234
            val location: Location = factory(x, y, z, interiorId, worldId)

            val immutableLocation: Location = location.toLocation()

            assertThat(immutableLocation)
                    .isNotInstanceOf(MutableLocation::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenInteriorIdAndWorldIdAreTheSameItShouldCalculateDistanceToLocation(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234)

            val distance = location.distanceTo(otherLocation)

            assertThat(distance)
                    .isCloseTo(10.246951f, Percentage.withPercentage(0.001))
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenInteriorIdIsNotTheSameItShouldCalculateDistanceToLocation(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 65536, worldId = 1234)

            val distance = location.distanceTo(otherLocation)

            assertThat(distance)
                    .isGreaterThan(Float.MAX_VALUE)
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenWorldIdAndWorldIdAreTheSameItShouldCalculateDistanceToLocation(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 65536)

            val distance = location.distanceTo(otherLocation)

            assertThat(distance)
                    .isGreaterThan(Float.MAX_VALUE)
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenVectorInRangeItShouldReturnTrue(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234)

            val isInRange = location.isInRange(otherLocation, 11f)

            assertThat(isInRange)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenVectorNotInRangeItShouldReturnFalse(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234)

            val isInRange = location.isInRange(otherLocation, 9f)

            assertThat(isInRange)
                    .isFalse()
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenInteriorIdIsNotTheSameItShouldReturnFalse(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 65536, worldId = 1234)

            val isInRange = location.isInRange(otherLocation, 9999f)

            assertThat(isInRange)
                    .isFalse()
        }

        @ParameterizedTest
        @ArgumentsSource(LocationFactoryArgumentsProvider::class)
        fun givenWorldIdIsNotTheSameItShouldReturnFalse(factory: (Float, Float, Float, Int, Int) -> Location) {
            val location: Location = factory(7f, 4f, 3f, 1337, 1234)
            val otherLocation = locationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 65536)

            val isInRange = location.isInRange(otherLocation, 9999f)

            assertThat(isInRange)
                    .isFalse()
        }
    }

    @Nested
    inner class AngledLocationTests {

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun shouldCreateAngledLocationWithExpectedValues(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234
            val angle = 123f

            val angledLocation: AngledLocation = factory(x, y, z, interiorId, worldId, angle)

            assertThat(angledLocation)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                        assertThat(it.angle)
                                .isEqualTo(angle)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun shouldConvertAngledLocationToMutableAngledLocation(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234
            val angle = 123f
            val angledLocation: AngledLocation = factory(x, y, z, interiorId, worldId, angle)

            val mutableAngledLocation: MutableAngledLocation = angledLocation.toMutableAngledLocation()

            assertThat(mutableAngledLocation)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                        assertThat(it.angle)
                                .isEqualTo(angle)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun shouldConvertAngledLocationToImmutableAngledLocation(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val x = 13.37f
            val y = 69f
            val z = -0.815f
            val interiorId = 1337
            val worldId = 1234
            val angle = 123f
            val angledLocation: AngledLocation = factory(x, y, z, interiorId, worldId, angle)

            val immutableAngledLocation: AngledLocation = angledLocation.toAngledLocation()

            assertThat(immutableAngledLocation)
                    .isNotInstanceOf(MutableAngledLocation::class.java)
                    .satisfies {
                        assertThat(it.x)
                                .isEqualTo(x)
                        assertThat(it.y)
                                .isEqualTo(y)
                        assertThat(it.z)
                                .isEqualTo(z)
                        assertThat(it.interiorId)
                                .isEqualTo(interiorId)
                        assertThat(it.virtualWorldId)
                                .isEqualTo(worldId)
                        assertThat(it.angle)
                                .isEqualTo(angle)
                    }
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenInteriorIdAndWorldIdAreTheSameItShouldCalculateDistanceToAngledLocation(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234, angle = 0f)

            val distance = angledLocation.distanceTo(otherAngledLocation)

            assertThat(distance)
                    .isCloseTo(10.246951f, Percentage.withPercentage(0.001))
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenInteriorIdIsNotTheSameItShouldCalculateDistanceToAngledLocation(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 65536, worldId = 1234, angle = 0f)

            val distance = angledLocation.distanceTo(otherAngledLocation)

            assertThat(distance)
                    .isGreaterThan(Float.MAX_VALUE)
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenWorldIdAndWorldIdAreTheSameItShouldCalculateDistanceToAngledLocation(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 65536, angle = 0f)

            val distance = angledLocation.distanceTo(otherAngledLocation)

            assertThat(distance)
                    .isGreaterThan(Float.MAX_VALUE)
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenVectorInRangeItShouldReturnTrue(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234, angle = 0f)

            val isInRange = angledLocation.isInRange(otherAngledLocation, 11f)

            assertThat(isInRange)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenVectorNotInRangeItShouldReturnFalse(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 1234, angle = 0f)

            val isInRange = angledLocation.isInRange(otherAngledLocation, 9f)

            assertThat(isInRange)
                    .isFalse()
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenInteriorIdIsNotTheSameItShouldReturnFalse(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 65536, worldId = 1234, angle = 0f)

            val isInRange = angledLocation.isInRange(otherAngledLocation, 9999f)

            assertThat(isInRange)
                    .isFalse()
        }

        @ParameterizedTest
        @ArgumentsSource(AngledLocationFactoryArgumentsProvider::class)
        fun givenWorldIdIsNotTheSameItShouldReturnFalse(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation) {
            val angledLocation: AngledLocation = factory(7f, 4f, 3f, 1337, 1234, 0f)
            val otherAngledLocation = angledLocationOf(x = 17f, y = 6f, z = 2f, interiorId = 1337, worldId = 65536, angle = 0f)

            val isInRange = angledLocation.isInRange(otherAngledLocation, 9999f)

            assertThat(isInRange)
                    .isFalse()
        }
    }

    private class Vector2DFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                createArguments { x, y -> vector2DOf(x = x, y = y) },
                createArguments { x, y -> mutableVector2DOf(x = x, y = y) },
                createArguments { x, y -> vector3DOf(x = x, y = y, z = 0f) },
                createArguments { x, y -> mutableVector3DOf(x = x, y = y, z = 0f) },
                createArguments { x, y -> positionOf(x = x, y = y, z = 0f, angle = 0f) },
                createArguments { x, y -> mutablePositionOf(x = x, y = y, z = 0f, angle = 0f) },
                createArguments { x, y -> locationOf(x = x, y = y, z = 0f, interiorId = 0, worldId = 0) },
                createArguments { x, y -> mutableLocationOf(x = x, y = y, z = 0f, interiorId = 0, worldId = 0) },
                createArguments { x, y -> angledLocationOf(x = x, y = y, z = 0f, interiorId = 0, worldId = 0, angle = 0f) },
                createArguments { x, y -> mutableAngledLocationOf(x = x, y = y, z = 0f, interiorId = 0, worldId = 0, angle = 0f) }
        )

        private fun createArguments(factory: (Float, Float) -> Vector2D): Arguments = Arguments.of(factory)

    }

    private class Vector3DFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                createArguments { x, y, z -> vector3DOf(x = x, y = y, z = z) },
                createArguments { x, y, z -> mutableVector3DOf(x = x, y = y, z = z) },
                createArguments { x, y, z -> positionOf(x = x, y = y, z = z, angle = 0f) },
                createArguments { x, y, z -> mutablePositionOf(x = x, y = y, z = z, angle = 0f) },
                createArguments { x, y, z -> locationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0) },
                createArguments { x, y, z -> mutableLocationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0) },
                createArguments { x, y, z -> angledLocationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0, angle = 0f) },
                createArguments { x, y, z -> mutableAngledLocationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0, angle = 0f) }
        )

        private fun createArguments(factory: (Float, Float, Float) -> Vector3D): Arguments = Arguments.of(factory)

    }

    private class PositionFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                createArguments { x, y, z, angle -> positionOf(x = x, y = y, z = z, angle = angle) },
                createArguments { x, y, z, angle -> mutablePositionOf(x = x, y = y, z = z, angle = angle) },
                createArguments { x, y, z, angle -> angledLocationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0, angle = angle) },
                createArguments { x, y, z, angle -> mutableAngledLocationOf(x = x, y = y, z = z, interiorId = 0, worldId = 0, angle = angle) }
        )

        private fun createArguments(factory: (Float, Float, Float, Float) -> Position): Arguments = Arguments.of(factory)

    }

    private class LocationFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                createArguments { x, y, z, interiorId, worldId -> locationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId) },
                createArguments { x, y, z, interiorId, worldId -> mutableLocationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId) },
                createArguments { x, y, z, interiorId, worldId -> angledLocationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId, angle = 0f) },
                createArguments { x, y, z, interiorId, worldId -> mutableAngledLocationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId, angle = 0f) }
        )

        private fun createArguments(factory: (Float, Float, Float, Int, Int) -> Location): Arguments = Arguments.of(factory)

    }

    private class AngledLocationFactoryArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                createArguments { x, y, z, interiorId, worldId, angle -> angledLocationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId, angle = angle) },
                createArguments { x, y, z, interiorId, worldId, angle -> mutableAngledLocationOf(x = x, y = y, z = z, interiorId = interiorId, worldId = worldId, angle = angle) }
        )

        private fun createArguments(factory: (Float, Float, Float, Int, Int, Float) -> AngledLocation): Arguments = Arguments.of(factory)

    }

}