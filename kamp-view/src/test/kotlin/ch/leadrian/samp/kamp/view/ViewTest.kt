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
    private val areaCalculator = mockk<ViewAreaCalculator>()

    @Test
    fun shouldSetPadding() {
        val view = TestView(player, areaCalculator)

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
        val view = TestView(player, areaCalculator)

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
        val view = TestView(player, areaCalculator)

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
        val view = TestView(player, areaCalculator)

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
        val view = TestView(player, areaCalculator)

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
        val view = TestView(player, areaCalculator)

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

        private val parentView = TestView(player, areaCalculator)

        @Test
        fun shouldAddChild() {
            val childView = TestView(player, areaCalculator)

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun shouldAddChildrenFromIterable() {
            val childView1 = TestView(player, areaCalculator)
            val childView2 = TestView(player, areaCalculator)

            parentView.addChildren(listOf(childView1, childView2))

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldAddChildrenFromVarargs() {
            val childView1 = TestView(player, areaCalculator)
            val childView2 = TestView(player, areaCalculator)

            parentView.addChildren(childView1, childView2)

            assertThat(parentView.children)
                    .containsExactly(childView1, childView2)
        }

        @Test
        fun shouldSetViewAsParentOfChild() {
            val childView = TestView(player, areaCalculator)

            parentView.addChild(childView)

            assertThat(childView.parent)
                    .isSameAs(parentView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldAddChild() {
            val childView = TestView(player, areaCalculator)
            TestView(player, areaCalculator).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView)
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldRemoveChildFromOldParent() {
            val childView = TestView(player, areaCalculator)
            val otherParentView = TestView(player, areaCalculator).apply {
                addChild(childView)
            }

            parentView.addChild(childView)

            assertThat(otherParentView.children)
                    .isEmpty()
        }

        @Test
        fun givenChildViewAlreadyHasParentItShouldChangeParent() {
            val childView = TestView(player, areaCalculator)
            TestView(player, areaCalculator).apply {
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
            val parentView = TestView(player, areaCalculator)
            val childView1 = TestView(player, areaCalculator)
            val childView2 = TestView(player, areaCalculator)
            parentView.addChildren(childView1, childView2)

            parentView.removeChild(childView1)

            assertThat(parentView.children)
                    .containsExactlyInAnyOrder(childView2)
        }

        @Test
        fun shouldSetParentToNull() {
            val parentView = TestView(player, areaCalculator)
            val childView = TestView(player, areaCalculator)
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
            val view = TestView(player, areaCalculator)

            val isDestroyed = view.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldCallOnDestroy() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, onDestroy = onDestroy)

            view.destroy()

            verify { onDestroy.invoke(view) }
        }

        @Test
        fun shouldDestroyChildren() {
            val onDestroy = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, areaCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, onDestroy = onDestroy).apply {
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
            val childView1 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, areaCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, onDestroy = onDestroy).apply {
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
            val childView1 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView2 = TestView(player, areaCalculator, onDestroy = onDestroy)
            val childView3 = TestView(player, areaCalculator, onDestroy = onDestroy)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, onDestroy = onDestroy).apply {
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
            val view = TestView(player, areaCalculator, onDestroy = onDestroy)

            view.destroy()
            view.destroy()

            verify(exactly = 1) { onDestroy.invoke(view) }
        }

        @Test
        fun shouldBeDestroyed() {
            val view = TestView(player, areaCalculator)

            view.destroy()

            assertThat(view.isDestroyed)
                    .isTrue()
        }

    }

    @Nested
    inner class InvalidateTests {

        @Test
        fun shouldInitiallyBeInvalidated() {
            val view = TestView(player, areaCalculator)

            val isInvalidated = view.isInvalidated

            assertThat(isInvalidated)
                    .isTrue()
        }

        @Test
        fun shouldInvalidate() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, draw = draw)
            val childView2 = TestView(player, areaCalculator, draw = draw)
            val childView3 = TestView(player, areaCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, draw = draw).apply {
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
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)

            view.draw()

            verify { draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f)) }
        }

        @Test
        fun givenViewIsInvalidatedItShouldCalculateAreaAndThenDrawView() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.invalidate()

            view.draw()

            verifyOrder {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun givenViewIsHiddenItShouldNotDrawAndMeasure() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.hide()

            view.draw()

            verify(exactly = 0) {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldNoLongerBeInvalidatedAfterDrawing() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, draw = draw)
            val childView2 = TestView(player, areaCalculator, draw = draw)
            val childView3 = TestView(player, areaCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, draw = draw).apply {
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
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.invalidate()

            view.draw()
            view.draw()

            verify(exactly = 1) { areaCalculator.calculate(any()) }
            verifyOrder {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldDrawChildren() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, draw = draw)
            val childView2 = TestView(player, areaCalculator, draw = draw)
            val childView3 = TestView(player, areaCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, draw = draw).apply {
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
        fun givenViewIsInvalidatedItShouldCalculateAreaAndThenDrawChildren() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, draw = draw)
            val childView2 = TestView(player, areaCalculator, draw = draw)
            val childView3 = TestView(player, areaCalculator, draw = draw)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, draw = draw).apply {
                addChildren(childView1, childView2)
            }
            view.invalidate()

            view.draw()

            verifyOrder {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                areaCalculator.calculate(childView1)
                draw.invoke(childView1, rectangleOf(0f, 640f, 0f, 480f))
                areaCalculator.calculate(childView3)
                draw.invoke(childView3, rectangleOf(0f, 640f, 0f, 480f))
                areaCalculator.calculate(childView2)
                draw.invoke(childView2, rectangleOf(0f, 640f, 0f, 480f))
            }
        }
    }

    @Nested
    inner class HideTests {

        @Test
        fun shouldInitiallyNotBeHidden() {
            val view = TestView(player, areaCalculator)

            val isHidden = view.isHidden

            assertThat(isHidden)
                    .isFalse()
        }

        @Test
        fun shouldBeHidden() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val childView1 = TestView(player, areaCalculator)
            val childView2 = TestView(player, areaCalculator)
            val childView3 = TestView(player, areaCalculator)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator).apply {
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
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onHide = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, onHide = onHide)
            val childView2 = TestView(player, areaCalculator, onHide = onHide)
            val childView3 = TestView(player, areaCalculator, onHide = onHide)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, onHide = onHide).apply {
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
            val view = TestView(player, areaCalculator, onHide = onHide)
            view.hide()

            view.hide()

            verify(exactly = 1) { onHide.invoke(view) }
        }

    }

    @Nested
    inner class ShowTests {

        @Test
        fun shouldCallOnShow() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val childView1 = TestView(player, areaCalculator, onShow = onShow)
            val childView2 = TestView(player, areaCalculator, onShow = onShow)
            val childView3 = TestView(player, areaCalculator, onShow = onShow)
            childView1.addChild(childView3)
            val view = TestView(player, areaCalculator, onShow = onShow).apply {
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
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val onShow = mockk<TestView.() -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, onShow = onShow)
            view.hide()
            view.show()

            view.show()

            verify(exactly = 1) { onShow.invoke(view) }
        }

        @Test
        fun shouldInvalidateAndDrawByDefault() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show()

            verifyOrder {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

        @Test
        fun shouldNotInvalidate() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show(invalidate = false)

            verifyOrder {
                areaCalculator.calculate(view)
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
            verify(exactly = 1) {
                areaCalculator.calculate(any())
            }
            assertThat(view.isInvalidated)
                    .isFalse()
        }

        @Test
        fun shouldNotDraw() {
            every { areaCalculator.calculate(any()) } returns mutableRectangleOf(0f, 640f, 0f, 480f)
            val draw = mockk<TestView.(Rectangle) -> Unit>(relaxed = true)
            val view = TestView(player, areaCalculator, draw = draw)
            view.draw()
            view.hide()

            view.show(draw = false)

            verify(exactly = 1) {
                areaCalculator.calculate(any())
                draw.invoke(view, rectangleOf(0f, 640f, 0f, 480f))
            }
        }

    }

    private class TestView(
            player: Player,
            viewAreaCalculator: ViewAreaCalculator,
            private val draw: TestView.(Rectangle) -> Unit = {},
            private val onShow: TestView.() -> Unit = {},
            private val onHide: TestView.() -> Unit = {},
            private val onDestroy: TestView.() -> Unit = {},
            private val onMeasure: TestView.(MutableRectangle) -> Unit = {}
    ) : View(player, viewAreaCalculator) {

        override fun onDraw() {
            draw.invoke(this, area)
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

        override fun onMeasure(calculatedArea: MutableRectangle) {
            onMeasure.invoke(this, calculatedArea)
        }

    }

}