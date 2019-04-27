package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerNamePropertyFactoryTest {

    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler = mockk()
    private lateinit var playerNamePropertyFactory: PlayerNamePropertyFactory

    @BeforeEach
    fun setUp() {
        playerNamePropertyFactory = PlayerNamePropertyFactory(nativeFunctionExecutor, onPlayerNameChangeHandler)
    }

    @Test
    fun cacheNamesShouldInitiallyBeTrue() {
        assertThat(playerNamePropertyFactory.cacheNames)
                .isTrue()
    }

    @Test
    fun givenCacheNamesIsTrueItShouldReturnCachingPlayerNameProperty() {
        val playerNameProperty = playerNamePropertyFactory.create()

        assertThat(playerNameProperty)
                .isInstanceOf(CachingPlayerNameProperty::class.java)
    }

    @Test
    fun givenCacheNamesIsFalseItShouldReturnNonCachingPlayerNameProperty() {
        playerNamePropertyFactory.cacheNames = false

        val playerNameProperty = playerNamePropertyFactory.create()

        assertThat(playerNameProperty)
                .isInstanceOf(NonCachingPlayerNameProperty::class.java)
    }

}