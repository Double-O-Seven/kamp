package ch.leadrian.samp.kamp.view.factory

import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import javax.inject.Inject

internal class DefaultViewFactory
@Inject
constructor(
        override val viewContext: ViewContext,
        override val textProvider: TextProvider,
        override val textFormatter: TextFormatter,
        override val playerTextDrawService: PlayerTextDrawService
) : ViewFactory