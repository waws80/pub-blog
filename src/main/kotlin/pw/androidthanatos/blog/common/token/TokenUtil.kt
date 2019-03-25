package pw.androidthanatos.blog.common.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.service.user.UserService
import java.util.*
import kotlin.collections.HashMap

/**
 * token工具类
 */
@Component
class TokenUtil {

    companion object {

        //token秘钥
        private const val SECRET = "pw.androidthanatos.blog:secret-token"

        //token 过期时间 1天
        private const val CalendarField = Calendar.DATE
        private const val CalendarInterval = 1

        private const val KEY_USER_ID = "userId"

        private const val ISS_VALUE = "Pub-Blog"

    }

    @Autowired
    private lateinit var mUserService: UserService


    /**
     * [userId] 用户id
     */
    fun createToken(userId: String): String{
        if (userId.isEmpty()) return ""
        val iatdate = Date()
        val now = Calendar.getInstance()
        now.add(CalendarField, CalendarInterval)
        val expiresDate = now.time
        val map = HashMap<String, Any>()
        map["alg"] = "HS256"
        map["typ"] = "JWT"

        return JWT.create()
                .withHeader(map)// header
                .withClaim("iss",ISS_VALUE)//payload
                .withClaim("aud", "APP")
                .withClaim(KEY_USER_ID, userId)
                .withIssuedAt(iatdate)//签发时间
                .withExpiresAt(expiresDate)//过期时间
                .withNotBefore(iatdate)
                .sign(Algorithm.HMAC256(SECRET))
    }

    /**
     * 通过token获取userId
     */
    fun getUserIdByToken(token: String?): String{
        if (token.isNullOrEmpty() || !isTokenValidity(token) || isTokenExpires(token)) return ""
        return try {
            getJWTVerify(token).claims[KEY_USER_ID]?.asString()?: ""
        }catch (e: Exception){
            ""
        }
    }

    private fun getJWTVerify(token: String): DecodedJWT{
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token)
    }

    /**
     * token是否已过期
     */
    fun isTokenExpires(token: String?):Boolean{
        if (token.isNullOrEmpty()) return true
        return  getJWTVerify(token).expiresAt.time  < System.currentTimeMillis()
    }


    /**
     * token 是否合法
     */
    fun isTokenValidity(token: String?): Boolean{
        if (token.isNullOrEmpty()) return false
        val map  = getJWTVerify(token).claims
        //是否是当前平台签发
        val iss = map["iss"]?.asString()
        if (iss != ISS_VALUE) return false
        //用户id是否合法
        val tokenInnerUserId =map[KEY_USER_ID]?.asString()
        logi("token中携带的用户id：$tokenInnerUserId")
        if (tokenInnerUserId.isNullOrEmpty()) return false
        val userBean = mUserService.findUserByUserId(tokenInnerUserId)
        return null != userBean
    }

    /**
     * 是否需要刷新token
     */
    fun needRefreshToken(token: String?): Boolean{
        if (isTokenValidity(token) && !isTokenExpires(token)){
            val expiresAt = getJWTVerify(token!!).expiresAt.time
            if (System.currentTimeMillis() - expiresAt < 60 * 60 * 48){
                //如果token一天内将会过期则需要刷新
                return true
            }
        }
        return false
    }
}