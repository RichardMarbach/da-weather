package requests

import java.io.ByteArrayOutputStream

data class GenericRequest(
    override val method: String,
    override val url: String,
    override val params: Map<String, String>,
    override val headers: Map<String, String>,
    override val data: Any?,
    override val timeout: Double,
    override val body: ByteArrayOutputStream
) : Request {

}

fun parse(data: String): Request? {
    TODO()
}