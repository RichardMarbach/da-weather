package com.ds.sensor

import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.random.Random

enum class SensorType {
    TEMPERATURE, WIND_SPEED, HUMIDITY, RAIN;

    companion object {
        fun random(): SensorType =
            values()[Random.nextInt(0, values().size)]

        fun getByName(value: String?) =
            if (value == null) {
                random()
            } else {
                valueOf(value.toUpperCase())
            }

    }
}

typealias Bounds<T> = Pair<T, T>

val <T> Bounds<T>.min: T
    get() = this.first

val <T> Bounds<T>.max: T
    get() = this.second

fun Double.clamp(bounds: Bounds<Double>): Double {
    return Math.min(Math.max(this, bounds.max), bounds.min)
}

fun generateValues(start: Double, averageStep: Double, bounds: Bounds<Double>?): Sequence<Double> {
    return generateSequence(start) {
        val step = Random.nextDouble(-averageStep, averageStep)
        val current = (it + step)
        if (bounds != null) {
            current.clamp(bounds)
        } else {
            current
        }
    }
}

abstract class WeatherSensor(val id: String) {
    abstract val type: SensorType
    protected open val step: Double = 1.0
    abstract val bounds: Bounds<Double>

    private val valueSequence by lazy { generateValues(Random.nextDouble(bounds.min, bounds.max), step, bounds) }

    private val values: Iterator<Double>
        get() = valueSequence.iterator()

    fun serialize(): String {
        val timestamp = DateTimeFormatter.ISO_DATE.format(Instant.now())
        return "$timestamp,$id,$type,${String.format("%.2f", values.next())}"
    }

    override fun toString(): String = "Sensor $id of type $type"

    companion object {
        fun make(type: SensorType, id: String): WeatherSensor =
            when (type) {
                SensorType.TEMPERATURE -> TemperatureSensor(id)
                SensorType.HUMIDITY -> HumiditySensor(id)
                SensorType.WIND_SPEED -> WindSpeedSensor(id)
                SensorType.RAIN -> RainFallSensor(id)
            }
    }
}

class TemperatureSensor(id: String) : WeatherSensor(id) {
    override val type: SensorType
        get() = SensorType.TEMPERATURE

    override val bounds
        get() = Bounds(-60.0, 60.0)
}

class HumiditySensor(id: String) : WeatherSensor(id) {
    override val type: SensorType
        get() = SensorType.HUMIDITY

    override val bounds
        get() = Bounds(0.0, 100.0)
}

class WindSpeedSensor(id: String) : WeatherSensor(id) {
    override val type: SensorType
        get() = SensorType.WIND_SPEED

    override val bounds
        get() = Bounds(0.0, 100.0)
}

class RainFallSensor(id: String) : WeatherSensor(id) {
    override val type: SensorType
        get() = SensorType.RAIN

    override val bounds
        get() = Bounds(0.0, 100.0)
}