package http.requests

import http.HttpNotImplementedException
import http.HttpVersionNotSupportedException
import http.responses.Response
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.util.*

data class GenericRequest(
    override val method: String,
    override val url: String,
    override val params: Map<String, String>,
    override val headers: Map<String, String>,
    override val data: Any?,
    override val timeout: Double,
    override val body: ByteArrayOutputStream
) : Request

fun parse(input: BufferedReader): Request {
    val parser = StringTokenizer(input.readLine(), " \t")

    TODO()
}

fun parseHttpStatusLine(line: String): HttpStatusLine {
    val parser = StringTokenizer(line)
    val method = parser.nextToken().toUpperCase()
    val path = parser.nextToken()
    val protocol = parser.nextToken()

    if (protocol != "Http/1.1") {
        throw HttpVersionNotSupportedException()
    }

    return HttpStatusLine(method, path, protocol)
}

private fun parseStatusLine() {

}

private fun parseHeaders() {

}

private fun parseBody() {

}

data class HttpStatusLine(val method: String, val path: String, val protocol: String)


//fun parseHttpStatusLine(line: String): HttpStatusLine {
//    val parser = StringTokenizer(line)
//    val method = parser.nextToken().toUpperCase()
//    val path = parser.nextToken()
//    val protocol = parser.nextToken()
//
//    if (protocol != "Http/1.1") {
//        throw HttpVersionNotSupportedException()
//    }
//
//    return HttpStatusLine(method, path, protocol)
//}

fun processHttpRequest(input: BufferedReader): Response {
    val statusLine = parseHttpStatusLine(input.readLine())

    if (statusLine.method != "GET") throw HttpNotImplementedException()

    TODO()
//    return "Http/1.1 200 OK\r\n\r\nHello from ${statusLine.path}"
}

fun processInput(socket: Socket) {
    val input = socket.getInputStream().bufferedReader()
    val out = socket.getOutputStream().bufferedWriter()

    try {
        val response = processHttpRequest(input)
        out.write(response.toString())
    } catch (e: Exception) {
        out.write("HTTP/1.1 500 Internal Server Error\r\n\r\n${e.message}")
    }
    out.flush()
}