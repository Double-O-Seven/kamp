package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandAccessCheckerGroup
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandDescription
import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.CommandParameterDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandAccessCheckers
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandParameter
import ch.leadrian.samp.kamp.core.api.command.annotation.InvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.UnlistedCommand
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import com.google.inject.Injector
import java.lang.reflect.Method
import java.lang.reflect.Modifier.isPublic
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.util.Collections.unmodifiableList
import javax.inject.Inject

internal class CommandDefinitionLoader
@Inject
constructor(
        private val injector: Injector,
        private val textProvider: TextProvider,
        private val commandParameterResolverRegistry: CommandParameterResolverRegistry
) {

    private val allowedReturnTypes = listOf(
            Unit::class.java,
            Void::class.javaObjectType,
            Void::class.javaPrimitiveType!!,
            Boolean::class.javaObjectType,
            Boolean::class.javaPrimitiveType!!
    )

    fun load(commandsClass: Class<out Commands>): List<CommandDefinition> {
        val commandAccessCheckerGroups = getCommandAccessCheckerAnnotations(commandsClass).map {
            getCommandAccessCheckerGroup(it)
        }
        val commandErrorHandler = getCommandErrorHandler(commandsClass)
        val commandMethods = getCommandMethods(commandsClass)
        return commandMethods.map {
            try {
                getCommandDefinition(it, commandAccessCheckerGroups, commandErrorHandler)
            } catch (e: CommandDefinitionLoaderException) {
                throw IllegalArgumentException("Could not load definition for method $it", e)
            }
        }
    }

    private fun getCommandMethods(commandsClass: Class<out Commands>): List<Method> {
        val commandMethods = mutableListOf<Method>()
        commandsClass.methods.filter { it.getAnnotation(Command::class.java) != null }.forEach {
            if (isStatic(it.modifiers) || !isPublic(it.modifiers)) {
                throw CommandDefinitionLoaderException("Method $it must be non-static and public")
            }
            if (it.returnType !in allowedReturnTypes) {
                throw CommandDefinitionLoaderException("Return type ${it.returnType} of method $it is not supported")
            }
            commandMethods += it
        }
        return commandMethods
    }

    private fun getCommandDefinition(
            commandMethod: Method,
            classAccessCheckers: List<CommandAccessCheckerGroup>,
            classCommandErrorHandler: CommandErrorHandler?
    ): CommandDefinition {
        val commandAnnotation: Command? = commandMethod.getAnnotation(Command::class.java)
        val name = (commandAnnotation?.name ?: commandMethod.name).toLowerCase()
        val aliases = commandAnnotation?.aliases?.toSet().orEmpty()
        val description = getCommandDescription(commandMethod)
        val parameters = getCommandParameterDefinitions(commandMethod)
        val isGreedy = commandAnnotation?.isGreedy ?: true
        val isListed = commandMethod.getAnnotation(UnlistedCommand::class.java) == null
        val errorHandler = getCommandErrorHandler(commandMethod) ?: classCommandErrorHandler
        val methodAccessCheckers = getCommandAccessCheckerAnnotations(commandMethod).map {
            getCommandAccessCheckerGroup(it)
        }
        val accessCheckers = mutableListOf<CommandAccessCheckerGroup>().apply {
            this += methodAccessCheckers
            this += classAccessCheckers
        }
        return CommandDefinition(
                name = name,
                aliases = aliases,
                description = description,
                method = commandMethod,
                parameters = parameters,
                isGreedy = isGreedy,
                isListed = isListed,
                errorHandler = errorHandler,
                accessCheckers = accessCheckers
        )
    }

    private fun getCommandParameterDefinitions(commandMethod: Method): List<CommandParameterDefinition> {
        val defaultInvalidValueHandler = commandMethod.getAnnotation(InvalidCommandParameterValueHandler::class.java)?.let {
            injector.getInstance(it.value.java)
        }
        return commandMethod.parameters.map { parameter ->
            val parameterAnnotation: CommandParameter? = parameter.getAnnotation(CommandParameter::class.java)
            val name = parameterAnnotation?.name?.takeIf { it.isNotEmpty() }
            val nameTextKey = parameterAnnotation?.nameTextKey?.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
            val description = parameterAnnotation?.description?.takeIf { it.isNotEmpty() }
            val descriptionTextKey = parameterAnnotation?.descriptionTextKey?.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
            val invalidValueHandler = parameter.getAnnotation(InvalidCommandParameterValueHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            } ?: defaultInvalidValueHandler
            val resolver = getCommandParameterResolver(parameter)
            CommandParameterDefinition(
                    type = parameter.type,
                    resolver = resolver,
                    invalidCommandParameterValueHandler = invalidValueHandler,
                    name = name,
                    nameTextKey = nameTextKey,
                    description = description,
                    descriptionTextKey = descriptionTextKey,
                    textProvider = textProvider
            )
        }
    }

    private fun getCommandParameterResolver(parameter: Parameter): CommandParameterResolver<*> {
        if (java.util.Collection::class.java.isAssignableFrom(parameter.type) || Collection::class.java.isAssignableFrom(parameter.type)) {
            val parameterizedType = parameter.parameterizedType
            if (parameterizedType is ParameterizedType) {
                if (parameterizedType.actualTypeArguments.size != 1) {
                    throw CommandDefinitionLoaderException("Type of parameter $parameter must have exactly one argument")
                }
                val typeArgument = parameterizedType.actualTypeArguments.first()
                if (typeArgument is Class<*>) {
                    return commandParameterResolverRegistry.getResolver(typeArgument)
                }
            }
            throw CommandDefinitionLoaderException("Cannot resolve type of parameter $parameter")
        } else {
            return commandParameterResolverRegistry.getResolver(parameter.type)
        }
    }

    private fun getCommandAccessCheckerGroup(annotation: CommandAccessChecker): CommandAccessCheckerGroup {
        val accessCheckers = annotation.accessCheckers.toSet().map { injector.getInstance(it.java) }
        val accessDeniedHandlers = annotation.accessDeniedHandlers.toSet().map { injector.getInstance(it.java) }
        val errorMessage = annotation.errorMessage.takeIf { it.isNotEmpty() }
        val errorMessageTextKey = annotation.errorMessageTextKey.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
        return CommandAccessCheckerGroup(
                accessCheckers = unmodifiableList(accessCheckers),
                accessDeniedHandlers = unmodifiableList(accessDeniedHandlers),
                errorMessage = errorMessage,
                errorMessageTextKey = errorMessageTextKey,
                textProvider = textProvider
        )
    }

    private fun getCommandAccessCheckerAnnotations(commandMethod: Method): List<CommandAccessChecker> {
        val annotations = mutableListOf<CommandAccessChecker>()
        commandMethod.getAnnotation(CommandAccessChecker::class.java)?.let { annotations += it }
        commandMethod.getAnnotation(CommandAccessCheckers::class.java)?.let { annotations += it.accessCheckers }
        return annotations
    }

    private fun getCommandAccessCheckerAnnotations(commandsClass: Class<out Commands>): List<CommandAccessChecker> {
        val annotations = mutableListOf<CommandAccessChecker>()
        commandsClass.getAnnotation(CommandAccessChecker::class.java)?.let { annotations += it }
        commandsClass.getAnnotation(CommandAccessCheckers::class.java)?.let { annotations += it.accessCheckers }
        return annotations
    }

    private fun getCommandDescription(commandMethod: Method): CommandDescription? =
            commandMethod.getAnnotation(ch.leadrian.samp.kamp.core.api.command.annotation.CommandDescription::class.java)?.let { annotation ->
                val text = annotation.text.takeIf { it.isNotEmpty() }
                val textKey = annotation.textKey.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
                if (text == null && textKey == null) {
                    null
                } else {
                    CommandDescription(text, textKey, textProvider)
                }
            }

    private fun getCommandErrorHandler(commandsClass: Class<out Commands>): CommandErrorHandler? =
            commandsClass.getAnnotation(ch.leadrian.samp.kamp.core.api.command.annotation.CommandErrorHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            }

    private fun getCommandErrorHandler(commandMethod: Method): CommandErrorHandler? =
            commandMethod.getAnnotation(ch.leadrian.samp.kamp.core.api.command.annotation.CommandErrorHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            }
}