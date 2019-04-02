package servers

import connections.Connection
import requests.Request
import responses.Response

typealias Listener = (Request) -> Unit

interface Server {
    val connection: Connection

    fun start(listener: Listener, port: Int = 8080)
    fun stop()
}