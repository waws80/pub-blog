package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.token.TokenUtil

/**
 * 主控制器
 */
@RestController
class MainController {

    @Autowired
    private lateinit var mTokenUtil: TokenUtil

    /**
     * 欢迎 api
     */
    @GetMapping
    @Login
    fun index() :ResponseBean{
        logi(mTokenUtil.createToken("hahaahaha"))
        return ResponseBean(data = "hello! this is a blog api service")
    }
}