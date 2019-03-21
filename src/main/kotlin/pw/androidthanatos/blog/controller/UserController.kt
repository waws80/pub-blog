package pw.androidthanatos.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.common.contract.CODE_PHONE_HAS_REGISTER
import pw.androidthanatos.blog.common.contract.CODE_SERVICE_ERROR
import pw.androidthanatos.blog.common.contract.MSG_PHONE_HAS_REGISTER
import pw.androidthanatos.blog.common.contract.MSG_SERVICE_ERROR
import pw.androidthanatos.blog.common.extension.no
import pw.androidthanatos.blog.common.extension.yes
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.UserBean

@RestController
@RequestMapping("user")
class UserController : BaseController(){

    /**
     * 用户接口　index
     */
    @GetMapping
    fun index() = ResponseBean(data = "欢迎访问用户接口")

    /**
     * 注册用户信息
     * 使用表单提交
     */
    @PostMapping("register")
    fun register(): ResponseBean{
        val responseBean = ResponseBean()
        //获取用户注册信息
        val phone = request.getParameter("phone")
        val pass = request.getParameter("pass")
        checkParams(phone, pass)
        checkPhone(phone)
        checkPass(pass)
        val getUserByPhone = userService.findUserByPhone(phone)?.phone
        (getUserByPhone == phone).yes {
            //当前手机号已注册
            responseBean.code = CODE_PHONE_HAS_REGISTER
            responseBean.msg = MSG_PHONE_HAS_REGISTER
        }.no {
            //开始执行注册操作
            val userBean = UserBean(userId = createId(),
                    phone = phone, pass = pass)
            userService.addUser(userBean).no {
                //注册失败
                responseBean.code = CODE_SERVICE_ERROR
                responseBean.msg = MSG_SERVICE_ERROR
            }.yes {
                responseBean.data = tokenUtil.createToken(userBean.userId)
            }
        }
        return responseBean
    }
}