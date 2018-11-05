package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.HasVehicle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider
import java.util.concurrent.TimeUnit

internal sealed class StreamableMapObjectState {

    abstract fun onEnter(streamableMapObject: StreamableMapObjectImpl)

    abstract fun onStreamIn(playerMapObject: PlayerMapObject)

    abstract fun onLeave(streamableMapObject: StreamableMapObjectImpl)

    abstract val coordinates: Vector3D

    abstract val rotation: Vector3D

    internal class FixedCoordinates(
            coordinates: Vector3D,
            rotation: Vector3D
    ) : StreamableMapObjectState() {

        override val coordinates = coordinates.toVector3D()

        override val rotation = rotation.toVector3D()

        override fun onStreamIn(playerMapObject: PlayerMapObject) {}

        override fun onEnter(streamableMapObject: StreamableMapObjectImpl) {}

        override fun onLeave(streamableMapObject: StreamableMapObjectImpl) {}

    }

    internal class Moving(
            private val onMoved: () -> Unit,
            origin: Vector3D,
            destination: Vector3D,
            startRotation: Vector3D,
            targetRotation: Vector3D?,
            private val speed: Float,
            private val timeProvider: TimeProvider,
            timerExecutor: TimerExecutor
    ) : StreamableMapObjectState() {

        private val origin = origin.toVector3D()

        private var destination = destination.toVector3D()

        private val startRotation = startRotation.toVector3D()

        private val targetRotation = targetRotation?.toVector3D()

        private val startTimeInMs: Long = timeProvider.getCurrentTimeInMs()

        private val distanceToMove = origin.distanceTo(destination)

        private val duration: Long = Math.round((distanceToMove / speed) * 1000f).toLong()

        private val timer: Timer

        private var stopped: Boolean = false

        init {
            timer = timerExecutor.addTimer(duration, TimeUnit.MILLISECONDS) {
                if (!stopped) {
                    onMoved()
                    stopped = true
                }
            }
        }

        override val coordinates: Vector3D
            get() = interpolate(origin, destination)

        override val rotation: Vector3D
            get() = when (targetRotation) {
                null -> startRotation
                else -> interpolate(startRotation, targetRotation)
            }

        private fun interpolate(start: Vector3D, end: Vector3D): Vector3D {
            if (duration <= 0) {
                return end
            }
            val currentTimeInMs = timeProvider.getCurrentTimeInMs()
            val timeDifference = currentTimeInMs - startTimeInMs
            if (timeDifference >= duration) {
                return end
            }
            val progress: Float = timeDifference.toFloat() / duration.toFloat()
            val dx = end.x - start.x
            val dy = end.y - start.y
            val dz = end.z - start.z
            val x = start.x + progress * dx
            val y = start.y + progress * dy
            val z = start.z + progress * dz
            return vector3DOf(x = x, y = y, z = z)
        }

        override fun onEnter(streamableMapObject: StreamableMapObjectImpl) {
            streamableMapObject.playerMapObjects.forEach { move(it) }
        }

        override fun onStreamIn(playerMapObject: PlayerMapObject) {
            move(playerMapObject)
        }

        private fun move(playerMapObject: PlayerMapObject) {
            playerMapObject.moveTo(coordinates = destination, speed = speed, rotation = targetRotation)
        }

        override fun onLeave(streamableMapObject: StreamableMapObjectImpl) {
            if (!stopped) {
                timer.stop()
                streamableMapObject.playerMapObjects.forEach { it.stop() }
                stopped = true
            }
        }

    }

    internal sealed class Attached(
            offset: Vector3D,
            attachRotation: Vector3D
    ) : StreamableMapObjectState() {

        protected val attachRotation = attachRotation.toVector3D()

        protected val offset = offset.toVector3D()

        protected abstract val entityAngle: Float

        protected abstract val entityCoordinates: Vector3D

        override val coordinates: Vector3D
            get() {
                val radians = Math.toRadians(this.entityAngle.toDouble())
                val sin = Math.sin(radians)
                val cos = Math.cos(radians)
                val x = offset.x * cos - offset.y * sin
                val y = offset.x * sin + offset.y * cos
                return entityCoordinates + vector3DOf(x.toFloat(), y.toFloat(), offset.z)
            }

        override val rotation: Vector3D
            get() = vector3DOf(attachRotation.x, attachRotation.y, attachRotation.z + entityAngle)

        override fun onEnter(streamableMapObject: StreamableMapObjectImpl) {
            streamableMapObject.playerMapObjects.forEach { attach(it) }
        }

        override fun onStreamIn(playerMapObject: PlayerMapObject) {
            attach(playerMapObject)
        }

        override fun onLeave(streamableMapObject: StreamableMapObjectImpl) {
            streamableMapObject.forceStreamOut()
        }

        protected abstract fun attach(playerMapObject: PlayerMapObject)

        internal class ToVehicle(
                override val vehicle: Vehicle,
                offset: Vector3D,
                attachRotation: Vector3D
        ) : Attached(offset = offset, attachRotation = attachRotation), HasVehicle {

            override val entityAngle: Float
                get() = vehicle.angle

            override val entityCoordinates: Vector3D
                get() = vehicle.coordinates

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(vehicle, offset, attachRotation)
            }

        }

        internal class ToPlayer(
                override val player: Player,
                offset: Vector3D,
                attachRotation: Vector3D
        ) : Attached(offset = offset, attachRotation = attachRotation), HasPlayer {

            override val entityAngle: Float
                get() = player.angle

            override val entityCoordinates: Vector3D
                get() = player.coordinates

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(player, offset, attachRotation)
            }

        }

    }

}