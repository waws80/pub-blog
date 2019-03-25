package pw.androidthanatos.blog.common.exception

import pw.androidthanatos.blog.common.contract.*

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

/**
 * 参数错误异常
 */
class ParamsErrorException(val code: Int = CODE_PARAMS_ERROR,
                           val msg: String = MSG_PARAMS_ERROR): Exception(msg)


/**
 * 权限不足异常
 */
class PermissionDeniedException(val code: Int = CODE_PERMISSION_DENIED,
                                val msg: String = MSG_PERMISSION_DENIED): Exception(msg)

/**
 * 用户被拉黑
 */
class BlackException(val code: Int = CODE_USER_STATUS_BLACK,
                     val msg: String = MSG_USER_STATUS_BLACK): Exception(msg)