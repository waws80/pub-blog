package pw.androidthanatos.blog.common.exception

import com.auth0.jwt.exceptions.SignatureVerificationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
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
    fun exceptionHandler(request: HttpServletRequest, e: Exception): ResponseBean{
        e.printStackTrace()
        println(request.contextPath)
        return when(e){
            //请求头没有添加token
            is NoTokenException -> ResponseBean(e.code, e.msg)
            //请求头携带token错误
            is TokenErrorException -> ResponseBean(e.code, e.msg)
            is SignatureVerificationException -> ResponseBean(CODE_TOKEN_ERROR, MSG_TOKEN_ERROR)
            //请求携带token过期
            is TokenTimeOutException -> ResponseBean(e.code, e.msg)
            //请求路径出错404
            is NoHandlerFoundException -> ResponseBean(CODE_PATH_NOT_FOUND, MSG_PATH_NOT_FOUND, e.message)
            //服务器异常
            else ->ResponseBean(CODE_SERVICE_ERROR, MSG_SERVICE_ERROR)
        }
    }
}