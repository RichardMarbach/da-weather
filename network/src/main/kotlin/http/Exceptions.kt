package http

open class HttpException(msg: String, code: Int) : Exception("$code $msg")

class HttpNotImplementedException : HttpException("Not Implemented", 501)
class HttpVersionNotSupportedException : HttpException("Http Version Not Supported", 505)
