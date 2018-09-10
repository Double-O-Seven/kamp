package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface WorldService {

    fun setTime(time: Int)

    fun setWeather(weatherId: Int)

    fun setWeather(weather: ch.leadrian.samp.kamp.core.api.constants.Weather)

    fun setGravity(gravity: Float)

    fun getGravity(): Float

    fun createExplosion(type: ch.leadrian.samp.kamp.core.api.constants.ExplosionType, area: Sphere)

    fun createExplosion(type: ch.leadrian.samp.kamp.core.api.constants.ExplosionType, coordinates: Vector3D, radius: Float)

    fun disableInteriorEnterExits()
}