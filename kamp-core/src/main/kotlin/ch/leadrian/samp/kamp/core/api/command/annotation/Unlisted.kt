package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * If a [Command] is annotated with [Unlisted], it will not appear in the command list shown
 * through [ch.leadrian.samp.kamp.core.api.command.Commands.showCommandList].
 * This will be useful for hiding the command list command itself.
 *
 * Examples:
 * ```
 * @Unlisted
 * @Command
 * fun cmds(player: Player) {
 *     showCommandList(player)
 * }
 *
 * @Unlisted
 * @Command
 * fun superSecretCommand(player: Player) {
 *     // Pssst!!!
 * }
 * ```
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Unlisted