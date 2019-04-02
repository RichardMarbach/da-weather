package connections

import mu.KotlinLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private val logger = KotlinLogging.logger { }

class UDPClient(override val address: String = "127.0.0.1", override val port: Int = 8080) : ConnectionClient {
    private val dest: InetAddress = InetAddress.getByName(address)
    private val socket = DatagramSocket()

    private fun bufferSize(data: ByteArray): Int {
        if (data.size > socket.sendBufferSize) {
            logger.warn { "$this -- UDP packet truncated: ${data.size} > ${socket.sendBufferSize}" }
        }
        return Math.min(data.size, socket.sendBufferSize)
    }

    private fun send(data: ByteArray, address: InetAddress, port: Int) {
        val packet = DatagramPacket(data, bufferSize(data), address, port)
        logger.trace { "Send packet to $address:$port" }
        socket.send(packet)
    }

    override fun send(data: ByteArray, address: String, port: Int) {
        send(data, InetAddress.getByName(address), port)
    }

    override fun send(data: ByteArray) {
        send(data, dest, port)
    }

    override fun toString(): String {
        return "$address:$port"
    }
}