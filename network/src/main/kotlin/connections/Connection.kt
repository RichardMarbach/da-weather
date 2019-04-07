package connections

import kotlinx.coroutines.channels.Channel
import java.io.Closeable
import java.net.SocketAddress
import java.nio.ByteBuffer

interface Connection : Closeable {
    fun bind(address: SocketAddress)
}

interface ReadConnection : Connection {
    val incoming: Channel<ByteBuffer>

    suspend fun read()
}

interface WriteConnection : Connection {
    suspend fun write(data: ByteArray, dest: SocketAddress)
}

interface ReadWriteConnection : ReadConnection, WriteConnection



