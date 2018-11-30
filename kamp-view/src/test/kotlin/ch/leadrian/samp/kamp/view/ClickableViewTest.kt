package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ClickableViewTest {

    private val view = TestClickableView(mockk(), mockk())

    @Test
    fun shouldInitiallyBeEnabled() {
        val isEnabled = view.isEnabled

        assertThat(isEnabled)
                .isTrue()
    }

    @Test
    fun shouldCallOnClickListener() {
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.addOnClickListener(listener)

        view.click()

        verify { listener.onClick(view) }
    }

    @Test
    fun givenItIsDisabledItShouldNotCallOnClickListener() {
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.addOnClickListener(listener)
        view.isEnabled = false

        view.click()

        verify { listener wasNot Called }
    }

    @Test
    fun shouldNotCallRemovedListener() {
        val listener = mockk<OnClickViewListener>(relaxed = true)
        view.addOnClickListener(listener)
        view.removeOnClickListener(listener)

        view.click()

        verify(exactly = 0) { listener.onClick(view) }
    }

    @Test
    fun shouldCallOnClick() {
        val clicked = mockk<(View) -> Unit>(relaxed = true)
        view.onClick { clicked(this) }

        view.click()

        verify { clicked(view) }
    }

    @Test
    fun givenListenerWasRemovedItShouldNotCallOnClick() {
        val clicked = mockk<() -> Unit>(relaxed = true)
        val listener = view.onClick { clicked() }
        view.removeOnClickListener(listener)

        view.click()

        verify(exactly = 0) { clicked() }
    }

    private class TestClickableView(player: Player, viewLayoutCalculator: ViewLayoutCalculator) : ClickableView(player, viewLayoutCalculator) {

        override fun draw(layout: Rectangle) {}

        override fun onShow() {}

        override fun onHide() {}

    }

}