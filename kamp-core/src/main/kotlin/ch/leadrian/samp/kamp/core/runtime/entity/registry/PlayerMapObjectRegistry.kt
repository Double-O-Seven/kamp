package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.runtime.entity.PlayerMapObjectImpl

internal class PlayerMapObjectRegistry : EntityRegistry<PlayerMapObjectImpl, PlayerMapObjectId>(arrayOfNulls(SAMPConstants.MAX_OBJECTS))
