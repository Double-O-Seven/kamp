package ch.leadrian.samp.kamp.runtime.entity.interceptor

import ch.leadrian.samp.kamp.runtime.entity.InterceptableVehicle

interface VehicleInterceptor {

    fun intercept(interceptableVehicle: InterceptableVehicle): InterceptableVehicle

}
