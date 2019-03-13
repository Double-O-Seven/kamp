package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry

internal class PlayerMapObjectRegistry :
        EntityRegistry<PlayerMapObject, PlayerMapObjectId>(arrayOfNulls(SAMPConstants.MAX_OBJECTS))
