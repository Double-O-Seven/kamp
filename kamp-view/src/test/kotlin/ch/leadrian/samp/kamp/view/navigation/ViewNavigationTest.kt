package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.view.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ViewNavigationTest {

    private lateinit var viewNavigation: ViewNavigation

    private val viewNavigationElementFactory = mockk<ViewNavigationElementFactory>()
    private val view = mockk<View>()

    @BeforeEach
    fun setUp() {
        viewNavigation = ViewNavigation(viewNavigationElementFactory)
    }

    @Test
    fun givenEmptyViewNavigationTopShouldBeNull() {
        val top = viewNavigation.top

        assertThat(top)
                .isNull()
    }

    @Test
    fun givenEmptyViewNavigationSizeShouldBeZero() {
        val size = viewNavigation.size

        assertThat(size)
                .isZero()
    }

    @Test
    fun givenEmptyViewNavigationItShouldBeEmpty() {
        val isEmpty = viewNavigation.isEmpty

        assertThat(isEmpty)
                .isTrue()
    }

    @Nested
    inner class IsManualNavigationAllowedTests {

        @Test
        fun givenEmptyViewNavigationItShouldReturnTrue() {
            val isManualNavigationAllowed = viewNavigation.isManualNavigationAllowed

            assertThat(isManualNavigationAllowed)
                    .isTrue()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenViewWasPushedItShouldReturnWhetherManualNavigationIsAllowed(expectedIsManualNavigationAllowed: Boolean) {
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { isManualNavigationAllowed } returns expectedIsManualNavigationAllowed
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any(), any(), any()) } returns viewNavigationElement
            viewNavigation.push(view)

            val isManualNavigationAllowed = viewNavigation.isManualNavigationAllowed

            assertThat(isManualNavigationAllowed)
                    .isEqualTo(expectedIsManualNavigationAllowed)
        }
    }

    @Nested
    inner class PushTests {

    }

}