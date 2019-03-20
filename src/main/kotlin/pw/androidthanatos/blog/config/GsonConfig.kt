package pw.androidthanatos.blog.config

import com.fasterxml.jackson.core.JsonGenerator
import com.google.gson.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter
import java.lang.reflect.Type

/**
 * gson 配置类
 */
@Configuration
@ConditionalOnClass(Gson::class)
@ConditionalOnBean(Gson::class)
@ConditionalOnMissingClass("com.fasterxml.jackson.core.JsonGenerator")
class GsonConfig {

    @Value("\${spring.gson.date-format}")
    private lateinit var GSON_DATE_FORMAT: String

    @Bean
    @ConditionalOnMissingBean
    fun gsonHttpMessageConverter(): GsonHttpMessageConverter{
        val converter = GsonHttpMessageConverter()
        val builder = GsonBuilder()
        converter.gson = builder
                .serializeNulls()
                .setDateFormat(GSON_DATE_FORMAT)
                .create()
        return converter
    }

//    inner class springfoxJsonToGsonAdapter : JsonSerializer<Json> {
//        override fun serialize(json: Json, type: Type, jsc: JsonSerializationContext): JsonElement
//                = JsonParser().parse(json.value())
//    }









}