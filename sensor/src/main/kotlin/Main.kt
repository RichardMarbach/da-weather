package com.ds.sensor

import com.ds.sensor.SensorType.Companion.random
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import isIpAddress
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.util.*

class Sensor : CliktCommand() {
    private val uuid by option(help = "Sensor ID").default(UUID.randomUUID().toString())

    private val type by option(help = SensorType.values().joinToString(", "))
        .convert("SensorType") {
            try {
                SensorType.getByName(it)
            } catch (e: IllegalArgumentException) {
                fail("Unknown Sensor type '$it' \nAvailable types: ${SensorType.values().joinToString(", ")}")
            }
        }
        .default(random())

    private val rate by option(help = "Rate at which sensor should send data (ms)").long().default(100)

    private val station by argument("station", help = "Weather station server (host:port)")
        .convert {
            val entry = it.split(":")
            if (entry.size == 2) {
                try {
                    require(entry.first().isIpAddress()) { "Invalid IP Address" }

                    InetSocketAddress.createUnresolved(entry.first(), entry.last().toInt())
                } catch (e: NumberFormatException) {
                    fail("Invalid port: ${entry.last()}")
                } catch (e: java.lang.IllegalArgumentException) {
                    fail("port must be between 0 and 65535")
                }
            } else {
                fail("Should be in format of host:port")
            }
        }

    override fun run() = runBlocking {
        val sensor = WeatherSensor.make(type, id = uuid)
        val server = WeatherServer(sensor, rate, station)

        server.start()
    }
}

fun main(args: Array<String>) = Sensor().main(args)