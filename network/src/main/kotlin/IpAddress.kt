private val IPValidator =
    "\\A(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\z".toRegex()

fun String.isIpAddress(): Boolean {
    return this.matches(IPValidator)
}