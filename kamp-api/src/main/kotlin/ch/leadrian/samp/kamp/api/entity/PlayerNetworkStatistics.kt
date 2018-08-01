package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.ConnectionStatus

interface PlayerNetworkStatistics {

    val connectedTime: Int

    val messagesReceived: Int

    val bytesReceived: Int

    val messagesSent: Int

    val bytesSent: Int

    val messagesReceivedPerSecond: Int

    val packetLossPercentage: Float

    val connectionStatus: ConnectionStatus

    val ipAndPort: String

    fun toSummarizedString(): String
}