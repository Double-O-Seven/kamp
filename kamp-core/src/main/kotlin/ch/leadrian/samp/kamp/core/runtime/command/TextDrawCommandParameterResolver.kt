package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import javax.inject.Inject

internal class TextDrawCommandParameterResolver
@Inject
constructor(textDrawRegistry: TextDrawRegistry) :
        EntityCommandParameterResolver<TextDraw, TextDrawId>(textDrawRegistry) {

    override val parameterType: Class<TextDraw> = TextDraw::class.java

}