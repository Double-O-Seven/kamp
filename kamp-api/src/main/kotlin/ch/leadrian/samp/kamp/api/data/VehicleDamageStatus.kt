package ch.leadrian.samp.kamp.api.data

data class VehicleDamageStatus(
        val panels: VehiclePanelDamageStatus,
        val doors: VehicleDoorsDamageStatus,
        val lights: VehicleLightsDamageStatus,
        val tires: VehicleTiresDamageStatus
)
