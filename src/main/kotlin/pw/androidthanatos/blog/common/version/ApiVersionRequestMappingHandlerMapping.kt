package pw.androidthanatos.blog.common.version

import org.springframework.web.servlet.mvc.condition.RequestCondition
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import pw.androidthanatos.blog.common.annotation.ApiVersion
import java.lang.reflect.Method

class ApiVersionRequestMappingHandlerMapping : RequestMappingHandlerMapping() {

    override fun getCustomTypeCondition(handlerType: Class<*>): RequestCondition<*>? {
        val apiVersion = handlerType.getAnnotation(ApiVersion::class.java)
        return createCondition(apiVersion)
    }

    override fun getCustomMethodCondition(method: Method): RequestCondition<*>? {
        val apiVersion = method.getAnnotation(ApiVersion::class.java)
        return createCondition(apiVersion)
    }

    private fun createCondition(apiVersion: ApiVersion?): RequestCondition<ApiVersionCondition>?{
        return if (null == apiVersion){
            null
        }else{
            ApiVersionCondition(apiVersion.value)
        }
    }
}