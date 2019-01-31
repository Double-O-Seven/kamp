package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * Groups all commands implemented in a [ch.leadrian.samp.kamp.core.api.command.Commands] implementation.
 * Each command must be written with the common prefix.
 *
 * Example:
 * ```
 * @Singleton
 * @CommandGroup("gang")
 * class GangCommands : Commands() {
 *
 *     // Invoked by /gang join [player] [gangId]
 *     @Command
 *     fun join(player: Player, gangId: Int) {
 *         // implementation
 *     }
 *
 *     // Invoked by /gang leave [player] [gangId]
 *     @Command
 *     fun leave(player: Player, gangId: Int) {
 *         // implementation
 *     }
 *
 * }
 * ```
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class CommandGroup(val name: String)