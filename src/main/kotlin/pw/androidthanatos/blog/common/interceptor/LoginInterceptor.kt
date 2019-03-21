package pw.androidthanatos.blog.common.interceptor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.KEY_HEADER_REFRESH_TOKEN
import pw.androidthanatos.blog.common.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.common.exception.NoTokenException
import pw.androidthanatos.blog.common.exception.TokenErrorException
import pw.androidthanatos.blog.common.exception.TokenTimeOutException
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.token.TokenUtil
import pw.androidthanatos.blog.common.util.log
import pw.androidthanatos.blog.mapper.UserMapper
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 登录拦截器
 */
@Component
class LoginInterceptor : HandlerInterceptor{

    @Autowired
    lateinit var mTokenUtil: TokenUtil

    /**
     * 返回false 停止执行　true　继续执行
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        getRequestToken(request, handler){ token->
            if (token.isEmpty()){
                throw NoTokenException()
            }else{
                //解析token
                if (mTokenUtil.isTokenValidity(token)){
                    if (mTokenUtil.isTokenExpires(token)){
                        //token过期
                        throw TokenTimeOutException()
                    }
                }else{
                    //不合法
                    throw TokenErrorException()
                }
            }
        }
        return super.preHandle(request, response, handler)
    }

    /**
     * 刷新的token会在响应头中返回　key: refresh_token
     */
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
        //token 刷新操作
        getRequestToken(request, handler){
            if (mTokenUtil.needRefreshToken(it)){
                val userId = mTokenUtil.getUserIdByToken(it)
                //刷新后的token,开发者需将其保存起来，以便下次网络请求使用
                if (userId.isNotEmpty()){
                    //有效的用户id
                    response.addHeader(KEY_HEADER_REFRESH_TOKEN, mTokenUtil.createToken(userId))
                }
            }
        }
    }

    /**
     * 回调获取token
     */
    private fun getRequestToken(request: HttpServletRequest, handler: Any, next:(String)->Unit){
        if(handler is HandlerMethod){
            val method = handler.method
            val login = method.getAnnotation(Login::class.java)
            if (null != login && login.value){ //需要登录
                val token = request.getHeader(KEY_HEADER_TOKEN)?:""
                next.invoke(token)
            }
        }
    }

}