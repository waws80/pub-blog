package pw.androidthanatos.blog.service.user

import pw.androidthanatos.blog.common.annotation.UserStatus
import pw.androidthanatos.blog.entity.UserBean

/**
 * 用户信息service
 */
interface UserService {

    /**
     * 通过用户id获取用户信息
     * @param userId 用户id
     * @return [UserBean] null: 没有找到
     */
    fun findUserByUserId(userId: String): UserBean?

    /**
     * 通过手机号获取用户信息
     * @param phone 手机号
     * @return [UserBean] null: 没有找到
     */
    fun findUserByPhone(phone: String): UserBean?

    /**
     * 获取所有用户信息
     */
    fun findAllUser(): List<UserBean>

    /**
     * 添加用户
     * @param userBean 用户信息实体类
     * @return [Boolean] true: 添加用户成功 false:　添加用户失败
     */
    fun addUser(userBean: UserBean): Boolean


    /**
     * 通过用户id更新相关信息 不能更新密码和状态
     * @param userBean 用户信息实体类
     * @return [Boolean] true: 成功 false:　失败
     */
    fun updateUserById(userBean: UserBean): Boolean

    /**
     * 更新用户密码通过邮箱
     * @param pass 修改的密码
     * @param email　邮箱
     * @return [Boolean] true: 成功 false:　失败
     */
    fun updatePassByEmail(pass: String, email: String): Boolean

    /**
     * 更新用户状态通过id
     * @param userId 用户id
     * @param status 要更改的用户状态
     * @return [Boolean] true: 成功 false:　失败
     */
    fun updateUserStatusById(userId: String, @UserStatus status: Int): Boolean

    /**
     * 更新用户密码通过用户id
     * @param pass 要更新的密码
     * @param userId 用户id
     * @return [Boolean] true: 成功 false:　失败
     */
    fun updatePassByUserId(pass: String, userId: String): Boolean
}