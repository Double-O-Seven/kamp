package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.runtime.command.CommandListDialogFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CommandsTest {

    private val commands = object : Commands() {}

    private val commandListDialogFactory = mockk<CommandListDialogFactory>()

    @BeforeEach
    fun setUp() {
        commands.commandListDialogFactory = commandListDialogFactory
    }

    @Nested
    inner class ShowCommandListTests {

        private val player = mockk<Player>()
        private val dialogNavigation = mockk<DialogNavigation>()
        private val dialog = mockk<Dialog>()

        @BeforeEach
        fun setUp() {
            every { player.dialogNavigation } returns dialogNavigation
            every { commandListDialogFactory.create(commands, 30) } returns dialog
        }

        @Test
        fun shouldShowCommandListAsRoot() {
            every { dialogNavigation.setRoot(any()) } just Runs

            commands.showCommandList(player, true)

            verify { dialogNavigation.setRoot(dialog) }
        }

        @Test
        fun shouldShowCommandListAsNextNavigationElement() {
            every { dialogNavigation.push(any()) } just Runs

            commands.showCommandList(player, false)

            verify { dialogNavigation.push(dialog) }
        }
    }

}