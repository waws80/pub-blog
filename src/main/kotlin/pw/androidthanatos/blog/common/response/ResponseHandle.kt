package pw.androidthanatos.blog.common.response

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
    fun getParams(request: REQ, vararg keys: String): HashMap<String, String>{
        val map = HashMap<String, String>()
        keys.forEach {
            val value = request.getParameter(it)?:""
            map[it] = value
        }
        return map
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


    fun notEmptyUse(value: String?, next:(String)->Unit){
        if (value != null && value.isNotEmpty()){
            next.invoke(value)
        }
    }



}