package ch.leadrian.samp.kamp.core.api.data

internal data class SphereImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val radius: Float
) : Sphere {

    override val volume: Float =
            (4f * Math.PI.toFloat() * (radius * radius * radius)) / 3f

    override fun toSphere(): Sphere = this

    override fun toMutableSphere(): MutableSphere = MutableSphereImpl(
            x = x,
            y = y,
            z = z,
            radius = radius
    )
}