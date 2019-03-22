package pw.androidthanatos.blog.common.version

import org.springframework.web.servlet.mvc.condition.RequestCondition
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

/**
 * 版本控制比较类
 */
class ApiVersionCondition(private val apiVersion: Int) : RequestCondition<ApiVersionCondition> {

    companion object {

        private val VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/")
    }


    override fun getMatchingCondition(request: HttpServletRequest): ApiVersionCondition? {
        val m = VERSION_PREFIX_PATTERN.matcher(request.requestURI)
        if (m.find()){
            val version = m.group(1).toInt()
            //如果等于则必须匹配版本，如果 大于 则 大于当前版本号都可以访问(提高容错率)
            if (version == this.apiVersion){
                return this
            }
        }
        return null
    }

    override fun compareTo(other: ApiVersionCondition, request: HttpServletRequest): Int {
        return other.apiVersion - this.apiVersion
    }

    override fun combine(other: ApiVersionCondition): ApiVersionCondition {
        return ApiVersionCondition(other.apiVersion)
    }
}