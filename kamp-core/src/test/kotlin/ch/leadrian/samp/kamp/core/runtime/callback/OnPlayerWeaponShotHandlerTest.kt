package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Result
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class OnPlayerWeaponShotHandlerTest {


    private val onPlayerWeaponShotHandler = OnPlayerWeaponShotHandler()
    private val player = mockk<Player>()
    private val weaponModel = WeaponModel.AK47
    private val coordinates = vector3DOf(1f, 2f, 3f)

    @ParameterizedTest
    @ArgumentsSource(TargetArgumentsProvider::class)
    fun givenNoListenerItShouldReturnAllowDamageResult(target: Target) {
        val result = onPlayerWeaponShotHandler.onPlayerShotWeapon(player, weaponModel, target, coordinates)

        assertThat(result)
                .isEqualTo(Result.AllowDamage)
    }

    @ParameterizedTest
    @ArgumentsSource(TargetArgumentsProvider::class)
    fun givenAllListenersReturnAllowDamageItShouldReturnAllowDamage(target: Target) {
        val listener1 = mockk<OnPlayerWeaponShotListener> {
            every {
                onPlayerShotWeapon(player, weaponModel, target, coordinates)
            } returns Result.AllowDamage
        }
        val listener2 = mockk<OnPlayerWeaponShotListener> {
            every {
                onPlayerShotWeapon(player, weaponModel, target, coordinates)
            } returns Result.AllowDamage
        }
        val listener3 = mockk<OnPlayerWeaponShotListener> {
            every {
                onPlayerShotWeapon(player, weaponModel, target, coordinates)
            } returns Result.AllowDamage
        }
        val onPlayerWeaponShotHandler = OnPlayerWeaponShotHandler()
        onPlayerWeaponShotHandler.register(listener1)
        onPlayerWeaponShotHandler.register(listener2)
        onPlayerWeaponShotHandler.register(listener3)

        val result = onPlayerWeaponShotHandler.onPlayerShotWeapon(player, weaponModel, target, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerShotWeapon(player, weaponModel, target, coordinates)
            listener2.onPlayerShotWeapon(player, weaponModel, target, coordinates)
            listener3.onPlayerShotWeapon(player, weaponModel, target, coordinates)
        }
        assertThat(result)
                .isEqualTo(Result.AllowDamage)
    }

    @ParameterizedTest
    @ArgumentsSource(TargetArgumentsProvider::class)
    fun shouldStopWithFirstPreventDamageResult(target: Target) {
        val listener1 = mockk<OnPlayerWeaponShotListener> {
            every { onPlayerShotWeapon(player, weaponModel, target, coordinates) } returns Result.AllowDamage
        }
        val listener2 = mockk<OnPlayerWeaponShotListener> {
            every { onPlayerShotWeapon(player, weaponModel, target, coordinates) } returns Result.PreventDamage
        }
        val listener3 = mockk<OnPlayerWeaponShotListener>()
        val onPlayerWeaponShotHandler = OnPlayerWeaponShotHandler()
        onPlayerWeaponShotHandler.register(listener1)
        onPlayerWeaponShotHandler.register(listener2)
        onPlayerWeaponShotHandler.register(listener3)

        val result = onPlayerWeaponShotHandler.onPlayerShotWeapon(player, weaponModel, target, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerShotWeapon(player, weaponModel, target, coordinates)
            listener2.onPlayerShotWeapon(player, weaponModel, target, coordinates)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.PreventDamage)
    }

    private class TargetArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(Target.PlayerTarget(mockk())),
                        Arguments.of(Target.VehicleTarget(mockk())),
                        Arguments.of(Target.PlayerMapObjectTarget(mockk())),
                        Arguments.of(Target.MapObjectTarget(mockk())),
                        Arguments.of(Target.NoTarget)
                )

    }

}