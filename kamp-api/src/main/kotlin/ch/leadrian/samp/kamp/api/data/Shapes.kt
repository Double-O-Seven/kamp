@file:kotlin.jvm.JvmName("Shapes")

package ch.leadrian.samp.kamp.api.data

fun rectangleOf(minX: Float, maxX: Float, minY: Float, maxY: Float): Rectangle =
        RectangleImpl(
                minX = minX,
                maxX = maxX,
                minY = minY,
                maxY = maxY
        )

fun mutableRectangleOf(minX: Float, maxX: Float, minY: Float, maxY: Float): MutableRectangle =
        MutableRectangleImpl(
                minX = minX,
                maxX = maxX,
                minY = minY,
                maxY = maxY
        )


fun boxOf(minX: Float, maxX: Float, minY: Float, maxY: Float, minZ: Float, maxZ: Float): Box =
        BoxImpl(
                minX = minX,
                maxX = maxX,
                minY = minY,
                maxY = maxY,
                minZ = minZ,
                maxZ = maxZ
        )

fun mutableBoxOf(minX: Float, maxX: Float, minY: Float, maxY: Float, minZ: Float, maxZ: Float): MutableBox =
        MutableBoxImpl(
                minX = minX,
                maxX = maxX,
                minY = minY,
                maxY = maxY,
                minZ = minZ,
                maxZ = maxZ
        )

fun circleOf(x: Float, y: Float, radius: Float): Circle = CircleImpl(
        x = x,
        y = y,
        radius = radius
)

fun circleOf(coordinates: Vector2D, radius: Float): Circle = CircleImpl(
        x = coordinates.x,
        y = coordinates.y,
        radius = radius
)

fun mutableCircleOf(x: Float, y: Float, radius: Float): MutableCircle = MutableCircleImpl(
        x = x,
        y = y,
        radius = radius
)

fun mutableCircleOf(coordinates: Vector2D, radius: Float): MutableCircle = MutableCircleImpl(
        x = coordinates.x,
        y = coordinates.y,
        radius = radius
)
