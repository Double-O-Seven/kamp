package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.view.base.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class ViewNavigationTest {

    private lateinit var viewNavigation: ViewNavigation

    private val viewNavigationElementFactory = mockk<ViewNavigationElementFactory>()

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
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { allowManualNavigation } returns expectedIsManualNavigationAllowed
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement
            viewNavigation.push(view)

            val isManualNavigationAllowed = viewNavigation.isManualNavigationAllowed

            assertThat(isManualNavigationAllowed)
                    .isEqualTo(expectedIsManualNavigationAllowed)
        }
    }

    @Nested
    inner class IsMouseUsedTests {

        @Test
        fun givenEmptyViewNavigationItShouldReturnFalse() {
            val isMouseUsed = viewNavigation.isMouseUsed

            assertThat(isMouseUsed)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenViewWasPushedItShouldReturnWhetherManualNavigationIsAllowed(expectedIsMouseUsed: Boolean) {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { useMouse } returns expectedIsMouseUsed
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement
            viewNavigation.push(view)

            val isMouseUsed = viewNavigation.isMouseUsed

            assertThat(isMouseUsed)
                    .isEqualTo(expectedIsMouseUsed)
        }
    }

    @Nested
    inner class PushTests {

        @ParameterizedTest
        @CsvSource(
                "true, false",
                "false, true"
        )
        fun shouldCreateViewNavigationElement(allowManualNavigation: Boolean, useMouse: Boolean) {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.push(
                    view = view,
                    allowManualNavigation = allowManualNavigation,
                    useMouse = useMouse
            )

            verify {
                viewNavigationElementFactory.create(
                        view = view,
                        allowManualNavigation = allowManualNavigation,
                        useMouse = useMouse
                )
            }
        }

        @Test
        fun shouldNavigateToViewNavigationElement() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.push(view)

            verify { viewNavigationElement.navigateTo() }
        }

        @Test
        fun shouldPushViewToTheTop() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.push(view)

            assertThat(viewNavigation.top)
                    .isSameAs(view)
        }

        @Test
        fun givenEmptyNavigationItShouldIncreaseSizeToOne() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.push(view)

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenEmptyNavigationItNotBeEmptyAfterwards() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.push(view)

            assertThat(viewNavigation.isEmpty)
                    .isFalse()
        }

        @Test
        fun givenNonEmptyNavigationItShouldHideTop() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)

            viewNavigation.push(view2)

            verifyOrder {
                viewNavigationElement1.navigateTo()
                view1.hide()
                viewNavigationElement2.navigateTo()
            }
        }

        @Test
        fun givenNonEmptyNavigationItShouldIncreaseSizeToTwo() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)

            viewNavigation.push(view2)

            assertThat(viewNavigation.size)
                    .isEqualTo(2)
        }

        @Test
        fun givenNonEmptyNavigationItShouldPushViewToTop() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)

            viewNavigation.push(view2)

            assertThat(viewNavigation.top)
                    .isSameAs(view2)
        }
    }

    @Nested
    inner class ReplaceTopTests {

        @ParameterizedTest
        @CsvSource(
                "true, false",
                "false, true"
        )
        fun shouldCreateViewNavigationElement(allowManualNavigation: Boolean, useMouse: Boolean) {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.replaceTop(
                    view = view,
                    allowManualNavigation = allowManualNavigation,
                    useMouse = useMouse
            )

            verify {
                viewNavigationElementFactory.create(
                        view = view,
                        allowManualNavigation = allowManualNavigation,
                        useMouse = useMouse
                )
            }
        }

        @Test
        fun shouldNavigateToViewNavigationElement() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.replaceTop(view)

            verify { viewNavigationElement.navigateTo() }
        }

        @Test
        fun shouldReplaceTopViewToTheTop() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.replaceTop(view)

            assertThat(viewNavigation.top)
                    .isSameAs(view)
        }

        @Test
        fun givenEmptyNavigationItShouldIncreaseSizeToOne() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.replaceTop(view)

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenEmptyNavigationItNotBeEmptyAfterwards() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.replaceTop(view)

            assertThat(viewNavigation.isEmpty)
                    .isFalse()
        }

        @Test
        fun givenNonEmptyNavigationItShouldPopTop() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)

            viewNavigation.replaceTop(view2)

            verifyOrder {
                viewNavigationElement1.navigateTo()
                view1.hide()
                viewNavigationElement2.navigateTo()
            }
        }

        @Test
        fun givenNonEmptyNavigationItShouldKeepSizeOfOne() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)

            viewNavigation.replaceTop(view2)

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenNonEmptyNavigationItShouldReplaceTopView() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.replaceTop(view1)

            viewNavigation.replaceTop(view2)

            assertThat(viewNavigation.top)
                    .isSameAs(view2)
        }
    }

    @Nested
    inner class SetRootTests {

        @ParameterizedTest
        @CsvSource(
                "true, false",
                "false, true"
        )
        fun shouldCreateViewNavigationElement(allowManualNavigation: Boolean, useMouse: Boolean) {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.setRoot(
                    view = view,
                    allowManualNavigation = allowManualNavigation,
                    useMouse = useMouse
            )

            verify {
                viewNavigationElementFactory.create(
                        view = view,
                        allowManualNavigation = allowManualNavigation,
                        useMouse = useMouse
                )
            }
        }

        @Test
        fun shouldNavigateToViewNavigationElement() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.setRoot(view)

            verify { viewNavigationElement.navigateTo() }
        }

        @Test
        fun shouldSetRootViewToTheTop() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.setRoot(view)

            assertThat(viewNavigation.top)
                    .isSameAs(view)
        }

        @Test
        fun givenEmptyNavigationItShouldIncreaseSizeToOne() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.setRoot(view)

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenEmptyNavigationItNotBeEmptyAfterwards() {
            val view = mockk<View>()
            val viewNavigationElement = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every { viewNavigationElementFactory.create(any(), any(), any()) } returns viewNavigationElement

            viewNavigation.setRoot(view)

            assertThat(viewNavigation.isEmpty)
                    .isFalse()
        }

        @Test
        fun givenNonEmptyNavigationItShouldPopAllExistingElements() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val view3 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            val viewNavigationElement3 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2, viewNavigationElement3)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.setRoot(view3)

            verifyOrder {
                viewNavigationElement1.navigateTo()
                view1.hide()
                viewNavigationElement2.navigateTo()
                view1.hide()
                view2.hide()
                viewNavigationElement3.navigateTo()
            }
        }

        @Test
        fun givenNonEmptyNavigationItShouldResetToSizeOne() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val view3 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            val viewNavigationElement3 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2, viewNavigationElement3)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.setRoot(view3)

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenNonEmptyNavigationItShouldSetTop() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val view3 = mockk<View>()
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            val viewNavigationElement3 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view3
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2, viewNavigationElement3)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.setRoot(view3)

            assertThat(viewNavigation.top)
                    .isSameAs(view3)
        }
    }

    @Nested
    inner class PopTests {

        @Test
        fun givenEmptyViewNavigationItShouldDoNothing() {
            val caughtThrowable = catchThrowable { viewNavigation.pop() }

            assertThat(caughtThrowable)
                    .isNull()
        }

        @Test
        fun givenNonEmptyViewNavigationItShouldPopTop() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.pop()

            verify { view2.hide() }
        }

        @Test
        fun givenNonEmptyViewNavigationItDecreaseSize() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.pop()

            assertThat(viewNavigation.size)
                    .isOne()
        }

        @Test
        fun givenNonEmptyViewNavigationTopShouldBePreviousView() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.pop()

            assertThat(viewNavigation.top)
                    .isSameAs(view1)
        }

    }

    @Nested
    inner class ClearTests {

        @Test
        fun shouldPopAllElements() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.clear()

            verify {
                view1.hide()
                view2.hide()
            }
        }

        @Test
        fun shouldBeEmptyAfterClear() {
            val view1 = mockk<View> {
                every { hide() } just Runs
            }
            val view2 = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.clear()

            assertThat(viewNavigation.isEmpty)
                    .isTrue()
            assertThat(viewNavigation.size)
                    .isZero()
        }

    }

    @Nested
    inner class DestroyTests {

        @Test
        fun shouldInitiallyNotBeDestroyed() {
            val isDestroyed = viewNavigation.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldDestroyViews() {
            val view1 = mockk<View> {
                every { hide() } just Runs
                every { destroy() } just Runs
            }
            val view2 = mockk<View> {
                every { destroy() } just Runs
            }
            val viewNavigationElement1 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view1
            }
            val viewNavigationElement2 = mockk<ViewNavigationElement> {
                every { navigateTo() } just Runs
                every { this@mockk.view } returns view2
            }
            every {
                viewNavigationElementFactory.create(any(), any(), any())
            } returnsMany listOf(viewNavigationElement1, viewNavigationElement2)
            viewNavigation.push(view1)
            viewNavigation.push(view2)

            viewNavigation.destroy()

            verify {
                view1.destroy()
                view2.destroy()
            }
        }

        @Test
        fun givenDestroyWasCalledItShouldBeDestroyed() {
            viewNavigation.destroy()

            val isDestroyed = viewNavigation.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }
    }

    @Test
    fun shouldReturnViewNavigationAsExtension() {
        val extensions = mockk<EntityExtensionContainer<Player>> {
            every { get(ViewNavigation::class) } returns viewNavigation
        }
        val player = mockk<Player> {
            every { this@mockk.extensions } returns extensions
        }

        val extension = player.viewNavigation

        assertThat(extension)
                .isEqualTo(viewNavigation)
    }

}