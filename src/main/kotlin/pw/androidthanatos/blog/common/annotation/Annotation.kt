package pw.androidthanatos.blog.common.annotation

import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.response.*

/**
 * 登录鉴权注解
 * ＠value true:　需要登录  false: 不需要登录
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Login(val value: Boolean = true)


/**
 * 标记[ResponseBean] 可使用的　code
 */
@IntDef(CODE_SUCCESS,
        CODE_FAILURE,
        CODE_PARAMS_ERROR,
        CODE_PHONE_HAS_REGISTER,
        CODE_HEADER_NO_TOKEN,
        CODE_PATH_NOT_FOUND,
        CODE_TOKEN_ERROR,
        CODE_TOKEN_TIME_OUT,
        CODE_SERVICE_ERROR)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ResponseCode

/**
 * 标记[ResponseBean] 可使用的　msg
 */
@StringDef(MSG_SUCCESS,
        MSG_FAILURE,
        MSG_PARAMS_ERROR,
        MSG_PHONE_HAS_REGISTER,
        MSG_HEADER_NO_TOKEN,
        MSG_PATH_NOT_FOUND,
        MSG_TOKEN_ERROR,
        MSG_TOKEN_TIME_OUT,
        MSG_SERVICE_ERROR)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ResponseMsg

/**
 * 标记目标所使用的数值范围
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class IntDef(vararg val value: Int)

/**
 * 标记目标所使用的字符串范围
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class StringDef(vararg val value: String)

/**
 * 用户状态
 */
@IntDef(STATUS_USER_NORMAL, STATUS_USER_BLACK)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class UserStatus

/**
 * 用户状态
 */
@IntDef(TYPE_USER_NORMAL, TYPE_USER_ADMIN)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class UserType