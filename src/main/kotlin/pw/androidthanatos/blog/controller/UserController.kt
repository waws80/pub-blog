package pw.androidthanatos.blog.controller

import org.springframework.web.bind.annotation.*
import pw.androidthanatos.blog.common.annotation.ApiVersion
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.extension.*
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.UserBean
import java.util.*

@RestController
@ApiVersion(1)
@RequestMapping("/user/{version}")
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
        //检测注册状态
        checkPhoneRegister(register = {
            _,_->
            responseBean.code = CODE_PHONE_HAS_REGISTER
            responseBean.msg = MSG_PHONE_HAS_REGISTER
        }, unRegister = {
            phone, pass ->
            //开始注册
            val userBean = UserBean(userId = createId(),
                    phone = phone, pass = pass)
            userService.addUser(userBean).no {
                //注册失败
                responseBean.code = CODE_SERVICE_ERROR
                responseBean.msg = MSG_SERVICE_ERROR
            }.yes {
                //注册成功，将token返回
                responseBean.data = buildUserMapWithToken(userBean.apply {
                    //将原始手机号返回
                    this.phone = phone
                })
            }
        })
        return responseBean
    }

    /**
     * 登录功能
     * 使用表单提交
     */
    @PostMapping("login")
    fun login(): ResponseBean{
        val responseBean = ResponseBean()
        //检测注册状态
        checkPhoneRegister(register = {
            pass, userBean->
            //当前手机号已注册
            if (pass.toMd5() == userBean.pass){
                //登录成功
                responseBean.data = buildUserMapWithToken(userBean)
            }else{
                //登录失败
                responseBean.code = CODE_LOGIN_PARAMS_ERROR
                responseBean.msg = MSG_LOGIN_PARAMS_ERROR
            }

        }, unRegister = {
            _, _ ->
            //当前手机号没有注册
            responseBean.code = CODE_PHONE_NOT_REGISTER
            responseBean.msg = MSG_PHONE_NOT_REGISTER
        })
        return responseBean
    }

    /**
     * 更新用户信息 密码和用户状态通过单独的接口更新
     * 表单提交数据
     */
    @Login
    @PutMapping("update")
    fun update(): ResponseBean{
        val responseBean = ResponseBean()
        //检测以及获取参数
        val paramsMap = convertParams("nickName", "phone", "portrait", "signature",
                "extension", "email")
        //获取用户信息并回填
        getUserInfoByToken()?.apply {
            paramsMap.safeGet("nickName"){
                if (it.isUsername())
                    this.nickName = it
            }
            paramsMap.safeGet("phone"){
                if (it.isMobileSimple())
                    this.phone = it
            }
            paramsMap.safeGet("portrait"){
                this.portrait = it
            }
            paramsMap.safeGet("signature"){
                this.signature = it
            }
            paramsMap.safeGet("extension"){
                this.extension = it
            }
            paramsMap.safeGet("email"){
                if (it.isEmail())
                    this.email = it
            }
            //开始更新用户信息
            val u = this
            (userService.updateUserById(u)).no {
                //更新失败
                responseBean.code = CODE_SERVICE_ERROR
                responseBean.msg = MSG_SERVICE_ERROR
            }.yes {
                //更新成功
                responseBean.data = buildUserMapWithToken(this)
            }
        }
        return responseBean
    }


    /**
     * 检测手机号注册状态
     * @param register 注册回调
     * @param unRegister 没有注册回调
     */
    private fun checkPhoneRegister(register:(String, UserBean)->Unit, unRegister:(String, String)->Unit){

        //获取用户基本信息
        val phone = request.getParameter("phone")
        val pass = request.getParameter("pass")
        //检查字段信息
        checkParams(phone, pass)
        checkPhone(phone)
        checkPass(pass)
        //判断用户是否注册过
        val getUser = userService.findUserByPhone(phone)
        (getUser?.phone == phone).yes {
            //当前手机号已注册
            register.invoke(pass, getUser!!)
        }.no {
            //当前手机号没有注册
            unRegister.invoke(phone, pass)
        }
    }

    /**
     * 构建一个承载token的map
     */
    private fun buildUserMapWithToken(userBean: UserBean): HashMap<String, Any>{
        userBean.apply {
            pass = ""
        }
        val token = tokenUtil.createToken(userBean.userId)
        return  hashMapOf<String, Any>().apply {
            put(KEY_HEADER_TOKEN, token)
            put("userInfo", userBean)
        }
    }
}