package pw.androidthanatos.blog.exception

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import pw.androidthanatos.blog.contract.CODE_SERVICE_ERROR
import pw.androidthanatos.blog.contract.MSG_SERVICE_ERROR
import pw.androidthanatos.blog.response.ResponseBean
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
            is NoTokenException -> ResponseBean(e.code, e.msg)
            is TokenErrorException -> ResponseBean(e.code, e.msg)
            is TokenTimeOutException -> ResponseBean(e.code, e.msg)
            else ->ResponseBean(CODE_SERVICE_ERROR, MSG_SERVICE_ERROR)
        }
    }
}