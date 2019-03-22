package pw.androidthanatos.blog.common.config

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import pw.androidthanatos.blog.common.version.ApiVersionRequestMappingHandlerMapping

/**
 * 全局注册器
 */
@Configuration
class GlobalRegister : WebMvcRegistrations {

    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return ApiVersionRequestMappingHandlerMapping()
    }
}