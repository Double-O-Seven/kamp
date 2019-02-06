package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ClickableViewTest {

    @Test
    fun shouldCallOnClickListener() {
        val view = TestClickableView(mockk(), mockk())
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.enable()
        view.addOnClickListener(listener)

        view.click()

        verify { listener.onClick(view) }
    }

    @Test
    fun givenItIsDisabledItShouldNotCallOnClickListener() {
        val view = TestClickableView(mockk(), mockk())
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.addOnClickListener(listener)
        view.disable()

        view.click()

        verify(exactly = 0) { listener.onClick(any()) }
    }

    @Test
    fun shouldNotCallRemovedListener() {
        val view = TestClickableView(mockk(), mockk())
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.addOnClickListener(listener)
        view.removeOnClickListener(listener)

        view.click()

        verify(exactly = 0) { listener.onClick(view) }
    }

    @Test
    fun shouldCallInlineOnClick() {
        val view = TestClickableView(mockk(), mockk())
        val clicked = mockk<(View) -> Unit>(relaxed = true)
        view.enable()
        view.onClick { clicked(this) }

        view.click()

        verify { clicked(view) }
    }

    @Test
    fun shouldCallOnClick() {
        val onClick = mockk<TestClickableView.() -> Unit>(relaxed = true)
        val view = TestClickableView(mockk(), mockk(), onClick = onClick)
        view.enable()

        view.click()

        verify { onClick(view) }
    }

    @Test
    fun givenViewIsEnabledClickShouldReturnProcessedAsResult() {
        val view = TestClickableView(mockk(), mockk())
        view.enable()

        val result = view.click()

        assertThat(result)
                .isEqualTo(OnPlayerClickPlayerTextDrawListener.Result.Processed)
    }

    @Test
    fun givenViewIsNotEnabledClickShouldReturnNotFoundAsResult() {
        val view = TestClickableView(mockk(), mockk())
        view.disable()

        val result = view.click()

        assertThat(result)
                .isEqualTo(OnPlayerClickPlayerTextDrawListener.Result.NotFound)
    }

    @Test
    fun givenListenerWasRemovedItShouldNotCallOnClick() {
        val view = TestClickableView(mockk(), mockk())
        val clicked = mockk<() -> Unit>(relaxed = true)
        val listener = view.onClick { clicked() }
        view.removeOnClickListener(listener)

        view.click()

        verify(exactly = 0) { clicked() }
    }

    @Nested
    inner class EnableTests {

        @Test
        fun shouldInitiallyBeDisabled() {
            val view = TestClickableView(mockk(), mockk())

            val isEnabled = view.isEnabled

            assertThat(isEnabled)
                    .isFalse()
        }

        @Test
        fun shouldCallOnEnable() {
            val onEnable = mockk<TestClickableView.() -> Unit>(relaxed = true)
            val view = TestClickableView(mockk(), mockk(), onEnable = onEnable)
            view.disable()

            view.enable()

            verify { onEnable.invoke(view) }
        }

        @Test
        fun givenViewIsEnabledItShouldNotCallOnEnableAgain() {
            val onEnable = mockk<TestClickableView.() -> Unit>(relaxed = true)
            val view = TestClickableView(mockk(), mockk(), onEnable = onEnable)
            view.enable()

            view.enable()

            verify(exactly = 1) { onEnable.invoke(view) }
        }

    }

    @Nested
    inner class OnDisableTests {

        @Test
        fun shouldCallOnDisable() {
            val onDisable = mockk<TestClickableView.() -> Unit>(relaxed = true)
            val view = TestClickableView(mockk(), mockk(), onDisable = onDisable)
            view.enable()

            view.disable()

            verify { onDisable.invoke(view) }
        }

        @Test
        fun givenViewIsDisabledItShouldNotCallOnDisableAgain() {
            val onDisable = mockk<TestClickableView.() -> Unit>(relaxed = true)
            val view = TestClickableView(mockk(), mockk(), onDisable = onDisable)

            view.disable()

            verify(exactly = 0) { onDisable.invoke(view) }
        }

    }

    private class TestClickableView(
            player: Player,
            viewContext: ViewContext,
            private val onEnable: TestClickableView.() -> Unit = {},
            private val onDisable: TestClickableView.() -> Unit = {},
            private val onClick: TestClickableView.() -> Unit = {}
    ) : ClickableView(player, viewContext) {

        override fun onEnable() {
            onEnable.invoke(this)
        }

        override fun onDisable() {
            onDisable.invoke(this)
        }

        override fun onClick() {
            onClick.invoke(this)
        }

    }

}