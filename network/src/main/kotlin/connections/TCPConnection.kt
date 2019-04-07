package connections

import kotlinx.coroutines.*
import mu.KotlinLogging
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

private val logger = KotlinLogging.logger { }

typealias ConnectionHandler = (Socket) -> Unit

class TCPServer (
    scope: CoroutineScope,
    private val port: Int = 80,
    private val address: InetAddress = InetAddress.getLocalHost(),
    backlog: Int = 100
) :
    CoroutineScope by scope {
    private val server = ServerSocket(port, backlog, address)

    suspend fun listen(handler: ConnectionHandler) = withContext(Dispatchers.IO) {
        logger.info { "Listening on $address:$port" }
        while (isActive) {
            val connection = server.accept()
            launch(Dispatchers.IO) {
                try {
                    handler(connection)
                } catch (e: IOException) {
                } finally {
                    connection.close()
                }
            }
        }
    }

    fun stop() {
        server.close()
    }
}