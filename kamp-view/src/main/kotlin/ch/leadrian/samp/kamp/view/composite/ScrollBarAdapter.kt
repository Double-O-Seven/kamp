package ch.leadrian.samp.kamp.view.composite

interface ScrollBarAdapter {

    val numberOfTicks: Int

    val windowSize: Int

    fun onScroll(view: ScrollBarView, oldPosition: Int, newPosition: Int)

}