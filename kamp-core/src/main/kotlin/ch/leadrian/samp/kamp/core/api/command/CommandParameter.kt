package ch.leadrian.samp.kamp.core.api.command

import kotlin.reflect.KClass

data class CommandParameter(
        val type: KClass<out Any>
)