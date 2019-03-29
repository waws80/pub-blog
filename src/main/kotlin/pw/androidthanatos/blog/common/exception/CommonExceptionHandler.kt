package pw.androidthanatos.blog.common.exception

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.NoHandlerFoundException
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.response.ResponseBean
import javax.servlet.http.HttpServletRequest

/**
 * 全局统一异常处理
 */
@ControllerAdvice
class CommonExceptionHandler{

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @RequestMapping("/exception")
    fun exceptionHandler(request: HttpServletRequest, e: Exception): ResponseBean{
        e.printStackTrace()
        println(request.contextPath)
        return when(e){
            //参数错误
            is ParamsErrorException -> ResponseBean(e.code, e.msg)
            //请求头没有添加token
            is NoTokenException -> ResponseBean(e.code, e.msg)
            //请求头携带token错误
            is TokenErrorException -> ResponseBean(e.code, e.msg)
            //请求携带token过期
            is TokenTimeOutException -> ResponseBean(e.code, e.msg)
            is TokenExpiredException -> ResponseBean(CODE_TOKEN_TIME_OUT, MSG_TOKEN_TIME_OUT)
            //jwt 内部验证错误
            is JWTVerificationException -> ResponseBean(CODE_TOKEN_ERROR, MSG_TOKEN_ERROR)
            //请求路径出错404
            is NoHandlerFoundException -> ResponseBean(CODE_PATH_NOT_FOUND, MSG_PATH_NOT_FOUND, request.requestURI)
            //请求资源不存在
            is ResNotFoundException -> ResponseBean(e.code, e.msg)
            //权限不足
            is PermissionDeniedException -> ResponseBean(e.code, e.msg)
            //用户被拉黑
            is BlackException -> ResponseBean(e.code, e.msg)
            //非法请求异常
            is IllegalRequestException -> ResponseBean(e.code, e.msg)
            is HttpRequestMethodNotSupportedException -> ResponseBean(CODE_REQUEST_METHOD_ERROR, MSG_REQUEST_METHOD_ERROR)
            //服务器异常
            is ServiceErrorException -> ResponseBean(e.code, e.msg)
            else ->ResponseBean(CODE_SERVICE_ERROR, MSG_SERVICE_ERROR)
        }
    }
}