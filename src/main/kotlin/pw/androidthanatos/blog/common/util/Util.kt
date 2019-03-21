package pw.androidthanatos.blog.common.util

import org.springframework.util.Base64Utils
import pw.androidthanatos.blog.common.contract.KEY_EXTENSION_BASE64
import pw.androidthanatos.blog.common.contract.KEY_VALUE_BASE64
import pw.androidthanatos.blog.common.contract.STR_EXTENSION_BASE64
import pw.androidthanatos.blog.common.contract.STR_EXTENSION_MD5
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.logging.Logger

/**
 * 工具类
 */

val log = Logger.getLogger("个人博客公开接口项目")

/**
 * 字符串　md5加密
 */
fun md5(text: String): String{
    if (text.isEmpty()) return ""
    val convert = text + STR_EXTENSION_MD5
    try {
        val instance:MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest:ByteArray = instance.digest(convert.toByteArray())//对字符串加密，返回字节数组
        val sb  = StringBuffer()
        for (b in digest) {
            val i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0$hexString"//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }
}

/**
 * base64加密
 */
fun base64Encode(text: String): String {
    if (text.isEmpty()) return ""
    val convert = JSONObject().putString(KEY_EXTENSION_BASE64, STR_EXTENSION_BASE64)
            .putString(KEY_VALUE_BASE64, text).toString()
    log.info("base64加密的信息：　$convert")
    return Base64Utils.encodeToString(convert.toByteArray(Charset.forName("UTF-8")))
}

/**
 * base64解密
 */
fun base64Decode(text: String, sysEncode: Boolean = true): String{
    if (text.isEmpty()) return ""
    val src = String(Base64Utils.decodeFromString(text), Charset.forName("UTF-8"))
    return if (sysEncode){
        val jsonObject = JSONObject(src)
        jsonObject.getString(KEY_VALUE_BASE64)
    }else{
        src
    }
}

/**
 * 生成id
 */
fun createId(): String {
    return UUID.randomUUID().toString().replace("-","")
}