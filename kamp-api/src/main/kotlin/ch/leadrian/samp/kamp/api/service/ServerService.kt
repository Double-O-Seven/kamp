package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.entity.ConsoleVars
import ch.leadrian.samp.kamp.api.entity.ServerVars

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
}