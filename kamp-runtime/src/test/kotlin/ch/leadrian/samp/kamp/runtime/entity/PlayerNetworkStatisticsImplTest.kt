package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.ConnectionStatus
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerNetworkStatisticsImplTest {

    @Test
    fun shouldGetConnectedTime() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_GetConnectedTime(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.connectedTime

        assertThat(result)
                .isEqualTo(12345)
    }

    @Test
    fun shouldGetMessagesReceived() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_MessagesReceived(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.messagesReceived

        assertThat(result)
                .isEqualTo(12345)
    }

    @Test
    fun shouldGetBytesReceived() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_BytesReceived(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.bytesReceived

        assertThat(result)
                .isEqualTo(12345)
    }

    @Test
    fun shouldGetMessagesSent() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_MessagesSent(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.messagesSent

        assertThat(result)
                .isEqualTo(12345)
    }

    @Test
    fun shouldGetBytesSent() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_BytesSent(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.bytesSent

        assertThat(result)
                .isEqualTo(12345)
    }

    @Test
    fun shouldGetMessagesReceivedPerSecond() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_MessagesRecvPerSecond(playerId) } returns 12345
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.messagesReceivedPerSecond

        assertThat(result)
                .isEqualTo(12345)
    }


    @Test
    fun shouldGetPacketLossPercentage() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_PacketLossPercent(playerId) } returns 95f
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.packetLossPercentage

        assertThat(result)
                .isEqualTo(95f)
    }

    @Test
    fun shouldGetConnectionStatus() {
        val playerId = 100
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { netStats_ConnectionStatus(playerId) } returns ConnectionStatus.CONNECTED.value
        }
        val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerNetworkStatistics.connectionStatus

        assertThat(result)
                .isEqualTo(ConnectionStatus.CONNECTED)
    }

    @Nested
    inner class IpAndPortTests {

        @Test
        fun shouldGetIpAndPort() {
            val playerId = 100
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { netStats_GetIpPort(playerId, any(), 21) } answers {
                    secondArg<ReferenceString>().value = "127.0.0.1:7777"
                    true
                }
            }
            val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            val result = playerNetworkStatistics.ipAndPort

            assertThat(result)
                    .isEqualTo("127.0.0.1:7777")
        }

        @Test
        fun givenIpAndPortIsNullItShouldReturnEmptyString() {
            val playerId = 100
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { netStats_GetIpPort(playerId, any(), 21) } returns true
            }
            val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            val result = playerNetworkStatistics.ipAndPort

            assertThat(result)
                    .isEmpty()
        }
    }

    @Nested
    inner class SummaryStringTests {

        @Test
        fun shouldGetSummaryString() {
            val playerId = 100
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { getPlayerNetworkStats(playerId, any(), 400) } answers {
                    secondArg<ReferenceString>().value = "Some funny numbers"
                    true
                }
            }
            val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            val result = playerNetworkStatistics.summaryString

            assertThat(result)
                    .isEqualTo("Some funny numbers")
        }

        @Test
        fun givenPlayerNetworkStatsIsNullItShouldReturnEmptyString() {
            val playerId = 100
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { getPlayerNetworkStats(playerId, any(), 400) } returns true
            }
            val playerNetworkStatistics = PlayerNetworkStatisticsImpl(
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            val result = playerNetworkStatistics.summaryString

            assertThat(result)
                    .isEmpty()
        }
    }
}