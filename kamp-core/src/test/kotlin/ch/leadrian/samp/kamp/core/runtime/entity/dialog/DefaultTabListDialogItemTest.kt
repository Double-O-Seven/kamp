package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogItem
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class DefaultTabListDialogItemTest {

    private lateinit var builder: DefaultTabListDialogItem.Builder<Int>

    private val textProvider = mockk<TextProvider>()
    private val player = mockk<Player>()
    private val locale = Locale.GERMANY

    @BeforeEach
    fun setUp() {
        every { player.locale } returns locale
        builder = DefaultTabListDialogItem.Builder(textProvider)
    }

    @Nested
    inner class ContentTests {

        @Test
        fun shouldBuildListDialogItemWithStringContent() {
            val tabListDialogItem = builder.apply {
                value(1337)
                tabbedContent("Hi there", "How are you?")
            }.build()

            val tabbedContent = tabListDialogItem.getTabbedContent(player)

            assertThat(tabbedContent)
                    .containsExactly("Hi there", "How are you?")
        }

        @Test
        fun shouldBuildListDialogItemWithTextKeyedContent() {
            val textKey1 = TextKey("test.hi.there")
            val textKey2 = TextKey("test.how.are.you")
            every {
                textProvider.getText(locale, textKey1)
            } returns "Hi there"
            every {
                textProvider.getText(locale, textKey2)
            } returns "How are you?"
            val tabListDialogItem = builder.apply {
                value(1337)
                tabbedContent(textKey1, textKey2)
            }.build()

            val tabbedContent = tabListDialogItem.getTabbedContent(player)

            assertThat(tabbedContent)
                    .containsExactly("Hi there", "How are you?")
        }

        @Test
        fun shouldBuildListDialogItemWithFunctionalContent() {
            val tabListDialogItem = builder.apply {
                value(1337)
                tabbedContent({ "Hi there" }, { "How are you?" })
            }.build()

            val tabbedContent = tabListDialogItem.getTabbedContent(player)

            assertThat(tabbedContent)
                    .containsExactly("Hi there", "How are you?")
        }

        @Test
        fun shouldBuildListDialogItemWithSupplierContent() {
            val supplier1 = object : DialogTextSupplier {

                override fun getText(player: Player): String = "Hi there"

            }
            val supplier2 = object : DialogTextSupplier {

                override fun getText(player: Player): String = "How are you?"

            }
            val tabListDialogItem = builder.apply {
                value(1337)
                tabbedContent(supplier1, supplier2)
            }.build()

            val tabbedContent = tabListDialogItem.getTabbedContent(player)

            assertThat(tabbedContent)
                    .containsExactly("Hi there", "How are you?")
        }
    }

    @Test
    fun shouldBuildListDialogItemWithValue() {
        val tabListDialogItem = builder.apply {
            value(1337)
            tabbedContent("Hi there")
        }.build()

        val value = tabListDialogItem.value

        assertThat(value)
                .isEqualTo(value)
    }

    @Test
    fun shouldBuildListDialogItemWithOnySelect() {
        val onSelect = mockk<OnSelect>(relaxed = true)
        val tabListDialogItem = builder.apply {
            value(1337)
            tabbedContent("Hi there")
            onSelect { player, inputText -> onSelect.onSelect(this, player, inputText) }
        }.build()

        tabListDialogItem.onSelect(player, "Test")

        verify { onSelect.onSelect(tabListDialogItem, player, "Test") }
    }

    private interface OnSelect {

        fun onSelect(item: TabListDialogItem<Int>, player: Player, inputText: String)

    }

}