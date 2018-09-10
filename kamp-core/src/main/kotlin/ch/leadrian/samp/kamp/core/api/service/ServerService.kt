package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.entity.ConsoleVars
import ch.leadrian.samp.kamp.core.api.entity.ServerVars
import java.nio.file.Path

interface ServerService {

    fun setGameModeText(text: String)

    fun setTeamCount(teamCount: Int)

    fun getServerVars(): ServerVars

    fun getConsoleVars(): ConsoleVars

    fun triggerGameModeExit()

    fun sendRconCommand(command: String)

    fun getNetworkStatistics(resultLength: Int = 401): String

    fun blockIpAddress(ipAddress: String, timeInMs: Int)

    fun unblockIpAddress(ipAddress: String)

    fun getAnimationName(animationIndex: Int): ch.leadrian.samp.kamp.core.api.data.Animation

    fun getDataDirectory(): Path
}