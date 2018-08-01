package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.ExplosionType
import ch.leadrian.samp.kamp.api.constants.Weather
import ch.leadrian.samp.kamp.api.data.Sphere
import ch.leadrian.samp.kamp.api.data.Vector3D

interface WorldService {

    fun setTime(time: Int)

    fun setWeather(weatherId: Int)

    fun setWeather(weather: Weather) {
        setWeather(weather.value)
    }

    fun setGravity(gravity: Float)

    fun getGravity(): Float

    fun createExplosion(type: ExplosionType, area: Sphere)

    fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float)

    fun disableInteriorEnterExits()
}