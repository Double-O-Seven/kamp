package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.runtime.entity.PlayerTextDrawImpl

internal class PlayerTextDrawRegistry : EntityRegistry<PlayerTextDrawImpl, PlayerTextDrawId>(arrayOfNulls(SAMPConstants.MAX_PLAYER_TEXT_DRAWS))
