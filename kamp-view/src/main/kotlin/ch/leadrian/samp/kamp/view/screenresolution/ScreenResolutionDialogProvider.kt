package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.view.KampViewTextKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ScreenResolutionDialogProvider
@Inject
constructor(dialogService: DialogService, messageSender: MessageSender) {

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
            caption(KampViewTextKeys.view.dialog.screen.resolution.selection.caption)
            allScreenResolutions.forEach {
                item {
                    value(it)
                    content("${it.width}x${it.height}")
                }
            }
            onSelectItem { player, listDialogItem, _ ->
                val resolution = listDialogItem.value
                player.screenResolution.set(resolution)
                messageSender.sendMessageToPlayer(
                        player,
                        Colors.GREY,
                        KampViewTextKeys.view.dialog.screen.resolution.message,
                        resolution.width.toString(),
                        resolution.height.toString()
                )
            }
            leftButton(KampCoreTextKeys.dialog.button.ok)
            rightButton(KampCoreTextKeys.dialog.button.cancel)
        }
    }

    fun get(): Dialog = dialog

}