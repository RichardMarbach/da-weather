package requests

import java.io.ByteArrayOutputStream

interface Request {
    val method: String
    val url: String
    val params: Map<String, String>
    val headers: Map<String, String>
    val data: Any?
    val timeout: Double
    val body: ByteArrayOutputStream
}