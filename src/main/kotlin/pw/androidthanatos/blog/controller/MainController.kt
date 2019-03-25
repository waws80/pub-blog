package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
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
     * 发送邮件
     * type: １：发送修改密码邮件
     */
    @Login
    @PostMapping("sendMail")
    fun sendMail(): ResponseBean{
        val responseBean = ResponseBean()
        val type = getParamsNotEmpty("type")
        val userEmail = getUserInfoByToken()?.email
        if (userEmail.isNullOrEmpty()){
            responseBean.code = CODE_PARAMS_ERROR
            responseBean.msg = "还未绑定邮箱"
        }else{
            when(type){
                "1" -> { sendMail()}
            }
        }
        return responseBean
    }
}