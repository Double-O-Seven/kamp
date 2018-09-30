package ch.leadrian.samp.kamp.core.api.command

import java.lang.reflect.Method

data class CommandDefinition
internal constructor(
        val name: String,
        val groupName: String? = null,
        val aliases: Set<String> = emptySet(),
        val description: CommandDescription? = null,
        val commandsInstance: Commands,
        val method: Method,
        val parameters: List<CommandParameterDefinition>,
        val isListed: Boolean = true,
        val isGreedy: Boolean = true,
        val errorHandler: CommandErrorHandler? = null,
        val accessCheckers: List<CommandAccessCheckerGroup> = emptyList()
) {

    val nameAndAliases: Set<String>
        get() {
            val nameAndAliases = mutableSetOf(name)
            nameAndAliases += aliases
            return nameAndAliases
        }

}
