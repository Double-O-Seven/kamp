package ch.leadrian.samp.kamp.core.api.data

interface MutableVehicleColors : VehicleColors {

    override var color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor

    override var color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor
}

fun mutableVehicleColorsOf(color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor, color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor): MutableVehicleColors = MutableVehicleColorsImpl(
        color1 = color1,
        color2 = color2
)
