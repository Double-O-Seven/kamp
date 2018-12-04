package ch.leadrian.samp.kamp.view

import javax.inject.Inject

class ViewContext
@Inject
internal constructor(internal val viewLayoutCalculator: ViewLayoutCalculator)
