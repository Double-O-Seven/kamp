package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandAccessCheckerGroup
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandDescription
import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.CommandParameterDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessChecks
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.command.annotation.ErrorHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.InvalidParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import com.google.inject.Injector
import java.lang.reflect.Method
import java.lang.reflect.Modifier.isPublic
import java.lang.reflect.Modifier.isStatic
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
        val commandAccessCheckerGroups = getAccessChecks(commandsClass).map {
            getCommandAccessCheckerGroup(it)
        }
        val commandErrorHandler = getErrorHandler(commandsClass)
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
        val name = (commandAnnotation?.name?.takeIf { it.isNotEmpty() } ?: commandMethod.name).toLowerCase()
        val aliases = commandAnnotation?.aliases?.toSet().orEmpty()
        val description = getDescription(commandMethod)
        val parameters = getCommandParameterDefinitions(commandMethod)
        val isGreedy = commandAnnotation?.isGreedy ?: true
        val isListed = commandMethod.getAnnotation(Unlisted::class.java) == null
        val errorHandler = getErrorHandler(commandMethod) ?: classCommandErrorHandler
        val methodAccessCheckers = getAccessChecks(commandMethod).map {
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
        if (commandMethod.parameters.firstOrNull()?.type != Player::class.java) {
            throw CommandDefinitionLoaderException("First parameter must be of type ${Player::class.java}")
        }
        val defaultInvalidValueHandler = commandMethod.getAnnotation(InvalidParameterValueHandler::class.java)?.let {
            injector.getInstance(it.value.java)
        }
        val commandParameters = commandMethod.parameters.drop(1)
        return commandParameters.mapIndexed { index, parameter ->
            val parameterAnnotation: Parameter? = parameter.getAnnotation(Parameter::class.java)
            val name = parameterAnnotation?.name?.takeIf { it.isNotEmpty() }
            val nameTextKey = parameterAnnotation?.nameTextKey?.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
            val description = parameterAnnotation?.description?.takeIf { it.isNotEmpty() }
            val descriptionTextKey = parameterAnnotation?.descriptionTextKey?.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
            val invalidValueHandler = parameter.getAnnotation(InvalidParameterValueHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            } ?: defaultInvalidValueHandler
            val isLast = index == commandParameters.size - 1
            val resolver = getCommandParameterResolver(parameter, isLast)
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

    private fun getCommandParameterResolver(parameter: java.lang.reflect.Parameter, isLast: Boolean): CommandParameterResolver<*> {
        if (java.util.Collection::class.java.isAssignableFrom(parameter.type) || Collection::class.java.isAssignableFrom(parameter.type)) {
            if (!isLast) {
                throw CommandDefinitionLoaderException("Only the last parameter can be a collection type")
            }
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

    private fun getCommandAccessCheckerGroup(annotation: AccessCheck): CommandAccessCheckerGroup {
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

    private fun getAccessChecks(commandMethod: Method): List<AccessCheck> {
        val annotations = mutableListOf<AccessCheck>()
        commandMethod.getAnnotation(AccessCheck::class.java)?.let { annotations += it }
        commandMethod.getAnnotation(AccessChecks::class.java)?.let { annotations += it.accessCheckers }
        return annotations
    }

    private fun getAccessChecks(commandsClass: Class<out Commands>): List<AccessCheck> {
        val annotations = mutableListOf<AccessCheck>()
        commandsClass.getAnnotation(AccessCheck::class.java)?.let { annotations += it }
        commandsClass.getAnnotation(AccessChecks::class.java)?.let { annotations += it.accessCheckers }
        return annotations
    }

    private fun getDescription(commandMethod: Method): CommandDescription? =
            commandMethod.getAnnotation(Description::class.java)?.let { annotation ->
                val text = annotation.text.takeIf { it.isNotEmpty() }
                val textKey = annotation.textKey.takeIf { it.isNotEmpty() }?.let { TextKey(it) }
                if (text == null && textKey == null) {
                    null
                } else {
                    CommandDescription(text, textKey, textProvider)
                }
            }

    private fun getErrorHandler(commandsClass: Class<out Commands>): CommandErrorHandler? =
            commandsClass.getAnnotation(ErrorHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            }

    private fun getErrorHandler(commandMethod: Method): CommandErrorHandler? =
            commandMethod.getAnnotation(ErrorHandler::class.java)?.let {
                injector.getInstance(it.value.java)
            }
}