import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.BufferedReader
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.Date
private val logger = KotlinLogging.logger { }
fun main() = runBlocking(Dispatchers.IO) {
    startDB()
    val server = ServerSocket(80, 50, InetAddress.getByName("0.0.0.0"))
    while (true) {
        val socket = server.accept()
        launch {
            handleHttpConnection(socket)
        }
    }
}
fun startDB() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(SensorReadings)
        SensorReadings.insert {
            it[sensorId] = UUID.randomUUID()
            it[takenAt] = Date().time
            it[sensorType] = "Humidity"
            it[reading] = 1.1
        }
    }
}
object SensorReadings : Table() {
    val sensorId = uuid("sensor_id")
    val takenAt = long("takenAt")
    val sensorType = varchar("sensorType", 50)
    val reading = double("reading")
}
fun httpResponse(code: Int, msg: String, content: String): String {
    return "HTTP/1.1 $code $msg \r\nContent-Type:text/html\r\nContent-Length: ${content.length}\r\n\r\n$content"
}
fun handleHttpConnection(socket: Socket) {
    try {
        val input = socket.getInputStream().bufferedReader(Charsets.US_ASCII)
        val parser = input.readLine().split(" ")
        logger.info { parser }
        require(parser.size == 3)
        require(parser[2] == "HTTP/1.1")
        if (parser[0] != "GET") {
            socket.getOutputStream().write(httpResponse(501, "Not Implemented", "").toByteArray())
            return
        }
        val response = router(parser[1], input)
        logger.info { response }
        socket.getOutputStream().write(response.toByteArray())
    } catch (e: IllegalArgumentException) {
        socket.getOutputStream().write(httpResponse(505, "HTTP VERSION NOT SUPPORTED", "").toByteArray())
    } catch (e: IOException) {
    } finally {
        try {
            socket.close()
        } catch (e: IOException) {
        }
    }
}
typealias Handler = (BufferedReader) -> String
data class SensorReadingJson(val takenAt: Long, val type: String, val reading: Double) {
    companion object {
        fun fromRow(result: ResultRow): SensorReadingJson {
            return SensorReadingJson(
                result[SensorReadings.takenAt],
                result[SensorReadings.sensorType],
                result[SensorReadings.reading]
            )
        }
    }
}
private val routes = mapOf<String, Handler>(
    "/" to { _ ->
        val readings = transaction {
            SensorReadings.selectAll().map { SensorReadingJson.fromRow(it) }
        }
//        val readings =
//        logger.info { Gson().toJson(readings) }
        httpResponse(200, "OK", "Found")
    },
    "/history" to { _ -> httpResponse(200, "OK", "History") }
)
fun router(route: String, input: BufferedReader): String {
    return routes.getOrDefault(route, { _ -> httpResponse(404, "Not Found", "") })(input)
}
