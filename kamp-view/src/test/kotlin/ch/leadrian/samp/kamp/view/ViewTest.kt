package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewTest {

    private val player = mockk<Player>()
    private val layoutCalculator = mockk<ViewLayoutCalculator>()

    @Test
    fun shouldSetPadding() {
        val view = TestView(player, layoutCalculator)

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
        val view = TestView(player, layoutCalculator)

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
        val view = TestView(player, layoutCalculator)

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
        val view = TestView(player, layoutCalculator)

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
        val view = TestView(player, layoutCalculator)

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
        val view = TestView(player, layoutCalculator)

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

        private val parentView = TestView(player, layoutCalculator)

        @Test
        fun shouldAddChild() {
            val childView = TestView(player, layoutCalculator)

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun shouldAddChildrenFromIterable() {
            val childView1 = TestView(player, layoutCalculator)
            val childView2 = TestView(player, layoutCalculator)

            parentView.addChildren(listOf(childView1, childView2))

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldAddChildrenFromVarargs() {
            val childView1 = TestView(player, layoutCalculator)
            val childView2 = TestView(player, layoutCalculator)

            parentView.addChildren(childView1, childView2)

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldSetViewAsParentOfChild() {
            val childView = TestView(player, layoutCalculator)

            parentView.addChild(childView)

            assertThat(childView.parent)
                    .isSameAs(parentView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldAddChild() {
            val childView = TestView(player, layoutCalculator)
            TestView(player, layoutCalculator).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldRemoveChildFromOldParent() {
            val childView = TestView(player, layoutCalculator)
            val otherParentView = TestView(player, layoutCalculator).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(otherParentView.children)
                    .isEmpty()
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldChangeParent() {
            val childView = TestView(player, layoutCalculator)
            TestView(player, layoutCalculator).apply {
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
            val parentView = TestView(player, layoutCalculator)
            val childView1 = TestView(player, layoutCalculator)
            val childView2 = TestView(player, layoutCalculator)
            parentView.addChildren(childView1, childView2)

            parentView.removeChild(childView1)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView2)
        }

        @Test
        fun shouldSetParentToNull() {
            val parentView = TestView(player, layoutCalculator)
            val childView = TestView(player, layoutCalculator)
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
            val view = TestView(player, layoutCalculator)

            val isDestroyed = view.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldCallOnDestroy() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, onDestroy = onDestroy)

            view.destroy()

            verify { onDestroy.invoke(view) }
        }

        @Test
        fun shouldDestroyChildren() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, onDestroy = onDestroy).apply {
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
            val childView1 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, onDestroy = onDestroy).apply {
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
            val childView1 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, layoutCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, onDestroy = onDestroy).apply {
                addChildren(childView1, childView2)
            }

            view.destroy()

            verifySequence {
                onDestroy.invoke(childView3)
                onDestroy.invoke(childView1)
                onDestroy.invoke(childView2)
                onDestroy.invoke(view)
            }
        }

        @Test
        fun shouldNotCallOnDestroyTwice() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, onDestroy = onDestroy)

            view.destroy()
            view.destroy()

            verify(exactly = 1) { onDestroy.invoke(view) }
        }

        @Test
        fun shouldBeDestroyed() {
            val view = TestView(player, layoutCalculator)

            view.destroy()

            assertThat(view.isDestroyed)
                    .isTrue()
        }

    }

    @Nested
    inner class InvalidateTests {

        @Test
        fun shouldInitiallyBeInvalidated() {
            val view = TestView(player, layoutCalculator)

            val isInvalidated = view.isInvalidated

            assertThat(isInvalidated)
                    .isTrue()
        }

        @Test
        fun shouldInvalidate() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, draw = draw)
            val childView2 = TestView(player, layoutCalculator, draw = draw)
            val childView3 = TestView(player, layoutCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, draw = draw).apply {
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

        @Test
        fun shouldDrawView() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)

            view.draw()

            verify { draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f)) }
        }

        @Test
        fun givenViewIsInvalidatedItShouldCalculateLayoutAndThenDrawView() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.invalidate()

            view.draw()

            verifyOrder {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun givenViewIsHiddenItShouldNotDrawAndMeasure() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.hide()

            view.draw()

            verify(exactly = 0) {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldNoLongerBeInvalidatedAfterDrawing() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, draw = draw)
            val childView2 = TestView(player, layoutCalculator, draw = draw)
            val childView3 = TestView(player, layoutCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, draw = draw).apply {
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
        fun givenViewWasNotInvalidatedAfterFirstDrawingItShouldNotCalculateLayoutAgain() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.invalidate()

            view.draw()
            view.draw()

            verify(exactly = 1) { layoutCalculator.calculate(any()) }
            verifyOrder {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldDrawChildren() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, draw = draw)
            val childView2 = TestView(player, layoutCalculator, draw = draw)
            val childView3 = TestView(player, layoutCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, draw = draw).apply {
                addChildren(childView1, childView2)
            }

            view.draw()

            verifyOrder {
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(childView1, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(childView3, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(childView2, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun givenViewIsInvalidatedItShouldCalculateLayoutAndThenDrawChildren() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, draw = draw)
            val childView2 = TestView(player, layoutCalculator, draw = draw)
            val childView3 = TestView(player, layoutCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, draw = draw).apply {
                addChildren(childView1, childView2)
            }
            view.invalidate()

            view.draw()

            verifyOrder {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                layoutCalculator.calculate(childView1)
                draw.invoke(childView1, rectangleOf(0f, 640f, 0f, 480f))
                layoutCalculator.calculate(childView3)
                draw.invoke(childView3, rectangleOf(0f, 640f, 0f, 480f))
                layoutCalculator.calculate(childView2)
                draw.invoke(childView2, rectangleOf(0f, 640f, 0f, 480f))
            }
        }
    }

    @Nested
    inner class HideTests {

        @Test
        fun shouldInitiallyNotBeHidden() {
            val view = TestView(player, layoutCalculator)

            val isHidden = view.isHidden

            assertThat(isHidden)
                    .isFalse()
        }

        @Test
        fun shouldBeHidden() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val childView1 = TestView(player, layoutCalculator)
            val childView2 = TestView(player, layoutCalculator)
            val childView3 = TestView(player, layoutCalculator)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator).apply {
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
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onHide = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, onHide = onHide)
            val childView2 = TestView(player, layoutCalculator, onHide = onHide)
            val childView3 = TestView(player, layoutCalculator, onHide = onHide)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, onHide = onHide).apply {
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
            val view = TestView(player, layoutCalculator, onHide = onHide)
            view.hide()

            view.hide()

            verify(exactly = 1) { onHide.invoke(view) }
        }

    }

    @Nested
    inner class ShowTests {

        @Test
        fun shouldCallOnShow() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, layoutCalculator, onShow = onShow)
            val childView2 = TestView(player, layoutCalculator, onShow = onShow)
            val childView3 = TestView(player, layoutCalculator, onShow = onShow)
            childView1.addChild(childView3)
            val view = TestView(player, layoutCalculator, onShow = onShow).apply {
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
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, onShow = onShow)
            view.hide()
            view.show()

            view.show()

            verify(exactly = 1) { onShow.invoke(view) }
        }

        @Test
        fun shouldInvalidateAndDrawByDefault() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show()

            verifyOrder {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldNotInvalidate() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show(invalidate = false)

            verifyOrder {
                layoutCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
            verify(exactly = 1) {
                layoutCalculator.calculate(any())
            }
            assertThat(view.isInvalidated)
                    .isFalse()
        }

        @Test
        fun shouldNotDraw() {
            every { layoutCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, layoutCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show(draw = false)

            verify(exactly = 1) {
                layoutCalculator.calculate(any())
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

    }

    private class TestView(
            player: Player,
            viewLayoutCalculator: ViewLayoutCalculator,
            private val draw: TestView.(Rectangle) -> Unit = {},
            private val onShow: TestView.() -> Unit = {},
            private val onHide: TestView.() -> Unit = {},
            private val onDestroy: TestView.() -> Unit = {},
            private val onMeasure: TestView.(MutableRectangle) -> Unit = {}
    ) : View(player, viewLayoutCalculator) {

        override fun draw(layout: Rectangle) {
            draw.invoke(this, layout)
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

        override fun onMeasure(layout: MutableRectangle) {
            onMeasure.invoke(this, layout)
        }

    }

}