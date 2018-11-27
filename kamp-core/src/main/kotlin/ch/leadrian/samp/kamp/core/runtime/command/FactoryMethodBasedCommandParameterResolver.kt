package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import java.lang.reflect.Method
import java.lang.reflect.Modifier

internal class FactoryMethodBasedCommandParameterResolver<T : Any>
private constructor(
        override val parameterType: Class<T>,
        private val method: Method
) : CommandParameterResolver<T> {

    companion object {

        private val factoryMethodNames = listOf("valueOf", "of", "newInstance", "getInstance", "from", "parse")

        fun <T : Any> forType(parameterType: Class<T>): FactoryMethodBasedCommandParameterResolver<T>? {
            return factoryMethodNames
                    .asSequence()
                    .mapNotNull {
                        try {
                            parameterType.getMethod(it, String::class.java)
                        } catch (e: NoSuchMethodException) {
                            null
                        }
                    }
                    .filter { parameterType.isAssignableFrom(it.returnType) }
                    .firstOrNull { Modifier.isPublic(it.modifiers) && Modifier.isStatic(it.modifiers) }
                    ?.let { FactoryMethodBasedCommandParameterResolver(parameterType, it) }
        }

    }

    override fun resolve(value: String): T? = parameterType.cast(method.invoke(null, value))

}