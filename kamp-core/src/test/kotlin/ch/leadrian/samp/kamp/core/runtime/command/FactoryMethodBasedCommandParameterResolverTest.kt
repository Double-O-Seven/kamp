package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class FactoryMethodBasedCommandParameterResolverTest {

    @ParameterizedTest
    @ArgumentsSource(ParameterTypeArgumentProvider::class)
    fun shouldResolveStringValueWithFactoryMethod(parameterType: Class<*>, stringValue: String, expectedParameterValue: Any) {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(parameterType)!!

        val parameterValue = commandParameterResolver.resolve(stringValue)

        assertThat(parameterValue)
                .isEqualTo(expectedParameterValue)
    }

    @Test
    fun givenTypeWithoutPublicMethodItShouldReturnNull() {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(TypeWithoutPublicMethod::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    @Test
    fun givenTypeWithoutStaticMethodItShouldReturnNull() {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(TypeWithoutStaticMethod::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    @Test
    fun givenTypeWithoutMatchingFactoryMethodReturnTypeItShouldReturnNull() {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(TypeWithoutMatchingFactoryMethodReturnType::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    @Test
    fun givenTypeWithoutAppropriateFactoryMethodParameterItShouldReturnNull() {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(TypeWithoutAppropriateFactoryMethodParameter::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    @Test
    fun givenTypeWithoutAppropriateFactoryMethodItShouldReturnNull() {
        val commandParameterResolver = FactoryMethodBasedCommandParameterResolver.forType(TypeWithoutAppropriateFactoryMethod::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    private class ParameterTypeArgumentProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    Arguments.of(ValueOfType::class.java, "123", ValueOfType("123")),
                    Arguments.of(OfType::class.java, "123", OfType("123")),
                    Arguments.of(NewInstanceType::class.java, "123", NewInstanceType("123")),
                    Arguments.of(GetInstanceType::class.java, "123", GetInstanceType("123")),
                    Arguments.of(FromType::class.java, "123", FromType("123")),
                    Arguments.of(ParseType::class.java, "123", ParseType("123"))
            )
        }

    }

    class TypeWithoutPublicMethod {

        companion object {

            @Suppress("unused")
            @JvmStatic
            private fun valueOf(@Suppress("UNUSED_PARAMETER") value: String): TypeWithoutPublicMethod = TypeWithoutPublicMethod()
        }

    }

    class TypeWithoutStaticMethod {

        fun valueOf(@Suppress("UNUSED_PARAMETER") value: String): TypeWithoutStaticMethod = TypeWithoutStaticMethod()

    }

    class TypeWithoutMatchingFactoryMethodReturnType {

        companion object {

            @JvmStatic
            fun valueOf(@Suppress("UNUSED_PARAMETER") value: String): Any = TypeWithoutMatchingFactoryMethodReturnType()
        }

    }

    class TypeWithoutAppropriateFactoryMethodParameter {

        companion object {

            @JvmStatic
            fun valueOf(@Suppress("UNUSED_PARAMETER") value: Int): TypeWithoutAppropriateFactoryMethodParameter = TypeWithoutAppropriateFactoryMethodParameter()
        }

    }

    class TypeWithoutAppropriateFactoryMethod {

        companion object {

            @Suppress("unused")
            @JvmStatic
            fun createSomething(@Suppress("UNUSED_PARAMETER") value: String): TypeWithoutAppropriateFactoryMethod = TypeWithoutAppropriateFactoryMethod()
        }

    }

    data class ValueOfType(val value: String) {

        companion object {

            @JvmStatic
            fun valueOf(value: String): ValueOfType = ValueOfType(value)
        }

    }

    data class OfType(val value: String) {

        companion object {

            @JvmStatic
            fun of(value: String): OfType = OfType(value)
        }

    }

    data class NewInstanceType(val value: String) {

        companion object {

            @Suppress("unused")
            @JvmStatic
            fun newInstance(value: String): NewInstanceType = NewInstanceType(value)
        }

    }

    data class GetInstanceType(val value: String) {

        companion object {

            @JvmStatic
            fun getInstance(value: String): GetInstanceType = GetInstanceType(value)
        }

    }

    data class FromType(val value: String) {

        companion object {

            @Suppress("unused")
            @JvmStatic
            fun from(value: String): FromType = FromType(value)
        }

    }

    data class ParseType(val value: String) {

        companion object {

            @Suppress("unused")
            @JvmStatic
            fun parse(value: String): ParseType = ParseType(value)
        }

    }

}
