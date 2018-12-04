package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.view.View
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ViewNavigationElementFactoryTest {

    @ParameterizedTest
    @CsvSource(
            "true, false, false",
            "false, true, false",
            "false, false, true"
    )
    fun shouldCreateViewNavigationElement(allowManualNavigation: Boolean, useMouse: Boolean, destroyOnPop: Boolean) {
        val view = mockk<View>()
        val viewNavigationElementFactory = ViewNavigationElementFactory()

        val element = viewNavigationElementFactory.create(
                view = view,
                allowManualNavigation = allowManualNavigation,
                useMouse = useMouse,
                destroyOnPop = destroyOnPop,
                hoverColor = Colors.RED
        )

        assertThat(element)
                .isEqualTo(ViewNavigationElement(
                        view = view,
                        isManualNavigationAllowed = allowManualNavigation,
                        useMouse = useMouse,
                        destroyOnPop = destroyOnPop,
                        hoverColor = Colors.RED
                ))
    }

}