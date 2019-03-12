package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerMapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EntityResolverImplTest {

    private lateinit var entityResolver: EntityResolverImpl
    private val playerRegistry: PlayerRegistry = mockk()
    private val vehicleRegistry: VehicleRegistry = mockk()
    private val actorRegistry: ActorRegistry = mockk()
    private val playerClassRegistry: PlayerClassRegistry = mockk()
    private val mapObjectRegistry: MapObjectRegistry = mockk()
    private val pickupRegistry: PickupRegistry = mockk()
    private val textDrawRegistry: TextDrawRegistry = mockk()

    @BeforeEach
    fun setUp() {
        entityResolver = EntityResolverImpl(
                playerRegistry,
                vehicleRegistry,
                actorRegistry,
                playerClassRegistry,
                mapObjectRegistry,
                pickupRegistry,
                textDrawRegistry
        )
    }

    @Nested
    inner class ToPlayerTests {

        @Test
        fun givenPlayerIdIsValidItShouldReturnPlayer() {
            val playerId = 69
            val expectedPlayer = mockk<Player>()
            every { playerRegistry[playerId] } returns expectedPlayer

            val player = entityResolver.run { playerId.toPlayer() }

            assertThat(player)
                    .isEqualTo(expectedPlayer)
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowException() {
            val playerId = 69
            every { playerRegistry[playerId] } returns null

            val caughtException = catchThrowable { entityResolver.run { playerId.toPlayer() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 69")
        }
    }

    @Nested
    inner class ToPlayerOrNullTests {

        @Test
        fun givenPlayerIdIsValidItShouldReturnPlayer() {
            val playerId = 69
            val expectedPlayer = mockk<Player>()
            every { playerRegistry[playerId] } returns expectedPlayer

            val player = entityResolver.run { playerId.toPlayerOrNull() }

            assertThat(player)
                    .isEqualTo(expectedPlayer)
        }

        @Test
        fun givenInvalidPlayerIdItShouldReturnNull() {
            val playerId = 69
            every { playerRegistry[playerId] } returns null

            val player = entityResolver.run { playerId.toPlayerOrNull() }

            assertThat(player)
                    .isNull()
        }
    }

    @Nested
    inner class ToPlayerClassTests {

        @Test
        fun givenPlayerClassIdIsValidItShouldReturnPlayerClass() {
            val playerClassId = 69
            val expectedPlayerClass = mockk<PlayerClass>()
            every { playerClassRegistry[playerClassId] } returns expectedPlayerClass

            val playerClass = entityResolver.run { playerClassId.toPlayerClass() }

            assertThat(playerClass)
                    .isEqualTo(expectedPlayerClass)
        }

        @Test
        fun givenInvalidPlayerClassIdItShouldThrowException() {
            val playerClassId = 69
            every { playerClassRegistry[playerClassId] } returns null

            val caughtException = catchThrowable { entityResolver.run { playerClassId.toPlayerClass() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player class ID 69")
        }
    }

    @Nested
    inner class ToVehicleTests {

        @Test
        fun givenVehicleIdIsValidItShouldReturnVehicle() {
            val vehicleId = 69
            val expectedVehicle = mockk<Vehicle>()
            every { vehicleRegistry[vehicleId] } returns expectedVehicle

            val vehicle = entityResolver.run { vehicleId.toVehicle() }

            assertThat(vehicle)
                    .isEqualTo(expectedVehicle)
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowException() {
            val vehicleId = 69
            every { vehicleRegistry[vehicleId] } returns null

            val caughtException = catchThrowable { entityResolver.run { vehicleId.toVehicle() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 69")
        }
    }

    @Nested
    inner class ToPlayerMapObjectTests {

        private val playerMapObjectRegistry: PlayerMapObjectRegistry = mockk()
        private val player: Player = mockk()

        @BeforeEach
        fun setUp() {
            every { player.playerMapObjectRegistry } returns playerMapObjectRegistry
        }

        @Test
        fun givenPlayerMapObjectIdIsValidItShouldReturnPlayerMapObject() {
            val playerMapObjectId = 69
            val expectedPlayerMapObject = mockk<PlayerMapObject>()
            every { playerMapObjectRegistry[playerMapObjectId] } returns expectedPlayerMapObject

            val playerMapObject = entityResolver.run { playerMapObjectId.toPlayerMapObject(player) }

            assertThat(playerMapObject)
                    .isEqualTo(expectedPlayerMapObject)
        }

        @Test
        fun givenInvalidPlayerMapObjectIdItShouldThrowException() {
            every { player.id } returns PlayerId.valueOf(123)
            val playerMapObjectId = 69
            every { playerMapObjectRegistry[playerMapObjectId] } returns null

            val caughtException = catchThrowable {
                entityResolver.run { playerMapObjectId.toPlayerMapObject(player) }
            }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player map object ID 69 for player ID 123")
        }
    }

    @Nested
    inner class ToMapObjectTests {

        @Test
        fun givenMapObjectIdIsValidItShouldReturnMapObject() {
            val mapObjectId = 69
            val expectedMapObject = mockk<MapObject>()
            every { mapObjectRegistry[mapObjectId] } returns expectedMapObject

            val mapObject = entityResolver.run { mapObjectId.toMapObject() }

            assertThat(mapObject)
                    .isEqualTo(expectedMapObject)
        }

        @Test
        fun givenInvalidMapObjectIdItShouldThrowException() {
            val mapObjectId = 69
            every { mapObjectRegistry[mapObjectId] } returns null

            val caughtException = catchThrowable { entityResolver.run { mapObjectId.toMapObject() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid map object ID 69")
        }
    }

    @Nested
    inner class ToPickupTests {

        @Test
        fun givenPickupIdIsValidItShouldReturnPickup() {
            val pickupId = 69
            val expectedPickup = mockk<Pickup>()
            every { pickupRegistry[pickupId] } returns expectedPickup

            val pickup = entityResolver.run { pickupId.toPickup() }

            assertThat(pickup)
                    .isEqualTo(expectedPickup)
        }

        @Test
        fun givenInvalidPickupIdItShouldThrowException() {
            val pickupId = 69
            every { pickupRegistry[pickupId] } returns null

            val caughtException = catchThrowable { entityResolver.run { pickupId.toPickup() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid pickup ID 69")
        }
    }

    @Nested
    inner class ToActorTests {

        @Test
        fun givenActorIdIsValidItShouldReturnActor() {
            val actorId = 69
            val expectedActor = mockk<Actor>()
            every { actorRegistry[actorId] } returns expectedActor

            val actor = entityResolver.run { actorId.toActor() }

            assertThat(actor)
                    .isEqualTo(expectedActor)
        }

        @Test
        fun givenInvalidActorIdItShouldThrowException() {
            val actorId = 69
            every { actorRegistry[actorId] } returns null

            val caughtException = catchThrowable { entityResolver.run { actorId.toActor() } }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid actor ID 69")
        }
    }

    @Nested
    inner class ToTextDrawOrNullTests {

        @Test
        fun givenTextDrawIdIsValidItShouldReturnTextDraw() {
            val textDrawId = 69
            val expectedTextDraw = mockk<TextDraw>()
            every { textDrawRegistry[textDrawId] } returns expectedTextDraw

            val textDraw = entityResolver.run { textDrawId.toTextDrawOrNull() }

            assertThat(textDraw)
                    .isEqualTo(expectedTextDraw)
        }

        @Test
        fun givenInvalidTextDrawIdItShouldReturnNull() {
            val textDrawId = 69
            every { textDrawRegistry[textDrawId] } returns null

            val textDraw = entityResolver.run { textDrawId.toTextDrawOrNull() }

            assertThat(textDraw)
                    .isNull()
        }
    }

    @Nested
    inner class ToPlayerTextDrawTests {

        private val playerTextDrawRegistry: PlayerTextDrawRegistry = mockk()
        private val player: Player = mockk()

        @BeforeEach
        fun setUp() {
            every { player.playerTextDrawRegistry } returns playerTextDrawRegistry
        }

        @Test
        fun givenPlayerTextDrawIdIsValidItShouldReturnPlayerTextDraw() {
            val playerTextDrawId = 69
            val expectedPlayerTextDraw = mockk<PlayerTextDraw>()
            every { playerTextDrawRegistry[playerTextDrawId] } returns expectedPlayerTextDraw

            val playerTextDraw = entityResolver.run { playerTextDrawId.toPlayerTextDraw(player) }

            assertThat(playerTextDraw)
                    .isEqualTo(expectedPlayerTextDraw)
        }

        @Test
        fun givenInvalidPlayerTextDrawIdItShouldThrowException() {
            every { player.id } returns PlayerId.valueOf(123)
            val playerTextDrawId = 69
            every { playerTextDrawRegistry[playerTextDrawId] } returns null

            val caughtException = catchThrowable {
                entityResolver.run { playerTextDrawId.toPlayerTextDraw(player) }
            }

            assertThat(caughtException)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player text draw ID 69 for player ID 123")
        }
    }

}