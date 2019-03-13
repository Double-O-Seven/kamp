package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextLabelRegistry
@Inject
constructor() : EntityRegistry<TextLabel, TextLabelId>(arrayOfNulls(SAMPConstants.MAX_3DTEXT_GLOBAL))
