package connections

import java.net.InetAddress

typealias Listener = (data: ByteArray, address: InetAddress, port: Int) -> Unit

// TODO: HTTP Server must respond to requests
interface Connection {
    val host: String
    val port: Int

    fun open(): Boolean
    fun listen(listener: Listener): Connection
    fun send()
    fun close()
}