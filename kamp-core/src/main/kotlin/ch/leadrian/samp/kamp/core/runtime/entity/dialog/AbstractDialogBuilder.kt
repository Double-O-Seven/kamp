package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.FunctionalDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TextKeyDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider

internal abstract class AbstractDialogBuilder<B : DialogBuilder<B>>(protected val textProvider: TextProvider) :
        DialogBuilder<B> {

    protected lateinit var captionTextSupplier: DialogTextSupplier

    protected lateinit var leftButtonTextSupplier: DialogTextSupplier

    protected var rightButtonTextSupplier: DialogTextSupplier = StringDialogTextSupplier("")

    override fun caption(text: String): B {
        captionTextSupplier = StringDialogTextSupplier(text)
        return self()
    }

    override fun caption(textKey: TextKey): B {
        captionTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)
        return self()
    }

    override fun caption(supplier: (Player) -> String): B {
        captionTextSupplier = FunctionalDialogTextSupplier(supplier)
        return self()
    }

    override fun caption(supplier: DialogTextSupplier): B {
        captionTextSupplier = supplier
        return self()
    }

    override fun leftButton(text: String): B {
        leftButtonTextSupplier = StringDialogTextSupplier(text)
        return self()
    }

    override fun leftButton(textKey: TextKey): B {
        leftButtonTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)
        return self()
    }

    override fun leftButton(supplier: (Player) -> String): B {
        leftButtonTextSupplier = FunctionalDialogTextSupplier(supplier)
        return self()
    }

    override fun leftButton(supplier: DialogTextSupplier): B {
        leftButtonTextSupplier = supplier
        return self()
    }

    override fun rightButton(text: String): B {
        rightButtonTextSupplier = StringDialogTextSupplier(text)
        return self()
    }

    override fun rightButton(textKey: TextKey): B {
        rightButtonTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)
        return self()
    }

    override fun rightButton(supplier: (Player) -> String): B {
        rightButtonTextSupplier = FunctionalDialogTextSupplier(supplier)
        return self()
    }

    override fun rightButton(supplier: DialogTextSupplier): B {
        rightButtonTextSupplier = supplier
        return self()
    }

    protected abstract fun self(): B

}