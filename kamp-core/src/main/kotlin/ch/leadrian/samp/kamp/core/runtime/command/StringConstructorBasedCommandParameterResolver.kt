package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

internal class StringConstructorBasedCommandParameterResolver<T : Any>
private constructor(
        override val parameterType: Class<T>,
        private val constructor: Constructor<T>
) : CommandParameterResolver<T> {

    companion object {

        fun <T : Any> forType(parameterType: Class<T>): StringConstructorBasedCommandParameterResolver<T>? {
            val constructor = try {
                parameterType.getConstructor(String::class.java)
            } catch (e: NoSuchMethodException) {
                return null
            }

            return if (Modifier.isPublic(constructor.modifiers)) {
                StringConstructorBasedCommandParameterResolver(parameterType, constructor)
            } else {
                null
            }
        }

    }

    override fun resolve(value: String): T? = constructor.newInstance(value)

}