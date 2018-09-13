package ch.leadrian.samp.kamp.core.runtime.command

import org.codehaus.plexus.util.cli.CommandLineException
import org.codehaus.plexus.util.cli.CommandLineUtils
import javax.inject.Inject

internal class CommandParser
@Inject
constructor() {

    fun parse(commandLine: String): ParsedCommand? {
        if (commandLine.isEmpty()) {
            return null
        }
        val tokens = try {
            CommandLineUtils.translateCommandline(commandLine.substring(1))
        } catch (e: CommandLineException) {
            return null
        }
        return when {
            tokens.isEmpty() -> null
            else -> ParsedCommand(
                    command = tokens[0],
                    parameterValues = tokens.drop(1)
            )
        }
    }

}