package http

import connections.TCPServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import java.net.InetAddress
import java.net.Socket

data class HttpServerConfiguration(val address: InetAddress = InetAddress.getLocalHost(), val port: Int = 80)

class HttpServer(scope: CoroutineScope, val config: HttpServerConfiguration) : CoroutineScope by scope {
    private val server = TCPServer(scope, config.port, config.address)

    suspend fun start() {
        server.listen(::handler)
    }

    fun handler(socket: Socket) {

    }

    fun stop() {
        server.stop()
    }
}