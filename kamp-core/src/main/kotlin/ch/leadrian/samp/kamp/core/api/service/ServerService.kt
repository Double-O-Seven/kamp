package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.entity.ConsoleVars
import ch.leadrian.samp.kamp.core.api.entity.ServerVars
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import javax.inject.Inject

class ServerService
@Inject
internal constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    private val serverVars = ServerVars(nativeFunctionExecutor)

    private val consoleVars = ConsoleVars(nativeFunctionExecutor)

    fun setGameModeText(text: String) {
        nativeFunctionExecutor.setGameModeText(text)
    }

    fun setTeamCount(teamCount: Int) {
        nativeFunctionExecutor.setTeamCount(teamCount)
    }

    fun getServerVars(): ServerVars = serverVars

    fun getConsoleVars(): ConsoleVars = consoleVars

    fun triggerGameModeExit() {
        nativeFunctionExecutor.gameModeExit()
    }

    fun sendRconCommand(command: String) {
        nativeFunctionExecutor.sendRconCommand(command)
    }

    @JvmOverloads
    fun getNetworkStatistics(resultLength: Int = 401): String {
        val networkStatistics = ReferenceString()
        nativeFunctionExecutor.getNetworkStats(networkStatistics, resultLength)
        return networkStatistics.value ?: ""
    }

    fun blockIpAddress(ipAddress: String, timeInMs: Int) {
        nativeFunctionExecutor.blockIpAddress(ipAddress, timeInMs)
    }

    fun unblockIpAddress(ipAddress: String) {
        nativeFunctionExecutor.unBlockIpAddress(ipAddress)
    }

    @JvmOverloads
    fun getAnimationName(animationIndex: Int, animationLibrarySize: Int = 32, animationNameSize: Int = 32): Animation {
        val animationLibrary = ReferenceString()
        val animationName = ReferenceString()
        nativeFunctionExecutor.getAnimationName(
                index = animationIndex,
                animlib = animationLibrary,
                animlib_size = animationLibrarySize,
                animname = animationName,
                animname_size = animationNameSize
        )
        val animationLibraryValue = animationLibrary.value
        val animationNameValue = animationName.value
        if (animationLibraryValue != null && animationNameValue != null) {
            return Animation.valueOf(library = animationLibraryValue, name = animationNameValue)
        } else {
            throw IllegalArgumentException("Invalid animation index: $animationIndex")
        }
    }
}