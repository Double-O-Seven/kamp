package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandRegistry
@Inject
constructor(
        private val commands: Set<@JvmSuppressWildcards Commands>,
        private val commandDefinitionLoader: CommandDefinitionLoader
) {

    private companion object {

        val log = loggerFor<CommandRegistry>()

    }

    private val entriesByName: MutableMap<String, Entry> = mutableMapOf()

    @PostConstruct
    fun initialize() {
        commands.flatMap { commandDefinitionLoader.load(it) }.forEach {
            if (it.groupName != null) {
                addCommandToGroup(it, it.groupName)
            } else {
                addSingleCommand(it)
            }
        }
        commands.forEach { log.info("Loading commands from {}", it::class.qualifiedName) }
    }

    fun getCommandDefinition(name: String, firstParameterValue: String?): CommandDefinition? {
        val entry = entriesByName[name]
        return when {
            entry is Entry.CommandGroup && firstParameterValue != null -> {
                entry.commandDefinitionsByName[firstParameterValue]
            }
            entry is Entry.SingleCommand -> entry.commandDefinition
            else -> null
        }
    }

    private fun addCommandToGroup(definition: CommandDefinition, groupName: String) {
        definition.nameAndAliases.forEach { nameOrAlias ->
            val existingEntry = entriesByName[groupName]
            when (existingEntry) {
                is Entry.CommandGroup -> {
                    existingEntry.commandDefinitionsByName.merge(nameOrAlias, definition) { _, _ ->
                        throw IllegalStateException("Duplicate command $nameOrAlias within group ${definition.groupName}")
                    }
                }
                is Entry.SingleCommand -> {
                    throw IllegalStateException("Command and command group with same name: ${definition.groupName}")
                }
                else -> {
                    entriesByName[groupName] = Entry.CommandGroup().apply {
                        commandDefinitionsByName[nameOrAlias] = definition
                    }
                }
            }
        }
    }

    private fun addSingleCommand(definition: CommandDefinition) {
        definition.nameAndAliases.forEach { nameOrAlias ->
            entriesByName.merge(nameOrAlias, Entry.SingleCommand(definition)) { _, _ ->
                throw IllegalStateException("Duplicate command or command group with name or alias $nameOrAlias")
            }
        }
    }

    private sealed class Entry {

        class CommandGroup : Entry() {

            val commandDefinitionsByName: MutableMap<String, CommandDefinition> = mutableMapOf()

        }

        class SingleCommand(val commandDefinition: CommandDefinition) : Entry()

    }

}