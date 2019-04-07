package connections

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

internal const val MAX_DATAGRAM_SIZE = 65535

class UDPConnection : ReadWriteConnection {
    private val datagramChannel = DatagramChannel.open()

    init {
        datagramChannel.configureBlocking(false)
    }

    override val incoming = Channel<ByteBuffer>()

    override fun bind(address: SocketAddress) {
        datagramChannel.bind(address)
    }

    fun connect(address: SocketAddress) {
        datagramChannel.connect(address)
    }

    override suspend fun read() = withContext(Dispatchers.IO) {
        val buf = ByteBuffer.allocate(MAX_DATAGRAM_SIZE)
        @Suppress("SENSELESS_COMPARISON")
        if (datagramChannel.read(buf) != null) {
            buf.asReadOnlyBuffer()
            incoming.send(buf)
        }
    }

    override suspend fun write(data: ByteArray, dest: SocketAddress) = withContext<Unit>(Dispatchers.IO) {
        require(data.size <= MAX_DATAGRAM_SIZE) { "UDP packet too large: ${data.size}" }

        val buf = ByteBuffer.allocate(data.size)
        buf.put(data)
        buf.flip()
        datagramChannel.send(buf, dest)
    }

    override fun close() {
        datagramChannel.close()
        incoming.close()
    }
}