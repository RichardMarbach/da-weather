package connections

interface ConnectionClient {
    val address: String
    val port: Int

    fun send(data: ByteArray)
    fun send(data: ByteArray, address: String, port: Int)
}