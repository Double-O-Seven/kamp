package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogItem
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

internal class DefaultListDialogItemTest {

    private lateinit var builder: DefaultListDialogItem.Builder<Int>

    private val textProvider = mockk<TextProvider>()
    private val player = mockk<Player>()
    private val locale = Locale.GERMANY

    @BeforeEach
    fun setUp() {
        every { player.locale } returns locale
        builder = DefaultListDialogItem.Builder(textProvider)
    }

    @Nested
    inner class ContentTests {

        @Test
        fun shouldBuildListDialogItemWithStringContent() {
            val listDialogItem = builder.apply {
                value(1337)
                content("Hi there")
            }.build()

            val content = listDialogItem.getContent(player)

            assertThat(content)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldBuildListDialogItemWithTextKeyedContent() {
            val textKey = TextKey("test.hi.there")
            every {
                textProvider.getText(locale, textKey)
            } returns "Hi there"
            val listDialogItem = builder.apply {
                value(1337)
                content(textKey)
            }.build()

            val content = listDialogItem.getContent(player)

            assertThat(content)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldBuildListDialogItemWithFunctionalContent() {
            val listDialogItem = builder.apply {
                value(1337)
                content { "Hi there" }
            }.build()

            val content = listDialogItem.getContent(player)

            assertThat(content)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldBuildListDialogItemWithSupplierContent() {
            val supplier = object : DialogTextSupplier {

                override fun getText(player: Player): String = "Hi there"

            }
            val listDialogItem = builder.apply {
                value(1337)
                content(supplier)
            }.build()

            val content = listDialogItem.getContent(player)

            assertThat(content)
                    .isEqualTo("Hi there")
        }
    }

    @Test
    fun shouldBuildListDialogItemWithValue() {
        val listDialogItem = builder.apply {
            value(1337)
            content("Hi there")
        }.build()

        val value = listDialogItem.value

        assertThat(value)
                .isEqualTo(value)
    }

    @Test
    fun shouldBuildListDialogItemWithOnySelect() {
        val onSelect = mockk<OnSelect>(relaxed = true)
        val listDialogItem = builder.apply {
            value(1337)
            content("Hi there")
            onSelect { player, inputText -> onSelect.onSelect(this, player, inputText) }
        }.build()

        listDialogItem.onSelect(player, "Test")

        verify { onSelect.onSelect(listDialogItem, player, "Test") }
    }

    private interface OnSelect {

        fun onSelect(item: ListDialogItem<Int>, player: Player, inputText: String)

    }

}