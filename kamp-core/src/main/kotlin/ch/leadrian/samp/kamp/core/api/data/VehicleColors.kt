package ch.leadrian.samp.kamp.core.api.data

interface VehicleColors {

    val color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor

    val color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor

    fun toVehicleColors(): VehicleColors

    fun toMutableVehicleColors(): MutableVehicleColors
}

fun vehicleColorsOf(color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor, color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor): VehicleColors = VehicleColorsImpl(
        color1 = color1,
        color2 = color2
)
