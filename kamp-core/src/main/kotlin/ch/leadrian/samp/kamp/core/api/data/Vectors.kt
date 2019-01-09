@file:kotlin.jvm.JvmName("Vectors")

package ch.leadrian.samp.kamp.core.api.data

fun vector2DOf(x: Float, y: Float): Vector2D = Vector2DImpl(
        x = x,
        y = y
)

fun mutableVector2DOf(x: Float, y: Float): MutableVector2D = MutableVector2DImpl(
        x = x,
        y = y
)

fun vector3DOf(x: Float, y: Float, z: Float): Vector3D = Vector3DImpl(
        x = x,
        y = y,
        z = z
)

fun vector3DOf(coordinates: Vector2D, z: Float): Vector3D = Vector3DImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = z
)

fun mutableVector3DOf(x: Float, y: Float, z: Float): MutableVector3D = MutableVector3DImpl(
        x = x,
        y = y,
        z = z
)

fun mutableVector3DOf(coordinates: Vector2D, z: Float): MutableVector3D = MutableVector3DImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = z
)

fun positionOf(x: Float, y: Float, z: Float, angle: Float): Position = PositionImpl(
        x = x,
        y = y,
        z = z,
        angle = angle
)

fun positionOf(coordinates: Vector3D, angle: Float): Position = PositionImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        angle = angle
)

fun mutablePositionOf(x: Float, y: Float, z: Float, angle: Float): MutablePosition = MutablePositionImpl(
        x = x,
        y = y,
        z = z,
        angle = angle
)

fun mutablePositionOf(coordinates: Vector3D, angle: Float): MutablePosition = MutablePositionImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        angle = angle
)

fun locationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int): Location = LocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        virtualWorldId = worldId
)

fun locationOf(coordinates: Vector3D, interiorId: Int, worldId: Int): Location = LocationImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        interiorId = interiorId,
        virtualWorldId = worldId
)

fun mutableLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int): MutableLocation = MutableLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        virtualWorldId = worldId
)

fun mutableLocationOf(coordinates: Vector3D, interiorId: Int, worldId: Int): MutableLocation = MutableLocationImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        interiorId = interiorId,
        virtualWorldId = worldId
)

fun angledLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int, angle: Float): AngledLocation = AngledLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = angle
)

fun angledLocationOf(coordinates: Vector3D, interiorId: Int, worldId: Int, angle: Float): AngledLocation = AngledLocationImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = angle
)

fun angledLocationOf(location: Location, angle: Float): AngledLocation = AngledLocationImpl(
        x = location.x,
        y = location.y,
        z = location.z,
        interiorId = location.interiorId,
        virtualWorldId = location.virtualWorldId,
        angle = angle
)

fun angledLocationOf(position: Position, interiorId: Int, worldId: Int): AngledLocation = AngledLocationImpl(
        x = position.x,
        y = position.y,
        z = position.z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = position.angle
)

fun mutableAngledLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int, angle: Float): MutableAngledLocation = MutableAngledLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = angle
)

fun mutableAngledLocationOf(coordinates: Vector3D, interiorId: Int, worldId: Int, angle: Float): MutableAngledLocation = MutableAngledLocationImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = angle
)

fun mutableAngledLocationOf(location: Location, angle: Float): MutableAngledLocation = MutableAngledLocationImpl(
        x = location.x,
        y = location.y,
        z = location.z,
        interiorId = location.interiorId,
        virtualWorldId = location.virtualWorldId,
        angle = angle
)

fun mutableAngledLocationOf(position: Position, interiorId: Int, worldId: Int): MutableAngledLocation = MutableAngledLocationImpl(
        x = position.x,
        y = position.y,
        z = position.z,
        interiorId = interiorId,
        virtualWorldId = worldId,
        angle = position.angle
)
