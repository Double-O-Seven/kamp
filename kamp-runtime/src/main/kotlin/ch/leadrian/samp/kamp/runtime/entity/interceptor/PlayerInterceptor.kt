package ch.leadrian.samp.kamp.runtime.entity.interceptor

import ch.leadrian.samp.kamp.runtime.entity.InterceptablePlayer

interface PlayerInterceptor {

    fun intercept(interceptablePlayer: InterceptablePlayer): InterceptablePlayer

}
