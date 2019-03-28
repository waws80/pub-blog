package pw.androidthanatos.blog.common.response

import pw.androidthanatos.blog.common.annotation.ResponseCode
import pw.androidthanatos.blog.common.annotation.ResponseMsg
import pw.androidthanatos.blog.common.contract.CODE_SERVICE_ERROR
import pw.androidthanatos.blog.common.contract.CODE_SUCCESS
import pw.androidthanatos.blog.common.contract.MSG_SERVICE_ERROR
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
){

    companion object {

        /**
         * 服务器异常响应
         */
        fun serviceError() = ResponseBean(code = CODE_SERVICE_ERROR,
                msg = MSG_SERVICE_ERROR)
    }

    /**
     * 服务器异常响应
     */
    fun buildServiceError(): ResponseBean{
        this.code = CODE_SERVICE_ERROR
        this.msg = MSG_SERVICE_ERROR
        return this
    }
}

