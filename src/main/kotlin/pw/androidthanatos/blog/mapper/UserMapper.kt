package pw.androidthanatos.blog.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.annotation.UserStatus
import pw.androidthanatos.blog.entity.UserBean
import pw.androidthanatos.blog.common.contract.STATUS_USER_NORMAL
import pw.androidthanatos.blog.common.contract.STATUS_USER_BLACK

/**
 * 用户信息查询
 */
@Component
interface UserMapper {

    /**
     * 通过用户id获取用户信息
     */
    @Select("SELECT * FROM tb_user WHERE userId = #{userId}")
    fun findUserById(userId: String): UserBean?

    /**
     * 通过用户手机号获取用户信息
     */
    @Select("SELECT * FROM tb_user WHERE phone = #{phone}")
    fun findUserByPhone(phone: String): UserBean?

    /**
     * 获取所有用户信息
     */
    @Select("SELECT * FROM tb_user ")
    fun findAllUser(): List<UserBean> = emptyList()

    /**
     * 添加用户
     */
    @Insert("INSERT INTO tb_user(userId, nickName, phone, pass, portrait, signature," +
            " extension, black, email, createDate, admin)" +
            "VALUES(#{userId}, #{nickName}, #{phone}, #{pass}, #{portrait}, #{signature}, #{extension}," +
            "#{black}, #{email}, #{createDate}, #{admin})")
    fun addUser(userBean: UserBean): Int

    /**
     * 更新用户信息
     */
    @Update("UPDATE tb_user SET nickName = #{nickName}, phone = #{phone}, pass = #{pass}, " +
            "portrait = #{portrait}, signature = #{signature}, extension = #{extension}, email = #{email} " +
            "WHERE userId = #{userId}")
    fun updateUserById(userBean: UserBean): Int

    /**
     * 更新用户密码通过邮箱
     */
    @Update("UPDATE tb_user SET pass = #{pass} WHERE email = #{email}")
    fun updatePassByEmail(email: String, pass: String): Int

    /**
     * 更新用户密码通过用户id
     */
    @Update("UPDATE tb_user SET pass = #{pass} WHERE userId = #{userId}")
    fun updatePassByUserId(userId: String, pass: String): Int

    /**
     * 更新用户状态
     * [status]  [STATUS_USER_NORMAL] 正常用户　[STATUS_USER_BLACK]　拉黑用户
     */
    @Update("UPDATE tb_user SET black = #{black} WHERE userId = #{userId}")
    fun updateUserStatusById(userId: String, @Param("black")@UserStatus status: Int): Int
}