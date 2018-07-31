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
