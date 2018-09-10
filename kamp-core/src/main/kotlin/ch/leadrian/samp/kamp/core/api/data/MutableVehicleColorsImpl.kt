package ch.leadrian.samp.kamp.core.api.data

internal data class MutableVehicleColorsImpl(
        override var color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
        override var color2: ch.leadrian.samp.kamp.core.api.constants.VehicleColor
) : MutableVehicleColors {

    override fun toVehicleColors(): VehicleColors = VehicleColorsImpl(
            color1 = color1,
            color2 = color2
    )

    override fun toMutableVehicleColors(): MutableVehicleColors = this
}