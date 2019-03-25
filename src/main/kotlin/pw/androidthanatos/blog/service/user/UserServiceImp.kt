package pw.androidthanatos.blog.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pw.androidthanatos.blog.common.annotation.UserStatus
import pw.androidthanatos.blog.common.extension.decodeByBase64
import pw.androidthanatos.blog.common.extension.toBase64Encode
import pw.androidthanatos.blog.common.extension.toMd5
import pw.androidthanatos.blog.entity.UserBean
import pw.androidthanatos.blog.mapper.UserMapper

/**
 * 用户service
 */
@Service
class UserServiceImp : UserService{

    @Autowired
    private lateinit var mUserMapper: UserMapper

    override fun findUserByUserId(userId: String): UserBean? {
        return mUserMapper.findUserById(userId)?.apply {
            //解密信息
            this.nickName = this.nickName.decodeByBase64()
            this.phone = this.phone.decodeByBase64()
            this.email = this.email.decodeByBase64()
        }
    }

    override fun findUserByPhone(phone: String): UserBean? {
        return mUserMapper.findUserByPhone(phone.toBase64Encode())?.apply {
            //解密信息
            this.nickName = this.nickName.decodeByBase64()
            this.phone = this.phone.decodeByBase64()
            this.email = this.email.decodeByBase64()
        }
    }

    override fun findAllUser(): List<UserBean> {
        return mUserMapper.findAllUser().map {
            it.apply {
                //解密信息
                this.nickName = this.nickName.decodeByBase64()
                this.phone = this.phone.decodeByBase64()
                this.email = this.email.decodeByBase64()
            }
        }
    }

    override fun addUser(userBean: UserBean): Boolean {
        return mUserMapper.addUser(userBean.apply {
            //加密信息
            this.nickName = this.nickName.toBase64Encode()
            this.phone = this.phone.toBase64Encode()
            this.pass = this.pass.toMd5()
            this.email = this.email.toBase64Encode()
        }) > 0
    }

    override fun updateUserById(userBean: UserBean): Boolean {
        return mUserMapper.updateUserById(userBean.apply {
            //加密信息
            this.nickName = this.nickName.toBase64Encode()
            this.phone = this.phone.toBase64Encode()
            this.email = this.email.toBase64Encode()
        }) > 0
    }

    override fun updatePassByEmail(pass: String, email: String): Boolean {
        //加密信息后更新
        return mUserMapper.updatePassByEmail(email.toBase64Encode(), pass.toMd5()) > 0
    }

    override fun updateUserStatusById(userId: String, @UserStatus status: Int): Boolean {
        return mUserMapper.updateUserStatusById(userId, status) > 0
    }

    override fun updatePassByUserId(pass: String, userId: String): Boolean {
        return mUserMapper.updatePassByUserId(userId, pass.toMd5()) > 0
    }

}