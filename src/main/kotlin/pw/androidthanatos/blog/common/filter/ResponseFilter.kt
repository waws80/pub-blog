package pw.androidthanatos.blog.common.filter

import com.google.gson.Gson
import org.apache.catalina.filters.AddDefaultCharsetFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.contract.KEY_HEADER_REFRESH_TOKEN
import pw.androidthanatos.blog.common.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.common.exception.TokenTimeOutException
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.token.TokenUtil
import java.nio.charset.Charset
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 响应过滤器
 */
@Component
class ResponseFilter : Filter {

    @Autowired
    private lateinit var mTokenUtil: TokenUtil

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletResponse = response as HttpServletResponse
        val httpServletRequest = request as HttpServletRequest
        val token = httpServletRequest.getHeader(KEY_HEADER_TOKEN)
        try {
            if (!token.isNullOrEmpty()){
                if (mTokenUtil.needRefreshToken(token)){
                    httpServletResponse.addHeader(KEY_HEADER_REFRESH_TOKEN, mTokenUtil.createToken(mTokenUtil.getUserIdByToken(token)))
                }
            }
        }catch (e: Exception){
            logi("token 有效性在进入拦截器中已经校验,此处可能抛出异常代表当前接口不需要登录权限，故不做处理。" +
                    "当token有效并且需要更换的时候自动会追加新tokenwangle " +
                    "")
        }
        chain?.doFilter(httpServletRequest,httpServletResponse)
    }

}