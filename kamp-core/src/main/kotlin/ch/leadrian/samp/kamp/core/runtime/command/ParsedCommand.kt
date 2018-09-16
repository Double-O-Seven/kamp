package ch.leadrian.samp.kamp.core.runtime.command

internal data class ParsedCommand(
        val command: String,
        val parameterValues: List<String> = emptyList()
)