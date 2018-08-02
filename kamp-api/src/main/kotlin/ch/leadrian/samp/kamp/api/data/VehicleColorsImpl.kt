package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleColor

internal data class VehicleColorsImpl(
        override val color1: VehicleColor,
        override val color2: VehicleColor
) : VehicleColors {

    override fun toVehicleColors(): VehicleColors = this

    override fun toMutableVehicleColors(): MutableVehicleColors = MutableVehicleColorsImpl(
            color1 = color1,
            color2 = color2
    )
}