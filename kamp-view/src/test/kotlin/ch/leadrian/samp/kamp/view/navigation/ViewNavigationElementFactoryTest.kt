package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.view.base.View
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ViewNavigationElementFactoryTest {

    @ParameterizedTest
    @CsvSource(
            "true, false",
            "false, true"
    )
    fun shouldCreateViewNavigationElement(allowManualNavigation: Boolean, useMouse: Boolean) {
        val view = mockk<View>()
        val viewNavigationElementFactory = ViewNavigationElementFactory()

        val element = viewNavigationElementFactory.create(
                view = view,
                allowManualNavigation = allowManualNavigation,
                useMouse = useMouse
        )

        assertThat(element)
                .isEqualTo(ViewNavigationElement(
                        view = view,
                        allowManualNavigation = allowManualNavigation,
                        useMouse = useMouse
                ))
    }

}