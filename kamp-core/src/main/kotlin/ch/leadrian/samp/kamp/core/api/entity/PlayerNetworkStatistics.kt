package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ConnectionStatus
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

class PlayerNetworkStatistics
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    val connectedTime: Int
        get() = nativeFunctionExecutor.netStats_GetConnectedTime(player.id.value)

    val messagesReceived: Int
        get() = nativeFunctionExecutor.netStats_MessagesReceived(player.id.value)

    val bytesReceived: Int
        get() = nativeFunctionExecutor.netStats_BytesReceived(player.id.value)

    val messagesSent: Int
        get() = nativeFunctionExecutor.netStats_MessagesSent(player.id.value)

    val bytesSent: Int
        get() = nativeFunctionExecutor.netStats_BytesSent(player.id.value)

    val messagesReceivedPerSecond: Int
        get() = nativeFunctionExecutor.netStats_MessagesRecvPerSecond(player.id.value)

    val packetLossPercentage: Float
        get() = nativeFunctionExecutor.netStats_PacketLossPercent(player.id.value)

    val connectionStatus: ConnectionStatus
        get() = nativeFunctionExecutor.netStats_ConnectionStatus(player.id.value).let { ConnectionStatus[it] }

    val ipAndPort: String
        get() {
            val ipAndPort = ReferenceString()
            nativeFunctionExecutor.netStats_GetIpPort(playerid = player.id.value, ip_port = ipAndPort, ip_port_len = 21)
            return ipAndPort.value ?: ""
        }

    val summaryString: String
        get() {
            val networkStatisticsString = ReferenceString()
            nativeFunctionExecutor.getPlayerNetworkStats(
                    playerid = player.id.value,
                    retstr = networkStatisticsString,
                    size = 400
            )
            return networkStatisticsString.value ?: ""
        }
}