package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry

internal class PlayerTextDrawRegistry :
        EntityRegistry<PlayerTextDraw, PlayerTextDrawId>(arrayOfNulls(SAMPConstants.MAX_PLAYER_TEXT_DRAWS))
