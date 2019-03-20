package pw.androidthanatos.blog.exception

import pw.androidthanatos.blog.contract.CODE_HEADER_NO_TOKEN
import pw.androidthanatos.blog.contract.CODE_TOKEN_ERROR
import pw.androidthanatos.blog.contract.MSG_HEADER_NO_TOKEN
import pw.androidthanatos.blog.contract.MSG_TOKEN_ERROR

/**
 * 请求头未发现token异常
 */
class NoTokenException(val code: Int = CODE_HEADER_NO_TOKEN,
                       val msg: String = MSG_HEADER_NO_TOKEN): Exception(msg)

/**
 * 请求token错误异常
 */
class TokenErrorException(val code: Int = CODE_TOKEN_ERROR,
                          val msg: String = MSG_TOKEN_ERROR): Exception(msg)

/**
 * 请求token过期异常
 */
class TokenTimeOutException(val code: Int = CODE_TOKEN_ERROR,
                            val msg: String = MSG_TOKEN_ERROR): Exception(msg)