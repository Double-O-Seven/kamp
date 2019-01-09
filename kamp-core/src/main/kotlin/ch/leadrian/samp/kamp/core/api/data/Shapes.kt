@file:kotlin.jvm.JvmName("Shapes")

package ch.leadrian.samp.kamp.core.api.data

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

fun sphereOf(x: Float, y: Float, z: Float, radius: Float): Sphere = SphereImpl(
        x = x,
        y = y,
        z = z,
        radius = radius
)

fun sphereOf(coordinates: Vector3D, radius: Float): Sphere = SphereImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        radius = radius
)

fun sphereOf(circle: Circle, z: Float): Sphere = SphereImpl(
        x = circle.x,
        y = circle.y,
        z = z,
        radius = circle.radius
)

fun Circle.toSphere(z: Float): Sphere = sphereOf(this, z)

fun mutableSphereOf(x: Float, y: Float, z: Float, radius: Float): MutableSphere = MutableSphereImpl(
        x = x,
        y = y,
        z = z,
        radius = radius
)

fun mutableSphereOf(coordinates: Vector3D, radius: Float): MutableSphere = MutableSphereImpl(
        x = coordinates.x,
        y = coordinates.y,
        z = coordinates.z,
        radius = radius
)

fun mutableSphereOf(circle: Circle, z: Float): MutableSphere = MutableSphereImpl(
        x = circle.x,
        y = circle.y,
        z = z,
        radius = circle.radius
)

fun Circle.toMutableSphere(z: Float): MutableSphere = mutableSphereOf(this, z)
