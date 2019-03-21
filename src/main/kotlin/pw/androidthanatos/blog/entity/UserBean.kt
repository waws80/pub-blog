package pw.androidthanatos.blog.entity

import pw.androidthanatos.blog.common.annotation.UserStatus
import pw.androidthanatos.blog.common.annotation.UserType
import pw.androidthanatos.blog.common.contract.STATUS_USER_NORMAL
import pw.androidthanatos.blog.common.contract.TYPE_USER_NORMAL
import java.util.*

/**
 * 用户信息实体类
 */
data class UserBean(var userId: String,//用户唯一id
                    var nickName: String,//用户昵称
                    var phone: String,//用户手机号
                    var pass: String,//用户密码
                    var portrait: String,//用户头像路径
                    var signature: String,//用户签名
                    var extension: String,//用户扩展信息
                    @UserStatus
                    var black: Int = STATUS_USER_NORMAL,//用户状态
                    var email: String,//用户邮箱
                    var createDate: Date,//用户注册时间
                    @UserType
                    var admin: Int = TYPE_USER_NORMAL //用户类型
)
