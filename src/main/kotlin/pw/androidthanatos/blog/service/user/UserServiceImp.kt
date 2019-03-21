package pw.androidthanatos.blog.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pw.androidthanatos.blog.common.annotation.UserStatus
import pw.androidthanatos.blog.common.extension.toBase64Encode
import pw.androidthanatos.blog.common.extension.toMd5
import pw.androidthanatos.blog.entity.UserBean
import pw.androidthanatos.blog.mapper.UserMapper

@Service
class UserServiceImp : UserService{

    @Autowired
    private lateinit var mUserMapper: UserMapper

    override fun findUserByUserId(userId: String): UserBean? {
        return mUserMapper.findUserById(userId)
    }

    override fun findUserByPhone(phone: String): UserBean? {
        return mUserMapper.findUserByPhone(phone)
    }

    override fun findAllUser(): List<UserBean> {
        return mUserMapper.findAllUser()
    }

    override fun addUser(userBean: UserBean): Boolean {
        return mUserMapper.addUser(userBean.apply {
            //加密信息
            nickName = nickName.toBase64Encode()
            phone = phone.toBase64Encode()
            pass = pass.toMd5()
            email = email.toBase64Encode()
        }) > 0
    }

    override fun updateUserById(userBean: UserBean): Boolean {
        return mUserMapper.updateUserById(userBean.apply {
            //加密信息
            nickName = nickName.toBase64Encode()
            phone = phone.toBase64Encode()
            email = email.toBase64Encode()
        }) > 0
    }

    override fun updatePassByEmail(pass: String, email: String): Boolean {
        //加密信息后更新
        return mUserMapper.updatePassByEmail(pass.toMd5(), email.toBase64Encode()) > 0
    }

    override fun updateUserStatusById(userId: String, @UserStatus status: Int): Boolean {
        return mUserMapper.updateUserStatusById(userId, status) > 0
    }

}