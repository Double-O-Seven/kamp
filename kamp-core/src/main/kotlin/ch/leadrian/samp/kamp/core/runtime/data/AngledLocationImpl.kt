package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.MutableAngledLocation
import ch.leadrian.samp.kamp.core.api.data.MutableLocation
import ch.leadrian.samp.kamp.core.api.data.MutablePosition
import ch.leadrian.samp.kamp.core.api.data.MutableVector2D
import ch.leadrian.samp.kamp.core.api.data.MutableVector3D
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D

internal data class AngledLocationImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val interiorId: Int,
        override val virtualWorldId: Int,
        override val angle: Float
) : AngledLocation {

    override fun toAngledLocation(): AngledLocation = this

    override fun toMutableAngledLocation(): MutableAngledLocation = MutableAngledLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId,
            angle = angle
    )

    override fun toPosition(): Position = this

    override fun toMutablePosition(): MutablePosition = MutablePositionImpl(
            x = x,
            y = y,
            z = z,
            angle = angle
    )

    override fun toLocation(): Location = this

    override fun toMutableLocation(): MutableLocation = MutableLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId
    )

    override fun toVector3D(): Vector3D = this

    override fun toMutableVector3D(): MutableVector3D = MutableVector3DImpl(
            x = x,
            y = y,
            z = z
    )

    override fun toVector2D(): Vector2D = this

    override fun toMutableVector2D(): MutableVector2D = MutableVector2DImpl(
            x = x,
            y = y
    )

    override fun plus(other: Vector2D): AngledLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): AngledLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

    override fun minus(other: Vector2D): AngledLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun minus(other: Vector3D): AngledLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
    )

    override fun times(value: Float): AngledLocation = copy(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
    )

    override fun div(value: Float): AngledLocation = copy(
            x = this.x / value,
            y = this.y / value,
            z = this.z / value
    )
}