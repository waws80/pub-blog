package pw.androidthanatos.blog.common.annotation

import org.springframework.web.bind.annotation.Mapping
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.response.*

/**
 * 登录鉴权注解
 * ＠value true:　需要登录  false: 不需要登录
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Login(val value: Boolean = true)

/**
 * 接口版本控制注解
 */
@Mapping
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiVersion(val value: Int)


/**
 * 标记[ResponseBean] 可使用的　code
 */
@IntDef(CODE_SUCCESS,
        CODE_PARAMS_ERROR,
        CODE_PHONE_HAS_REGISTER,
        CODE_PHONE_NOT_REGISTER,
        CODE_LOGIN_PARAMS_ERROR,
        CODE_PERMISSION_DENIED,
        CODE_USER_STATUS_BLACK,
        CODE_INFO_TILE_REPEAT,
        CODE_HEADER_NO_TOKEN,
        CODE_PATH_NOT_FOUND,
        CODE_RES_NOT_FOUND,
        CODE_LOAD_URL_BODY_ERROR,
        CODE_TOKEN_ERROR,
        CODE_TOKEN_TIME_OUT,
        CODE_SERVICE_ERROR,
        CODE_REQUEST_METHOD_ERROR,
        CODE_ILLEGAL_REQUEST)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ResponseCode

/**
 * 标记[ResponseBean] 可使用的　msg
 */
@StringDef(MSG_SUCCESS,
        MSG_PARAMS_ERROR,
        MSG_PHONE_HAS_REGISTER,
        MSG_PHONE_NOT_REGISTER,
        MSG_LOGIN_PARAMS_ERROR,
        MSG_HEADER_NO_TOKEN,
        MSG_PERMISSION_DENIED,
        MSG_USER_STATUS_BLACK,
        MSG_INFO_TILE_REPEAT,
        MSG_PATH_NOT_FOUND,
        MSG_RES_NOT_FOUND,
        MSG_LOAD_URL_BODY_ERROR,
        MSG_TOKEN_ERROR,
        MSG_TOKEN_TIME_OUT,
        MSG_SERVICE_ERROR,
        MSG_REQUEST_METHOD_ERROR,
        MSG_ILLEGAL_REQUEST)
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

/**
 * 待办事项类型
 */
@IntDef(TODO_TYPE_NORMAL,
        TODO_TYPE_STUDY,
        TODO_TYPE_WORK,
        TODO_TYPE_PLAY,
        TODO_TYPE_FAMILY,
        TODO_TYPE_OTHER)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TodoType

/**
 * 待办事项列表排序类型
 */
@IntDef(TODO_LIST_NORMAL, TODO_LIST_TOP)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TodoListType

/**
 * 待办事项删除类型
 */
@IntDef(TODO_DEL_NORMAL, TODO_DEL_DEL)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TodoDelType

/**
 * 待办事项提醒类型
 */
@IntDef(TODO_REMIND_NORMAL, TODO_REMIND_REMIND)
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TodoRemindType

