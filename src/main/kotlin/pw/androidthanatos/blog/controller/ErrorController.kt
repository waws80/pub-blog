package pw.androidthanatos.blog.controller

import com.auth0.jwt.exceptions.TokenExpiredException
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.NoHandlerFoundException
import pw.androidthanatos.blog.common.exception.ServiceErrorException
import pw.androidthanatos.blog.common.exception.TokenTimeOutException
import javax.servlet.http.HttpServletRequest

/**
 * 控制器外异常处理器
 */
@RestController
class ErrorController : BasicErrorController(DefaultErrorAttributes(true), ErrorProperties()) {

    @RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    override fun error(request: HttpServletRequest?): ResponseEntity<MutableMap<String, Any>> {
        val body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL))
        val exceptionClass = body["exception"]
        when(body["status"]!!.toString().toInt()){
            404 -> throw NoHandlerFoundException("", body["path"].toString(), HttpHeaders())
            else ->{
                when (exceptionClass){
                    TokenExpiredException::class.java.name -> throw TokenTimeOutException()
                    else -> throw ServiceErrorException()
                }
            }
        }
    }

    override fun getErrorPath(): String {
        return "app/error"
    }
}