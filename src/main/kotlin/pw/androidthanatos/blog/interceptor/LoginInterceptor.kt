package pw.androidthanatos.blog.interceptor

import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pw.androidthanatos.blog.annotation.Login
import pw.androidthanatos.blog.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.exception.NoTokenException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 登录拦截器
 */
class LoginInterceptor : HandlerInterceptor{

    /**
     * 返回false 停止执行　true　继续执行
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod){
            val method = handler.method
            val login = method.getAnnotation(Login::class.java)
            if (null != login && login.value){ //需要登录
                val token = request.getHeader(KEY_HEADER_TOKEN)
                if (token.isNullOrEmpty()){
                    throw NoTokenException()
                }else{
                    //解析token
                }
            }
        }
        return super.preHandle(request, response, handler)
    }
}