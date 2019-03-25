package pw.androidthanatos.blog.common.mail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.extension.safeUse
import java.util.concurrent.ConcurrentHashMap

@Component
class SendMail {

    companion object {

        private val concurrentHashMap = ConcurrentHashMap<String, Boolean>()

        private const val resetPath = "http://localhost:8080/reSetPass.html"

        /**
         * 链接是否已经使用过
         */
        fun linkExpires(token: String): Boolean{
            return concurrentHashMap[token].safeUse()
        }

        /**
         * 发送一个连接创建一个有效标志
         */
        fun put(token: String){
            concurrentHashMap[token] = true
        }

        //点击一个链接移除标志
        fun remove(token: String){
            concurrentHashMap.remove(token)
        }
    }

    @Value("\${spring.mail.username}")
    private lateinit var mFrom: String

    @Autowired
    private lateinit var mailSender: JavaMailSender

    /**
     * 发送邮箱
     */
    fun send(to: String, subject: String, text: String){
        val mailMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mailMessage, true)
        helper.setFrom(mFrom)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text)
        mailSender.send(mailMessage)
    }

    /**
     * 发送重置密码邮件
     */
    fun sendResetPassMail(to: String, token: String){
        send(to, "个人博客公开接口项目--重置密码",
                "$resetPath?$token")
        put(token)

    }
}