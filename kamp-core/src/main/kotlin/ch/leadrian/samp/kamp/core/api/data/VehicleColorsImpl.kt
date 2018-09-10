package ch.leadrian.samp.kamp.core.api.data

internal data class VehicleColorsImpl(
        override val color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
        override val color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor
) : VehicleColors {

    override fun toVehicleColors(): VehicleColors = this

    override fun toMutableVehicleColors(): MutableVehicleColors = MutableVehicleColorsImpl(
            color1 = color1,
            color2 = color2
    )
}