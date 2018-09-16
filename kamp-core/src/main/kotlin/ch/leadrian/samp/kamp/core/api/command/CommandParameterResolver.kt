package ch.leadrian.samp.kamp.core.api.command

interface CommandParameterResolver<T : Any> {

    val parameterType: Class<T>

    fun resolve(value: String): T?

}