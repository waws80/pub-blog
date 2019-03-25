package pw.androidthanatos.blog.common.filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.contract.KEY_HEADER_REFRESH_TOKEN
import pw.androidthanatos.blog.common.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.token.TokenUtil
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 响应过滤器
 */
class ResponseFilter : Filter {

    @Autowired
    private lateinit var mTokenUtil: TokenUtil

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletResponse = response as HttpServletResponse
        val httpServletRequest = request as HttpServletRequest
        val token = httpServletRequest.getHeader(KEY_HEADER_TOKEN)
        if (token.isNullOrEmpty()){
            return
        }

        if (mTokenUtil.needRefreshToken(token)){
            httpServletResponse.addHeader(KEY_HEADER_REFRESH_TOKEN, mTokenUtil.createToken(mTokenUtil.getUserIdByToken(token)))
            httpServletResponse.headerNames
            logi(httpServletResponse.headerNames.toString())
        }
        chain?.doFilter(httpServletRequest,httpServletResponse)

    }

}