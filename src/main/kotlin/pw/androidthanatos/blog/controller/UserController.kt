package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.service.user.UserService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    private lateinit var mRequest: HttpServletRequest

    @Autowired
    private lateinit var mUserService: UserService

    /**
     * 用户接口　index
     */
    @GetMapping
    fun index() = ResponseBean(data = "欢迎访问用户接口")

    /**
     * 注册用户信息
     */
    @PostMapping("register")
    fun register(): ResponseBean{
        val responseBean = ResponseBean()


        return responseBean
    }
}