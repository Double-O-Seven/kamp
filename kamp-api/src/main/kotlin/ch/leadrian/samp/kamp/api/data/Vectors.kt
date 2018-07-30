@file:kotlin.jvm.JvmName("Vectors")

package ch.leadrian.samp.kamp.api.data

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

fun mutableVector3DOf(x: Float, y: Float, z: Float): MutableVector3D = MutableVector3DImpl(
        x = x,
        y = y,
        z = z
)

fun positionOf(x: Float, y: Float, z: Float, angle: Float): Position = PositionImpl(
        x = x,
        y = y,
        z = z,
        angle = angle
)

fun mutablePositionOf(x: Float, y: Float, z: Float, angle: Float): MutablePosition = MutablePositionImpl(
        x = x,
        y = y,
        z = z,
        angle = angle
)

fun locationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int): Location = LocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        worldId = worldId
)

fun mutableLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int): MutableLocation = MutableLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        worldId = worldId
)

fun angledLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int, angle: Float): AngledLocation = AngledLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        worldId = worldId,
        angle = angle
)

fun mutableAngledLocationOf(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int, angle: Float): MutableAngledLocation = MutableAngledLocationImpl(
        x = x,
        y = y,
        z = z,
        interiorId = interiorId,
        worldId = worldId,
        angle = angle
)
