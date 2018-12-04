package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.view.layout.ViewLayoutCalculator
import javax.inject.Inject

class ViewContext
@Inject
internal constructor(internal val viewLayoutCalculator: ViewLayoutCalculator)
