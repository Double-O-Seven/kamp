package ch.leadrian.samp.kamp.core.runtime.text

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.text.GameTextSender
import ch.leadrian.samp.kamp.core.api.text.MessageFormatter
import ch.leadrian.samp.kamp.core.api.text.MessagePreparer
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.PlayerMessageSender
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextPreparer
import ch.leadrian.samp.kamp.core.api.text.TextProvider

internal class TextModule : KampModule() {

    override fun configure() {
        newTextProviderResourceBundlePackagesSetBinder().apply {
            addBinding().toInstance("ch.leadrian.samp.kamp.core")
        }
        bind(GameTextSender::class.java)
        bind(MessageFormatter::class.java)
        bind(MessagePreparer::class.java)
        bind(MessageSender::class.java)
        bind(PlayerMessageSender::class.java)
        bind(TextFormatter::class.java)
        bind(TextPreparer::class.java)
        bind(TextProvider::class.java)
    }
}