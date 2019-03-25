package pw.androidthanatos.blog.controller

import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.contract.KEY_HEADER_TOKEN
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.extension.isMobileSimple
import pw.androidthanatos.blog.common.extension.isPassWord
import pw.androidthanatos.blog.common.token.TokenUtil
import pw.androidthanatos.blog.entity.UserBean
import pw.androidthanatos.blog.service.user.UserService
import javax.servlet.http.HttpServletRequest
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.TYPE_USER_ADMIN
import pw.androidthanatos.blog.common.contract.TYPE_USER_NORMAL
import pw.androidthanatos.blog.common.exception.PermissionException
import pw.androidthanatos.blog.common.extension.isUsername

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
     * 获取参数可为空
     */
    protected fun getParams(@NotNull key: String, notEmpty:()->Unit = {}): String{
        val value = request.getParameter(key)?:""
        if (value.isNotEmpty()){
            notEmpty.invoke()
        }
        return value
    }

    /**
     * 获取不为空的参数
     */
    protected fun getParamsNotEmpty(@NotNull key: String): String{
        val params = getParams(key)
        if (params.isEmpty()){
            throw ParamsErrorException()
        }
        return params
    }


    /**
     * 检查参数是否合法
     */
    protected fun checkParams(vararg params: String?){
        params.forEach {
            if (it.isNullOrEmpty()){
                throw ParamsErrorException()
            }
        }

    }

    /**
     * 检查参数是否合法
     */
    protected fun checkParamsByKey(map: Map<String, String> = emptyMap(), vararg keys: String){
        keys.forEach {
            if (map[it].isNullOrEmpty()){
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
     * 检查邮箱格式合法性
     */
    protected fun checkEmail(email: String?){
        if (email.isNullOrEmpty() || !email.isPassWord())
            throw ParamsErrorException()
    }

    /**
     * 昵称合法性校验
     * 取值范围为 a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是 6-20 位
     */
    protected fun checkNickName(nickName: String?){
        if (nickName.isNullOrEmpty() || !nickName.isUsername())
            throw ParamsErrorException()
    }


    /**
     * 检测权限
     */
    protected fun checkAdmin(){
        val user = getUserInfoByToken()
        if (user == null || user.admin != TYPE_USER_ADMIN){
            throw PermissionException()
        }
    }

    /**
     * 检测用户id
     */
    protected fun checkUserId(userId: String){
        userService.findUserByUserId(userId) ?: throw ParamsErrorException()
    }

    /**
     * 检测参数是否为空和检测特定数据是否合法
     * @param keys 参数key
     * @return [HashMap] 返回对应的key value
     */
    protected fun convertParams(vararg keys: String): HashMap<String, String>{
        val map = HashMap<String, String>()
        keys.forEach {
            val value = request.getParameter(it)?:""
            map[it] = value
        }
        return map
    }

    /**
     * 获取用户信息
     * 只有使用了[Login]注解才有数据，否则用户信息为空
     * 由于登录拦截器已经校验了token的合法性，所以用户信息不可能为空，
     */
    protected fun getUserInfoByToken(): UserBean?{
        val  token = request.getHeader(KEY_HEADER_TOKEN)
        return userService.findUserByUserId(tokenUtil.getUserIdByToken(token))
    }
}