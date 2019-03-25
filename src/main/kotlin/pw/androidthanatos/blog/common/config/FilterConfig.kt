package pw.androidthanatos.blog.common.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pw.androidthanatos.blog.common.filter.ResponseFilter

/**
 * 过滤器配置
 */
@Configuration
class FilterConfig {

    @Bean
    fun registerFilter(): FilterRegistrationBean<ResponseFilter>{
        val registrationBean = FilterRegistrationBean<ResponseFilter>()
        registrationBean.filter = getResponseFilter()
        registrationBean.addUrlPatterns("/*")
        return registrationBean
    }

    @Bean
    fun getResponseFilter() = ResponseFilter()
}