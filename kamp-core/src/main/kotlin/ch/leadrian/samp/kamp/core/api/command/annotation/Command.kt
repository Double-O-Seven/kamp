package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * Defines a function of a [ch.leadrian.samp.kamp.core.api.command.Commands] implementation as a command implementation.
 *
 * Examples:
 * ```
 * @Command(name = "pm", isGreedy = true)
 * fun sendPrivateMessage(player: Player, receiver: Player, message: String) {
 *     // command implementation
 * }
 * ```
 *
 * ```
 * @Command(aliases = ["tp"])
 * fun teleport(player: Player, target: Player) {
 *     // command implementation
 * }
 * ```
 *
 * @see [AccessCheck]
 * @see [AccessChecks]
 * @see [Description]
 * @see [ErrorHandler]
 * @see [InvalidParameterValueHandler]
 * @see [Parameter]
 * @see [Unlisted]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
        /**
         * If not empty, overrides the command name derived from the implementing function.
         * Else, the lowercase function name will be used as command name.
         */
        val name: String = "",
        val aliases: Array<String> = [],
        /**
         * If true, the last parameters of the command text will be grouped together into a single command parameter value,
         * else, unneeded parameter values be ignored.
         */
        val isGreedy: Boolean = true
)