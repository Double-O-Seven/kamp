package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextDrawRegistry
@Inject
constructor() : EntityRegistry<TextDraw, TextDrawId>(arrayOfNulls(SAMPConstants.MAX_TEXT_DRAWS))
