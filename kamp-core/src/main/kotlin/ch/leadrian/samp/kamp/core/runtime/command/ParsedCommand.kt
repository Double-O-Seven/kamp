package ch.leadrian.samp.kamp.core.runtime.command

data class ParsedCommand(
        val command: String,
        val parameterValues: List<String> = emptyList()
)