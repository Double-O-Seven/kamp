package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.TextDraw
import ch.leadrian.samp.kamp.api.entity.id.TextDrawId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextDrawRegistry
@Inject
constructor() : EntityRegistry<TextDraw, TextDrawId>(arrayOfNulls(SAMPConstants.MAX_TEXT_DRAWS))
