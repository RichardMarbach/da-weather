import connections.UDPClient
import connections.UDPConnection
import java.lang.Thread.sleep

fun main() {
    val server = UDPConnection().listen { println(String(it)) }
    val client = UDPClient()

    client.send("Hello, World".toByteArray())
    client.send("Hello, World".toByteArray())
    client.send("Hello, World".toByteArray())
    client.send("Hello, World".toByteArray())

    sleep(100)
    server.close()
}