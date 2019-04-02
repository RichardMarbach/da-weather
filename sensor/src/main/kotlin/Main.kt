package com.ds.sensor

import mu.KotlinLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class EchoServer : Thread() {

    private val socket: DatagramSocket = DatagramSocket(4445)
    var running: Boolean = false
    private var buf = ByteArray(256)

    override fun run() {
        running = true

        while (running) {
            var packet = DatagramPacket(buf, buf.size)
            socket.receive(packet)

            val address = packet.address
            val port = packet.port
            packet = DatagramPacket(buf, buf.size, address, port)
            val length = packet.data.indexOfFirst { it == 0.toByte() }
            val received = String(packet.data, packet.offset, if (length == -1) packet.length else length)

            logger.info { "Received: $received -- ${received.length}" }

            if (received.matches("end".toRegex())) {
                running = false
                continue
            }
            socket.send(packet)
            buf = ByteArray(256)
        }
        logger.info { "Close socket" }
        socket.close()
    }
}

class EchoClient {
    private val socket: DatagramSocket = DatagramSocket()
    private val address: InetAddress = InetAddress.getByName("localhost")

    private var buf: ByteArray? = null

    fun sendEcho(msg: String): String {
        buf = msg.toByteArray()
        var packet = DatagramPacket(buf!!, buf!!.size, address, 4445)
        socket.send(packet)
        packet = DatagramPacket(buf!!, buf!!.size)
        socket.receive(packet)
        return String(
            packet.data, 0, packet.length
        )
    }

    fun close() {
        socket.close()
    }
}

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val server = EchoServer()
    server.start()
    val client = EchoClient()

    var echo = client.sendEcho("hello server")
    logger.info { echo }
    echo = client.sendEcho("server is working")
    logger.info { echo }

    echo = client.sendEcho("end")
    logger.info { echo }
    client.close()
    logger.info { server.running }
}