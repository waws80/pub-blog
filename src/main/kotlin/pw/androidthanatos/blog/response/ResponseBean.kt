package pw.androidthanatos.blog.response

import pw.androidthanatos.blog.annotation.ResponseCode
import pw.androidthanatos.blog.annotation.ResponseMsg

/**
 * 全局相应实体类
 */
data class ResponseBean (@ResponseCode
                         val code: Int, //响应码
                         @ResponseMsg
                         val msg: String, //相应信息
                         val data: Any? = null //相应数据
)

