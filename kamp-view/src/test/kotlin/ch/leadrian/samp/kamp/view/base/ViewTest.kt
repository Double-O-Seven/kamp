package ch.leadrian.samp.kamp.view.base


import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.ViewLayout
import ch.leadrian.samp.kamp.view.layout.ViewLayoutCalculator
import ch.leadrian.samp.kamp.view.layout.pixels
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class ViewTest {

    private val player = mockk<Player>(relaxed = true)
    private val viewContext = mockk<ViewContext>()
    private val viewLayoutCalculator = mockk<ViewLayoutCalculator>()

    @BeforeEach
    fun setUp() {
        every { viewContext.viewLayoutCalculator } returns viewLayoutCalculator
    }

    @Nested
    inner class ParentAreaTests {

        @Test
        fun givenNoParentItShouldReturnScreenArea() {
            val view = TestView(player, viewContext)
            val parentArea = view.parentArea

            assertThat(parentArea)
                    .isEqualTo(rectangleOf(0f, 640f, 0f, 480f))
        }

        @Test
        fun givenParentItShouldReturnContentAreaOfParent() {
            val expectedParentArea = rectangleOf(13f, 37f, 0.8f, 15f)
            val parentView = spyk(TestView(player, viewContext)) {
                every { contentArea } returns expectedParentArea
            }
            val view = TestView(player, viewContext)
            parentView.addChild(view)
            val parentArea = view.parentArea

            assertThat(parentArea)
                    .isEqualTo(expectedParentArea)
        }

    }

    @Test
    fun shouldSetPadding() {
        val view = TestView(player, viewContext)

        view.setPadding(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.paddingLeft)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingRight)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingTop)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingBottom)
                            .isEqualTo(10.pixels())
                }
    }

    @Test
    fun shouldSetHorizontalPadding() {
        val view = TestView(player, viewContext)

        view.setHorizontalPadding(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.paddingLeft)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingRight)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingTop)
                            .isEqualTo(0.pixels())
                    assertThat(it.paddingBottom)
                            .isEqualTo(0.pixels())
                }
    }

    @Test
    fun shouldSetVerticalPadding() {
        val view = TestView(player, viewContext)

        view.setVerticalPadding(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.paddingLeft)
                            .isEqualTo(0.pixels())
                    assertThat(it.paddingRight)
                            .isEqualTo(0.pixels())
                    assertThat(it.paddingTop)
                            .isEqualTo(10.pixels())
                    assertThat(it.paddingBottom)
                            .isEqualTo(10.pixels())
                }
    }

    @Test
    fun shouldSetMargin() {
        val view = TestView(player, viewContext)

        view.setMargin(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.marginLeft)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginRight)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginTop)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginBottom)
                            .isEqualTo(10.pixels())
                }
    }

    @Test
    fun shouldSetHorizontalMargin() {
        val view = TestView(player, viewContext)

        view.setHorizontalMargin(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.marginLeft)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginRight)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginTop)
                            .isEqualTo(0.pixels())
                    assertThat(it.marginBottom)
                            .isEqualTo(0.pixels())
                }
    }

    @Test
    fun shouldSetVerticalMargin() {
        val view = TestView(player, viewContext)

        view.setVerticalMargin(10.pixels())

        assertThat(view)
                .satisfies {
                    assertThat(it.marginLeft)
                            .isEqualTo(0.pixels())
                    assertThat(it.marginRight)
                            .isEqualTo(0.pixels())
                    assertThat(it.marginTop)
                            .isEqualTo(10.pixels())
                    assertThat(it.marginBottom)
                            .isEqualTo(10.pixels())
                }
    }

    @Nested
    inner class AddChildTests {

        private lateinit var parentView: TestView

        @BeforeEach
        fun setUp() {
            parentView = TestView(player, viewContext)
        }

        @Test
        fun shouldAddChild() {
            val childView = TestView(player, viewContext)

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun shouldAddChildrenFromIterable() {
            val childView1 = TestView(player, viewContext)
            val childView2 = TestView(player, viewContext)

            parentView.addChildren(listOf(childView1, childView2))

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldAddChildrenFromVarargs() {
            val childView1 = TestView(player, viewContext)
            val childView2 = TestView(player, viewContext)

            parentView.addChildren(childView1, childView2)

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldSetViewAsParentOfChild() {
            val childView = TestView(player, viewContext)

            parentView.addChild(childView)

            assertThat(childView.parent)
                    .isSameAs(parentView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldAddChild() {
            val childView = TestView(player, viewContext)
            TestView(player, viewContext).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldRemoveChildFromOldParent() {
            val childView = TestView(player, viewContext)
            val otherParentView = TestView(player, viewContext).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(otherParentView.children)
                    .isEmpty()
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldChangeParent() {
            val childView = TestView(player, viewContext)
            TestView(player, viewContext).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(childView.parent)
                    .isSameAs(parentView)
        }

    }

    @Nested
    inner class RemoveChildTests {

        @Test
        fun shouldRemoveChild() {
            val parentView = TestView(player, viewContext)
            val childView1 = TestView(player, viewContext)
            val childView2 = TestView(player, viewContext)
            parentView.addChildren(childView1, childView2)

            parentView.removeChild(childView1)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView2)
        }

        @Test
        fun shouldSetParentToNull() {
            val parentView = TestView(player, viewContext)
            val childView = TestView(player, viewContext)
            parentView.addChildren(childView)

            parentView.removeChild(childView)

            assertThat(childView.parent)
                    .isNull()
        }
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun shouldInitiallyNotBeDestroyed() {
            val view = TestView(player, viewContext)

            val isDestroyed = view.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldCallOnDestroy() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, onDestroy = onDestroy)

            view.destroy()

            verify { onDestroy.invoke(view) }
        }

        @Test
        fun shouldDestroyChildren() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView2 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView3 = TestView(player, viewContext, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, onDestroy = onDestroy).apply {
                addChildren(childView1, childView2)
            }

            view.destroy()

            assertThat(childView1.isDestroyed)
                    .isTrue()
            assertThat(childView2.isDestroyed)
                    .isTrue()
            assertThat(childView3.isDestroyed)
                    .isTrue()
        }

        @Test
        fun shouldClearChildren() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView2 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView3 = TestView(player, viewContext, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, onDestroy = onDestroy).apply {
                addChildren(childView1, childView2)
            }

            view.destroy()

            assertThat(view.children)
                    .isEmpty()
            assertThat(childView1.children)
                    .isEmpty()
        }

        @Test
        fun shouldCallOnDestroyForChildren() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView2 = TestView(player, viewContext, onDestroy = onDestroy)
            val childView3 = TestView(player, viewContext, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, onDestroy = onDestroy).apply {
                addChildren(childView1, childView2)
            }

            view.destroy()

            verifySequence {
                onDestroy.invoke(view)
                onDestroy.invoke(childView1)
                onDestroy.invoke(childView3)
                onDestroy.invoke(childView2)
            }
        }

        @Test
        fun shouldNotCallOnDestroyTwice() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, onDestroy = onDestroy)

            view.destroy()
            view.destroy()

            verify(exactly = 1) { onDestroy.invoke(view) }
        }

        @Test
        fun shouldBeDestroyed() {
            val view = TestView(player, viewContext)

            view.destroy()

            assertThat(view.isDestroyed)
                    .isTrue()
        }

    }

    @Nested
    inner class InvalidateTests {

        @Test
        fun shouldInitiallyBeInvalidated() {
            val view = TestView(player, viewContext)

            val isInvalidated = view.isInvalidated

            assertThat(isInvalidated)
                    .isTrue()
        }

        @Test
        fun shouldInvalidate() {
            every { viewLayoutCalculator.calculate(any()) } returns mockk()
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, draw = draw)
            val childView2 = TestView(player, viewContext, draw = draw)
            val childView3 = TestView(player, viewContext, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, draw = draw).apply {
                addChildren(childView1, childView2)
            }
            view.draw()

            view.invalidate()

            assertThat(view.isInvalidated)
                    .isTrue()
            assertThat(childView1.isInvalidated)
                    .isTrue()
            assertThat(childView2.isInvalidated)
                    .isTrue()
            assertThat(childView3.isInvalidated)
                    .isTrue()
        }

    }

    @Nested
    inner class DrawTests {

        private val viewLayout = ViewLayout(
                marginArea = rectangleOf(0f, 640f, 0f, 480f),
                paddingArea = rectangleOf(10f, 630f, 20f, 460f),
                contentArea = rectangleOf(30f, 590f, 40f, 420f)
        )

        @BeforeEach
        fun setUp() {
            every { viewLayoutCalculator.calculate(any()) } returns viewLayout
        }

        @Test
        fun shouldDrawView() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)

            view.draw()

            verify { draw.invoke(view) }
        }

        @Test
        fun shouldCalculateContentArea() {
            val view = TestView(player, viewContext)

            view.draw()

            assertThat(view.contentArea)
                    .isEqualTo(rectangleOf(30f, 590f, 40f, 420f))
        }

        @Test
        fun shouldCalculatePaddingArea() {
            val view = TestView(player, viewContext)

            view.draw()

            assertThat(view.paddingArea)
                    .isEqualTo(rectangleOf(10f, 630f, 20f, 460f))
        }

        @Test
        fun shouldCalculateMarginArea() {
            val view = TestView(player, viewContext)

            view.draw()

            assertThat(view.marginArea)
                    .isEqualTo(rectangleOf(0f, 640f, 0f, 480f))
        }

        @Test
        fun givenViewIsInvalidatedItShouldCalculateAreaAndThenDrawView() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.invalidate()

            view.draw()

            verifyOrder {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
            }
        }

        @Test
        fun givenViewIsHiddenItShouldNotDrawAndMeasure() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.hide()

            view.draw()

            verify {
                viewLayoutCalculator wasNot Called
                draw wasNot Called
            }
        }

        @Test
        fun shouldNoLongerBeInvalidatedAfterDrawing() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, draw = draw)
            val childView2 = TestView(player, viewContext, draw = draw)
            val childView3 = TestView(player, viewContext, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, draw = draw).apply {
                addChildren(childView1, childView2)
            }
            view.invalidate()

            view.draw()

            assertThat(view.isInvalidated)
                    .isFalse()
            assertThat(childView1.isInvalidated)
                    .isFalse()
            assertThat(childView2.isInvalidated)
                    .isFalse()
            assertThat(childView3.isInvalidated)
                    .isFalse()
        }

        @Test
        fun givenViewWasNotInvalidatedAfterFirstDrawingItShouldNotCalculateAreaAgain() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.invalidate()

            view.draw()
            view.draw()

            verify(exactly = 1) {
                viewLayoutCalculator.calculate(any())
            }
            verifyOrder {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
                draw.invoke(view)
            }
        }

        @Test
        fun shouldDrawChildren() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, draw = draw)
            val childView2 = TestView(player, viewContext, draw = draw)
            val childView3 = TestView(player, viewContext, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, draw = draw).apply {
                addChildren(childView1, childView2)
            }

            view.draw()

            verifyOrder {
                draw.invoke(view)
                draw.invoke(childView1)
                draw.invoke(childView3)
                draw.invoke(childView2)
            }
        }

        @Test
        fun givenViewIsInvalidatedItShouldCalculateAreaAndThenDrawChildren() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, draw = draw)
            val childView2 = TestView(player, viewContext, draw = draw)
            val childView3 = TestView(player, viewContext, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, draw = draw).apply {
                addChildren(childView1, childView2)
            }
            view.invalidate()

            view.draw()

            verifyOrder {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
                viewLayoutCalculator.calculate(childView1)
                draw.invoke(childView1)
                viewLayoutCalculator.calculate(childView3)
                draw.invoke(childView3)
                viewLayoutCalculator.calculate(childView2)
                draw.invoke(childView2)
            }
        }
    }

    @Nested
    inner class HideTests {

        @Test
        fun shouldInitiallyNotBeHidden() {
            val view = TestView(player, viewContext)

            val isHidden = view.isHidden

            assertThat(isHidden)
                    .isFalse()
        }

        @Test
        fun shouldBeHidden() {
            val childView1 = TestView(player, viewContext)
            val childView2 = TestView(player, viewContext)
            val childView3 = TestView(player, viewContext)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext).apply {
                addChildren(childView1, childView2)
            }

            view.hide()

            assertThat(view.isHidden)
                    .isTrue()
            assertThat(childView1.isHidden)
                    .isTrue()
            assertThat(childView2.isHidden)
                    .isTrue()
            assertThat(childView3.isHidden)
                    .isTrue()
        }

        @Test
        fun shouldCallOnHide() {
            val onHide = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, onHide = onHide)
            val childView2 = TestView(player, viewContext, onHide = onHide)
            val childView3 = TestView(player, viewContext, onHide = onHide)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, onHide = onHide).apply {
                addChildren(childView1, childView2)
            }

            view.hide()

            verify {
                onHide.invoke(view)
                onHide.invoke(childView1)
                onHide.invoke(childView2)
                onHide.invoke(childView3)
            }
        }

        @Test
        fun givenViewIsAlreadyHiddenItShouldNotCallOnHide() {
            val onHide = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, onHide = onHide)
            view.hide()

            view.hide()

            verify(exactly = 1) { onHide.invoke(view) }
        }

    }

    @Nested
    inner class ShowTests {

        private val viewLayout = ViewLayout(
                marginArea = rectangleOf(0f, 640f, 0f, 480f),
                paddingArea = rectangleOf(10f, 630f, 20f, 460f),
                contentArea = rectangleOf(30f, 590f, 40f, 420f)
        )

        @BeforeEach
        fun setUp() {
            every { viewLayoutCalculator.calculate(any()) } returns viewLayout
        }

        @Test
        fun shouldCallOnShow() {
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, viewContext, onShow = onShow)
            val childView2 = TestView(player, viewContext, onShow = onShow)
            val childView3 = TestView(player, viewContext, onShow = onShow)
            childView1.addChild(childView3)
            val view = TestView(player, viewContext, onShow = onShow).apply {
                addChildren(childView1, childView2)
            }
            view.hide()

            view.show()

            verify {
                onShow.invoke(view)
                onShow.invoke(childView1)
                onShow.invoke(childView2)
                onShow.invoke(childView3)
            }
        }

        @Test
        fun givenViewIsAlreadyShownItShouldNotCallOnShow() {
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, onShow = onShow)
            view.hide()
            view.show()

            view.show()

            verify(exactly = 1) { onShow.invoke(view) }
        }

        @Test
        fun shouldInvalidateAndDrawByDefault() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.draw()
            view.hide()

            view.show()

            verifyOrder {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
            }
        }

        @Test
        fun shouldNotInvalidate() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.draw()
            view.hide()

            view.show(invalidate = false)

            verify(exactly = 1) {
                viewLayoutCalculator.calculate(any())
            }
            verifyOrder {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
                draw.invoke(view)
            }
            assertThat(view.isInvalidated)
                    .isFalse()
        }

        @Test
        fun shouldNotDraw() {
            val draw = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, viewContext, draw = draw)
            view.draw()
            view.hide()

            view.show(draw = false)

            verify(exactly = 1) {
                viewLayoutCalculator.calculate(view)
                draw.invoke(view)
            }
        }

    }

    private class TestView(
            player: Player,
            viewContext: ViewContext,
            private val draw: TestView.() -> Unit = {},
            private val onShow: TestView.() -> Unit = {},
            private val onHide: TestView.() -> Unit = {},
            private val onDestroy: TestView.() -> Unit = {}
    ) : View(player, viewContext) {

        override fun onDraw() {
            draw.invoke(this)
        }

        override fun onShow() {
            onShow.invoke(this)
        }

        override fun onHide() {
            onHide.invoke(this)
        }

        override fun onDestroy() {
            onDestroy.invoke(this)
        }

    }

}
