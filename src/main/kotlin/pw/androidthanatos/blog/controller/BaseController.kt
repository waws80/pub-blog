package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.extension.isMobileSimple
import pw.androidthanatos.blog.common.extension.isPassWord
import pw.androidthanatos.blog.common.extension.no
import pw.androidthanatos.blog.common.token.TokenUtil
import pw.androidthanatos.blog.common.util.RegexUtil
import pw.androidthanatos.blog.entity.UserBean
import pw.androidthanatos.blog.service.user.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 控制器基类
 */
@Component
abstract class BaseController {

    @Autowired
    protected lateinit var request: HttpServletRequest

    @Autowired
    protected lateinit var userService: UserService

    @Autowired
    protected lateinit var tokenUtil: TokenUtil

    /**
     * 检查参数是否合法
     */
    protected fun checkParams(vararg params: Any?){
        params.forEach {
            if (null == it){
                if (it is String){
                    if ((it as String).isNotEmpty()){
                        return
                    }
                }
                throw ParamsErrorException()
            }
        }

    }

    /**
     * 检查手机号合法性
     */
    protected fun checkPhone(phone: String?){
        if (phone.isNullOrEmpty() || !phone.isMobileSimple())
            throw ParamsErrorException()
    }

    /**
     * 检查密码合法性 6- 18位
     */
    protected fun checkPass(pass: String?){
        if (pass.isNullOrEmpty() || !pass.isPassWord())
            throw ParamsErrorException()
    }

    /**
     * 获取用户信息
     */
    protected fun getUserInfoByToken(): UserBean?{
        val  token = request.getHeader(KEY_HEADER_TOKEN)
        return userService.findUserByUserId(tokenUtil.getUserIdByToken(token))
    }
}