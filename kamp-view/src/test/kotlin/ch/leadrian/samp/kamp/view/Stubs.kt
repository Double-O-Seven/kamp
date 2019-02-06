package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.MessageFormatter
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import io.mockk.mockk

internal fun stubDefaultViewFactory(
        viewContext: ViewContext = mockk(),
        textProvider: TextProvider = mockk(),
        textFormatter: TextFormatter = mockk(),
        playerTextDrawService: PlayerTextDrawService = mockk(),
        dialogService: DialogService = mockk(),
        messageFormatter: MessageFormatter = mockk()
): DefaultViewFactory {
    return DefaultViewFactory(
            viewContext,
            textProvider,
            textFormatter,
            playerTextDrawService,
            dialogService,
            messageFormatter
    )
}

