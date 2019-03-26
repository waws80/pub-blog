package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartHttpServletRequest
import pw.androidthanatos.blog.common.annotation.ApiVersion
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.extension.*
import pw.androidthanatos.blog.common.mail.SendMail
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.upload.FileType
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.UserBean
import kotlin.collections.HashMap

/**
 * 用户控制器
 */
@RestController
@ApiVersion(1)
@RequestMapping("/user/{version}")
class UserController : BaseController(){

    @Autowired
    private lateinit var sendMail: SendMail

    /**
     * 用户接口　index
     */
    @GetMapping("index")
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
            checkUserStatus(userBean)
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
    fun update(req: MultipartHttpServletRequest): ResponseBean{
        val responseBean = ResponseBean()
        //检测以及获取参数
        val paramsMap = convertParamsToMap("nickName", "phone", "signature",
                "extension", "email")
        //获取用户信息校验并回填
        getUserInfoByToken()?.apply {
            paramsMap.safeNotEmptyGet("nickName"){
                if (it.isUsername())
                    this.nickName = it
                else
                    throw ParamsErrorException()

            }
            paramsMap.safeNotEmptyGet("phone"){
                if (it.isMobileSimple())
                    this.phone = it
                else
                    throw ParamsErrorException()
            }
            paramsMap.safeNotEmptyGet("signature"){
                this.signature = it
            }
            paramsMap.safeNotEmptyGet("extension"){
                this.extension = it
            }
            paramsMap.safeNotEmptyGet("email"){
                if (it.isEmail())
                    this.email = it
                else
                    throw ParamsErrorException()
            }
            //获取头像
            val portraitMap = uploadFileUtil.uploadFile(req, this)
            val portraitPair = portraitMap["portrait"]
            if (portraitPair != null){
                if (portraitPair.second == FileType.IMG){
                    this.portrait = portraitPair.first
                }else{
                    throw ParamsErrorException()
                }

            }
            //开始更新用户信息
            (userService.updateUserById(this)).no {
                //更新失败
                responseBean.code = CODE_SERVICE_ERROR
                responseBean.msg = MSG_SERVICE_ERROR
            }.yes {
                //更新成功
                responseBean.data = buildUserMapWithToken(this.apply {
                    userService.decode(this)
                })
            }
        }
        return responseBean
    }


    /**
     * 管理员获取所有用户信息
     */
    @Login
    @GetMapping("list")
    fun getAllUser():ResponseBean{
        val responseBean = ResponseBean()
        checkAdmin()
        //返回所有用户信息
        val userList = userService.findAllUser()
        responseBean.data = userList
        return responseBean
    }

    /**
     * 管理员更新用户状态
     */
    @Login
    @PutMapping("updateUserStatus")
    fun updateUserStatus():ResponseBean{
        val responseBean = ResponseBean()
        val otherId = getParamsNotEmpty("otherId")
        val status = getParamsNotEmpty("status")
        //检测是否是管理员更新用户信息
        checkAdmin()
        //检测otherId
        checkUserId(otherId)
        //管理员不能更新管理员状态
        if (userService.findUserByUserId(otherId)?.admin == TYPE_USER_ADMIN){
            throw ParamsErrorException()
        }
        //检测修改的用户状态参数
        if (status != STATUS_USER_BLACK.toString() && status != STATUS_USER_NORMAL.toString()){
            throw ParamsErrorException()
        }
        val updateStatus = userService.updateUserStatusById(otherId, status.toInt())
        if (updateStatus){
            responseBean.data = HashMap<String, Any>().apply {
                put("newStatus", status)
            }
        }else{
            responseBean.code = CODE_SERVICE_ERROR
            responseBean.msg = MSG_SERVICE_ERROR
        }
        return responseBean
    }

    /**
     * 邮箱重置密码发邮件和token修改密码
     */
    @Login
    @RequestMapping(method = [RequestMethod.POST, RequestMethod.PUT],value = ["reSetPass"])
    fun reSetPass(): ResponseBean{
        val responseBean = ResponseBean()
        val user = getUserInfoByToken()
        //发送邮箱重置密码
        if (request.method == RequestMethod.POST.name){

            if (user?.email.isNullOrEmpty()){
                responseBean.code = CODE_PARAMS_ERROR
                responseBean.msg = "还未绑定邮箱"
            }else{
                sendMail.sendResetPassMail(user?.email!!, request.getHeader("token"))
            }
        }else{
            val token = request.getHeader(KEY_HEADER_TOKEN)
            val type = request.getHeader("type")
            //浏览器更新
            if (type == "1"){
                if (SendMail.linkExpires(token) ){
                    SendMail.remove(token)
                    val pass = getParamsNotEmpty("pass")
                    checkPass(pass)
                    val update = userService.updatePassByUserId(pass, user?.userId!!)
                    if (!update){
                        responseBean.code = CODE_SERVICE_ERROR
                        responseBean.msg = MSG_SERVICE_ERROR
                    }
                }else{
                    responseBean.code = CODE_SERVICE_ERROR
                    responseBean.msg = MSG_LINK_EXPIRES
                }
            }else{//客户端更新
                val pass = getParamsNotEmpty("pass")
                checkPass(pass)
                val update = userService.updatePassByUserId(pass, user?.userId!!)
                if (!update){
                    responseBean.code = CODE_SERVICE_ERROR
                    responseBean.msg = MSG_SERVICE_ERROR
                }
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