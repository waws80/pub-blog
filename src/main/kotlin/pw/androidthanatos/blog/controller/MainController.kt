package pw.androidthanatos.blog.controller

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartHttpServletRequest
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.CODE_PARAMS_ERROR
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.token.TokenUtil

/**
 * 主控制器
 */
@RestController
class MainController : BaseController(){



    /**
     * 欢迎 api
     */
    @GetMapping("index")
    fun index() :ResponseBean{
        return ResponseBean(data = "hello! this is a blog api service")
    }

    /**
     * 上传文件工具接口
     */
    @Login
    @PostMapping("upload")
    fun upload(req: MultipartHttpServletRequest): ResponseBean{
        val responseBean = ResponseBean()
        val map = uploadFileUtil.uploadFile(req, getUserInfoByToken()!!)
        responseBean.data = HashMap<String, Any>().apply {
            put("files", map.map { it.value.first })
        }
        return responseBean
    }
}