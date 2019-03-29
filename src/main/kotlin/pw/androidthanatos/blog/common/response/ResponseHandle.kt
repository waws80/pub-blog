package pw.androidthanatos.blog.common.response

import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartHttpServletRequest
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.upload.UploadFileUtil
import pw.androidthanatos.blog.entity.UserBean
import javax.servlet.http.HttpServletRequest

/**
 * 业务执行模板
 */
interface ResponseHandle<REQ: HttpServletRequest> {

    /**
     * 执行业务前解析参数
     */
    fun preHandleWithParams(request: REQ, params: HashMap<String, String>, tags: HashMap<String, Any>)

    /**
     * 执行业务前解析上传的文件
     */
    fun preHandleWithUploadFile(request: MultipartHttpServletRequest, params: HashMap<String, String>){
        getCurrentHandleUserBean()?.apply {
            val uploadFileUtil = UploadFileUtil()
            uploadFileUtil.uploadFile(request,this).forEach {
                params[it.key] = it.value.first
            }
        }

    }

    /**
     * 执行业务逻辑
     * @param params 客户端穿过来的所有有效的参数（包括上传的文件　key: 名称    value: 文件保存后的名称）
     */
    fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean)


    /**
     * 抛出异常
     */
    fun handleException(e: Exception){
        throw e
    }


    /**
     * 获取当前登录用户信息
     */
    fun getCurrentHandleUserBean(): UserBean? {
        return null
    }


    /**
     * 是否有上传文件
     */
    fun hasUploadFile(): Boolean = false


    /**
     * 获取参数转换成map
     */
    fun getParamsMap(request: REQ, vararg keys: String): HashMap<String, String>{
        val map = HashMap<String, String>()
        keys.forEach {
            val value = request.getParameter(it)?:""
            map[it] = value
        }
        return map
    }

    /**
     * 获取不为空的参数
     */
    fun getParamsNotEmpty(request: REQ, @NotNull key: String): String{
        val params = request.getParameter(key)
        if (params.isEmpty()){
            throw ParamsErrorException()
        }
        return params
    }

    /**
     * 判空
     */
    fun checkEmpty(map: HashMap<String, String>, vararg keys: String){
        keys.forEach {
            if (map[it].isNullOrEmpty()){
                throw ParamsErrorException()
            }
        }
    }


    /**
     * 非空使用
     */
    fun notEmptyUse(value: String?, next:(String)->Unit){
        if (value != null && value.isNotEmpty()){
            next.invoke(value)
        }
    }

    /**
     * 判断字符串是否是int
     */
    fun checkInt(value: String?){
        if (value.isNullOrEmpty()){
            throw ParamsErrorException()
        }else{
            try {
                value.toInt()
            }catch (e: Exception){
                throw ParamsErrorException()
            }
        }
    }

    /**
     * 判断字符串是否是long
     */
    fun checkLong(value: String?){
        if (value.isNullOrEmpty()){
            throw ParamsErrorException()
        }else{
            try {
                value.toLong()
            }catch (e: Exception){
                throw ParamsErrorException()
            }
        }
    }

    /**
     * 检查参数是否为空
     */
    fun checkParamsEmpty(vararg values: String?){
        values.forEach {
            if (it.isNullOrEmpty()){
                throw ParamsErrorException()
            }
        }
    }

    /**
     * 字符串转化Int
     */
    fun convertInt(value: String?): Int?{
        return if (value.isNullOrEmpty()) null
        else value.toIntOrNull()
    }

    /**
     * 字符串转化Long
     */
    fun convertLong(value: String?): Long?{
        return if (value.isNullOrEmpty()) null
        else value.toLongOrNull()
    }


    fun convertBool(value: String?): Boolean{
        return if (value.isNullOrEmpty()){
            throw ParamsErrorException()
        }else{
            value.toBoolean()
        }
    }




}