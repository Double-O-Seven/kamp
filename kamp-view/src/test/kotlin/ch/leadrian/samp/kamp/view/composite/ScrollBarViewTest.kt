package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.view.style.ScrollBarStyle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal interface ScrollBarViewTest<V : ScrollBarView> {

    fun getScrollBarView(): V

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSetColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.color = color

        assertThat(scrollBarView.color)
                .isEqualTo(color)
    }

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSupplyColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.color { color }

        assertThat(scrollBarView.color)
                .isEqualTo(color)
    }

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSetBackgroundColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.backgroundColor = color

        assertThat(scrollBarView.backgroundColor)
                .isEqualTo(color)
    }

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSupplyBackgroundColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.backgroundColor { color }

        assertThat(scrollBarView.backgroundColor)
                .isEqualTo(color)
    }

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSetButtonColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.buttonColor = color

        assertThat(scrollBarView.buttonColor)
                .isEqualTo(color)
    }

    @JvmDefault
    @ParameterizedTest
    @ArgumentsSource(ColorArgumentsProvider::class)
    fun shouldSupplyButtonColor(color: Color) {
        val scrollBarView = getScrollBarView()

        scrollBarView.buttonColor { color }

        assertThat(scrollBarView.buttonColor)
                .isEqualTo(color)
    }

    @Test
    fun shouldApplyStyle() {
        val scrollBarStyle = object : ScrollBarStyle {
            override val scrollBarColor: Color
                get() = Colors.PINK
            override val scrollBarBackgroundColor: Color
                get() = Colors.LIGHT_YELLOW
            override val scrollBarButtonColor: Color
                get() = Colors.AQUA
        }
        val scrollBarView = getScrollBarView()

        scrollBarView.style(scrollBarStyle)

        assertThat(scrollBarView)
                .satisfies {
                    assertThat(it.color)
                            .isEqualTo(Colors.PINK)
                    assertThat(it.backgroundColor)
                            .isEqualTo(Colors.LIGHT_YELLOW)
                    assertThat(it.buttonColor)
                            .isEqualTo(Colors.AQUA)
                }
    }

    class ColorArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(Colors.PINK),
                        Arguments.of(Colors.CYAN),
                        Arguments.of(Colors.RED)
                )

    }

}