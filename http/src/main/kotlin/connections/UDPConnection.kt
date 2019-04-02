package connections

import mu.KotlinLogging
import java.net.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger { }

class UDPConnection(override val host: String = "127.0.0.1", override val port: Int = 8080) :
    Connection, Thread() {
    override fun send() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val address = InetAddress.getByName(host)
    private val socket: DatagramSocket = DatagramSocket(port, address)

    private var buffer = ByteArray(socket.receiveBufferSize)

    private var listener: Listener? = null

    override fun open(): Boolean {
        return !socket.isClosed
    }

    override fun listen(listener: Listener): Connection {
        logger.info { "Listening on: $this" }
        this.listener = listener
        start()
        return this
    }

    override fun run() {
        try {
            while (open()) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val result = ByteArray(packet.length)

                listener?.invoke(
                    // Remove trailing null bytes in the buffer as they break string comparison
                    packet.data.copyInto(result, 0, packet.offset, packet.length),
                    packet.address,
                    packet.port
                ).also { logger.trace { "Received packet for $this: $packet" } }
                // Zero the buffer
                buffer = ByteArray(socket.receiveBufferSize)
            }
        } catch (e: SocketException) {
            logger.info(e) { "$this: ${e.message}" }
        }
    }

    override fun close() {
        logger.info { "Closing: $this" }
        socket.close()
    }

    override fun toString(): String {
        return "$host:$port"
    }
}