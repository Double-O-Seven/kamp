package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextLabelRegistry
import javax.inject.Inject

internal class TextLabelCommandParameterResolver
@Inject
constructor(textLabelRegistry: TextLabelRegistry) : EntityCommandParameterResolver<TextLabel, TextLabelId>(textLabelRegistry) {

    override val parameterType: Class<TextLabel> = TextLabel::class.java

}