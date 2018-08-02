package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleColor

internal data class MutableVehicleColorsImpl(
        override var color1: VehicleColor,
        override var color2: VehicleColor
) : MutableVehicleColors {

    override fun toVehicleColors(): VehicleColors = VehicleColorsImpl(
            color1 = color1,
            color2 = color2
    )

    override fun toMutableVehicleColors(): MutableVehicleColors = this
}