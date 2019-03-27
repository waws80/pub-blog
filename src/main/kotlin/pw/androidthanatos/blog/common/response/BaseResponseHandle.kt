package pw.androidthanatos.blog.common.response

import javax.servlet.http.HttpServletRequest

/**
 * 响应体执行类
 */
abstract class BaseResponseHandle<REQ: HttpServletRequest> : ResponseHandle<REQ>