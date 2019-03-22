package pw.androidthanatos.blog.common.response

import pw.androidthanatos.blog.common.annotation.ResponseCode
import pw.androidthanatos.blog.common.annotation.ResponseMsg
import pw.androidthanatos.blog.common.contract.CODE_SUCCESS
import pw.androidthanatos.blog.common.contract.MSG_SUCCESS

/**
 * 全局相应实体类
 */
data class ResponseBean (@ResponseCode
                         var code: Int = CODE_SUCCESS, //响应码
                         @ResponseMsg
                         var msg: String = MSG_SUCCESS, //相应信息
                         var data: Any? = null, //相应数据
                         val timestamp: Long = System.currentTimeMillis() //当前时间戳
)

