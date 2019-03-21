package pw.androidthanatos.blog.common.config

import com.google.gson.GsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pw.androidthanatos.blog.common.interceptor.LoginInterceptor

/**
 * 拦截器配置类
 */
@Configuration
class InterceptorConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var mLoginInterceptor: LoginInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        //添加登录拦截器
        registry.addInterceptor(mLoginInterceptor)
        super.addInterceptors(registry)
    }

    /**
     * 使用gson转换器返回数据
     */
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        super.configureMessageConverters(converters)
        converters.clear()
        val gsonConfig = GsonHttpMessageConverter()
        gsonConfig.gson = GsonBuilder().serializeNulls()
                .setPrettyPrinting().create()
        converters.add(gsonConfig)
    }

    /**
     * 解决跨域问题
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600)
//        registry.addMapping("/**")
//                .allowedOrigins("http://192.168.1.97")
//                .allowedMethods("GET", "POST")
//                .allowCredentials(false).maxAge(3600);
    }
}