package ch.leadrian.samp.kamp.core.api.command

/**
 * A resolver to translate a [String] command parameter value to a more appropriate object, a [ch.leadrian.samp.kamp.core.api.entity.Player].
 *
 * Bind a new [CommandParameterResolver] by using [ch.leadrian.samp.kamp.core.api.inject.KampModule.newCommandParameterResolverSetBinder].
 *
 * @see [ch.leadrian.samp.kamp.core.runtime.command.PlayerCommandParameterResolver]
 * @see [ch.leadrian.samp.kamp.core.runtime.command.WeaponModelCommandParameterResolver]
 * @see [ch.leadrian.samp.kamp.core.runtime.command.IntCommandParameterResolver]
 */
interface CommandParameterResolver<T : Any> {

    val parameterType: Class<T>

    /**
     * Converts [value] to an object of type [T], should return null if the value could not be parsed.
     * Do not throw an exception.
     *
     * @return if [value] could be converted to an object of type [T], else null
     */
    fun resolve(value: String): T?

}