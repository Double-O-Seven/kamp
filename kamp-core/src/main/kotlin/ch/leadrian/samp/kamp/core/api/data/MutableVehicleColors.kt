package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleColor

interface MutableVehicleColors : VehicleColors {

    override var color1: VehicleColor

    override var color2: VehicleColor
}

fun mutableVehicleColorsOf(color1: Int, color2: Int): MutableVehicleColors = MutableVehicleColorsImpl(
        color1 = VehicleColor[color1],
        color2 = VehicleColor[color2]
)

fun mutableVehicleColorsOf(color1: VehicleColor, color2: VehicleColor): MutableVehicleColors = MutableVehicleColorsImpl(
        color1 = color1,
        color2 = color2
)
