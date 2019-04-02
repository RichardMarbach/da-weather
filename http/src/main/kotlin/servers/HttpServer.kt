package servers

import connections.Connection
import mu.KotlinLogging
import requests.parse
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger { }

class HttpServer(override val connection: Connection, workerCount: Int = 4) : Server {
    private val workerPool = Executors.newFixedThreadPool(workerCount)

    override fun start(listener: Listener, port: Int) {
        logger.info { "Starting HTTP Server on $connection" }
        connection.listen(handlePacket(listener))
    }

    private fun handlePacket(listener: Listener): connections.Listener = {
        workerPool.execute(HttpWorker(it, listener))
    }

    override fun stop() {
        logger.info { "Stopping HTTP Server..." }
        workerPool.shutdown()
        while (!workerPool.isTerminated) {
        }
        logger.info { "HTTP Server shutdown" }
    }
}

class HttpWorker(private val data: ByteArray, val listener: Listener) : Runnable {
    override fun run() {
        val request = parse(String(data))
        if (request == null) {
            logger.warn { "Malformed HTTP request: ${String(data)}" }
            return
        }

        logger.trace { "Received request: $request" }
        listener(request)
    }
}

