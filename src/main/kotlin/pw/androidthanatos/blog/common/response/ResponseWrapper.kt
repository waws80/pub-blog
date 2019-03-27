package pw.androidthanatos.blog.common.response

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.springframework.web.multipart.MultipartHttpServletRequest
import javax.servlet.http.HttpServletRequest

/**
 * 响应体包裹类
 */
class ResponseWrapper<REQ: HttpServletRequest>(private val request: REQ, private val handle: ResponseHandle<REQ>) : ResponseTemplate{

    private val paramsMap by lazy { HashMap<String, String>() }

    private val tags by lazy { HashMap<String, Any>() }

    override fun process(): ResponseBean {
        val responseBean = ResponseBean()
        try {
            paramsMap.clear()
            //解析所有参数
            handle.preHandleWithParams(request, paramsMap, tags)
            if (request is MultipartHttpServletRequest || ServletFileUpload.isMultipartContent(request) || handle.hasUploadFile()){
                val multiPartRequest = request as MultipartHttpServletRequest
                handle.preHandleWithUploadFile(multiPartRequest, paramsMap)
            }
            //执行业务逻辑
            handle.handle(paramsMap, tags, responseBean)
        }catch (e: Exception){
            handle.handleException(e)
        }
        return responseBean
    }

}