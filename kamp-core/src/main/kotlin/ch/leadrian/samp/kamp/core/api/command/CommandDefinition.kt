package ch.leadrian.samp.kamp.core.api.command

import java.lang.reflect.Method

data class CommandDefinition
internal constructor(
        val name: String,
        val aliases: Set<String>,
        val description: CommandDescription?,
        val method: Method,
        val parameters: List<CommandParameterDefinition>,
        val isListed: Boolean,
        val isGreedy: Boolean,
        val errorHandler: CommandErrorHandler?,
        val accessCheckers: List<CommandAccessCheckerGroup>
)