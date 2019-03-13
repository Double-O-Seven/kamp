package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId

internal class PlayerTextLabelRegistry :
        EntityRegistry<PlayerTextLabel, PlayerTextLabelId>(arrayOfNulls(SAMPConstants.MAX_3DTEXT_PLAYER))
