package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.runtime.entity.PlayerTextDrawImpl

internal class PlayerTextDrawRegistry : EntityRegistry<PlayerTextDrawImpl, PlayerTextDrawId>(arrayOfNulls(SAMPConstants.MAX_PLAYER_TEXT_DRAWS))
