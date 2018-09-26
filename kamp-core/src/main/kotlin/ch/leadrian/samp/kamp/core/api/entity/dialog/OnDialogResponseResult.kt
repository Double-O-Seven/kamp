package ch.leadrian.samp.kamp.core.api.entity.dialog

sealed class OnDialogResponseResult {

    object Processed : OnDialogResponseResult()

    object Ignored : OnDialogResponseResult()

}