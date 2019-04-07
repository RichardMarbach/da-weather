package com.ds.sensor

import connections.UDPConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.net.InetSocketAddress

private val logger = KotlinLogging.logger {}

class WeatherServer(
    private val sensor: WeatherSensor,
    private val pollRate: Long,
    private val station: InetSocketAddress
) {
    private val server = UDPConnection()

    init {
        server.connect(station)
    }

    suspend fun start() = withContext(Dispatchers.Default) {
        logger.info { "Starting $sensor on $server at rate $pollRate" }

        while (isActive) {
            server.write(sensor.serialize().toByteArray(), station)
            delay(pollRate)
        }
    }
}