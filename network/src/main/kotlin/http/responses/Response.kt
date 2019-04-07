package http.responses

import connections.Connection
import http.requests.Request
import java.io.InputStream

interface Response {
    val request: Request
    val statusCode: Int
    val headers: Map<String, String>
    val raw: InputStream
    val text: String
    val connection: Connection
}