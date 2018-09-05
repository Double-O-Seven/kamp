package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.runtime.entity.PlayerMapObjectImpl

internal class PlayerMapObjectRegistry : EntityRegistry<PlayerMapObjectImpl, PlayerMapObjectId>(arrayOfNulls(SAMPConstants.MAX_OBJECTS))
