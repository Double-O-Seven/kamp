package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
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
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandGroup
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
import java.util.Collections.unmodifiableSet
import javax.inject.Inject

internal class CommandDefinitionLoader
@Inject
constructor(
        private val injector: Injector,
        private val textProvider: TextProvider,
        private val commandParameterResolverFactory: CommandParameterResolverFactory
) {

    private val allowedReturnTypes = setOf(
            Unit::class.java,
            Void::class.javaPrimitiveType!!,
            Boolean::class.javaObjectType,
            Boolean::class.javaPrimitiveType!!,
            OnPlayerCommandTextListener.Result::class.java,
            OnPlayerCommandTextListener.Result.Processed::class.java,
            OnPlayerCommandTextListener.Result.UnknownCommand::class.java
    )

    private val allowedCollectionTypes = setOf(
            Iterable::class.java,
            Collection::class.java,
            List::class.java,
            Set::class.java
    )

    fun load(commandsInstance: Commands): List<CommandDefinition> {
        val commandsClass = commandsInstance::class.java
        val commandAccessCheckerGroups = getAccessChecks(commandsClass).map {
            getCommandAccessCheckerGroup(it)
        }
        val commandErrorHandler = getErrorHandler(commandsClass)
        val commandMethods = getCommandMethods(commandsClass)
        val commandGroupName = commandsClass.getAnnotation(CommandGroup::class.java)?.name?.toLowerCase()
        val definitions = unmodifiableList(commandMethods.map {
            try {
                getCommandDefinition(
                        commandsInstance,
                        it,
                        commandAccessCheckerGroups,
                        commandErrorHandler,
                        commandGroupName
                )
            } catch (e: CommandDefinitionLoaderException) {
                throw CommandDefinitionLoaderException("Could not load definition for method $it", e)
            }
        })
        commandsInstance.definitions = definitions
        commandsInstance.groupName = commandGroupName
        return definitions
    }

    private fun getCommandMethods(commandsClass: Class<out Commands>): List<Method> {
        val commandMethods = mutableListOf<Method>()
        commandsClass.declaredMethods.filter { it.getAnnotation(Command::class.java) != null }.forEach {
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
            commandsInstance: Commands,
            commandMethod: Method,
            classAccessCheckers: List<CommandAccessCheckerGroup>,
            classCommandErrorHandler: CommandErrorHandler?,
            commandGroupName: String?
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
                groupName = commandGroupName,
                aliases = unmodifiableSet(aliases),
                description = description,
                commandsInstance = commandsInstance,
                method = commandMethod,
                parameters = unmodifiableList(parameters),
                isGreedy = isGreedy,
                isListed = isListed,
                errorHandler = errorHandler,
                accessCheckers = unmodifiableList(accessCheckers)
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
        if (allowedCollectionTypes.contains(parameter.type)) {
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
                    return commandParameterResolverFactory.getResolver(typeArgument)
                }
            }
            throw CommandDefinitionLoaderException("Cannot resolve type of parameter $parameter")
        } else {
            return commandParameterResolverFactory.getResolver(parameter.type)
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
        commandMethod.getAnnotation(AccessChecks::class.java)?.let { annotations += it.accessChecks }
        return annotations
    }

    private fun getAccessChecks(commandsClass: Class<out Commands>): List<AccessCheck> {
        val annotations = mutableListOf<AccessCheck>()
        commandsClass.getAnnotation(AccessCheck::class.java)?.let { annotations += it }
        commandsClass.getAnnotation(AccessChecks::class.java)?.let { annotations += it.accessChecks }
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