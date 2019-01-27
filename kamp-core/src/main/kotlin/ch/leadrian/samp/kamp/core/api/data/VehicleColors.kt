package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.runtime.data.VehicleColorsImpl

interface VehicleColors {

    val color1: VehicleColor

    val color2: VehicleColor

    fun toVehicleColors(): VehicleColors

    fun toMutableVehicleColors(): MutableVehicleColors
}

fun vehicleColorsOf(color1: Int, color2: Int): VehicleColors = VehicleColorsImpl(
        color1 = VehicleColor[color1],
        color2 = VehicleColor[color2]
)

fun vehicleColorsOf(color1: VehicleColor, color2: VehicleColor): VehicleColors = VehicleColorsImpl(
        color1 = color1,
        color2 = color2
)
