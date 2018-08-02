package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleColor

interface VehicleColors {

    val color1: VehicleColor

    val color2: VehicleColor

    fun toVehicleColors(): VehicleColors

    fun toMutableVehicleColors(): MutableVehicleColors
}

fun vehicleColorsOf(color1: VehicleColor, color2: VehicleColor): VehicleColors = VehicleColorsImpl(
        color1 = color1,
        color2 = color2
)
