package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.ExplosionType
import ch.leadrian.samp.kamp.core.api.constants.Weather
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

class WorldService
@Inject
internal constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun setTime(time: Int) {
        nativeFunctionExecutor.setWorldTime(time)
    }

    fun setWeather(weatherId: Int) {
        nativeFunctionExecutor.setWeather(weatherId)
    }

    fun setWeather(weather: Weather) {
        setWeather(weather.value)
    }

    fun setGravity(gravity: Float) {
        nativeFunctionExecutor.setGravity(gravity)
    }

    fun getGravity(): Float = nativeFunctionExecutor.getGravity()

    fun createExplosion(type: ExplosionType, area: Sphere) {
        nativeFunctionExecutor
                .createExplosion(x = area.x, y = area.y, z = area.z, radius = area.radius, type = type.value)
    }

    fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float) {
        nativeFunctionExecutor.createExplosion(
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                radius = radius,
                type = type.value
        )
    }

    fun disableInteriorEnterExits() {
        nativeFunctionExecutor.disableInteriorEnterExits()
    }
}