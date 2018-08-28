package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.ConnectionStatus
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerNetworkStatistics
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceString

internal class PlayerNetworkStatisticsImpl(
        override val player: Player,
        private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor
) : PlayerNetworkStatistics {

    override val connectedTime: Int
        get() = nativeFunctionsExecutor.netStats_GetConnectedTime(player.id.value)

    override val messagesReceived: Int
        get() = nativeFunctionsExecutor.netStats_MessagesReceived(player.id.value)

    override val bytesReceived: Int
        get() = nativeFunctionsExecutor.netStats_BytesReceived(player.id.value)

    override val messagesSent: Int
        get() = nativeFunctionsExecutor.netStats_MessagesSent(player.id.value)

    override val bytesSent: Int
        get() = nativeFunctionsExecutor.netStats_BytesSent(player.id.value)

    override val messagesReceivedPerSecond: Int
        get() = nativeFunctionsExecutor.netStats_MessagesRecvPerSecond(player.id.value)

    override val packetLossPercentage: Float
        get() = nativeFunctionsExecutor.netStats_PacketLossPercent(player.id.value)

    override val connectionStatus: ConnectionStatus
        get() = nativeFunctionsExecutor.netStats_ConnectionStatus(player.id.value).let { ConnectionStatus[it] }

    override val ipAndPort: String
        get() {
            val ipAndPort = ReferenceString()
            nativeFunctionsExecutor.netStats_GetIpPort(playerid = player.id.value, ip_port = ipAndPort, ip_port_len = 21)
            return ipAndPort.value ?: ""
        }

    override val summaryString: String
        get() {
            val networkStatisticsString = ReferenceString()
            nativeFunctionsExecutor.getPlayerNetworkStats(playerid = player.id.value, retstr = networkStatisticsString, size = 400)
            return networkStatisticsString.value ?: ""
        }
}