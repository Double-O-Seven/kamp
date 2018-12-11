package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.view.TextKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ScreenResolutionDialogProvider
@Inject
constructor(dialogService: DialogService) {

    private val allScreenResolutions: List<ScreenResolution> = listOf(
            640 x 480,
            720 x 480,
            720 x 576,
            800 x 600,
            1024 x 768,
            1152 x 864,
            1176 x 664,
            1280 x 720,
            1280 x 768,
            1280 x 800,
            1280 x 960,
            1280 x 1024,
            1360 x 768,
            1366 x 768,
            1400 x 900,
            1600 x 900,
            1600 x 1024,
            1680 x 1050,
            1768 x 992,
            1920 x 1080
    )

    private val dialog: Dialog by lazy {
        dialogService.createListDialog<ScreenResolution> {
            caption(TextKeys.view.dialog.screen.resolution.selection.caption)
            allScreenResolutions.forEach {
                item {
                    value(it)
                    content("${it.width} x ${it.height}")
                }
            }
            onSelectItem { player, listDialogItem, _ -> player.screenResolution.set(listDialogItem.value) }
            leftButton(ch.leadrian.samp.kamp.core.TextKeys.dialog.button.ok)
            rightButton(ch.leadrian.samp.kamp.core.TextKeys.dialog.button.cancel)
        }
    }

    fun get(): Dialog = dialog

}