package pw.androidthanatos.blog.common.upload

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import pw.androidthanatos.blog.FILE_PATH
import pw.androidthanatos.blog.common.extension.toMd5
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.UserBean
import java.io.File
import java.lang.Exception
import java.util.HashMap


/**
 * 上传文件工具类
 */
@Component
class UploadFileUtil {

    /**
     * @param request http请求对象
     * [HashMap<String, Pair<String, FileType>>]  key:params value: [Pair] v1: 保存的路径 v2: 文件类型
     */
    fun uploadFile(req: MultipartHttpServletRequest, userBean: UserBean): HashMap<String, Pair<String, FileType>>{
        val map = HashMap<String, Pair<String, FileType>>()
        val fileNames = req.fileNames
        fileNames.forEach {
            val file = req.getFile(it)
            if (file != null && !file.isEmpty && !file.originalFilename.isNullOrEmpty()){
                val name = file.originalFilename?:""
                val suffix = name.substring(name.lastIndexOf("."))
                val user_ext = userBean.userId.toMd5() + "_"
                val fileName = user_ext + createId() + suffix
                val dest = File("$FILE_PATH$fileName")
                if (!dest.parentFile.exists()){
                    dest.parentFile.mkdir()
                }
                try {
                    file.transferTo(dest)
                    val type = getContentType(file.contentType)
                    map[it] = Pair(fileName,type)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        return map
    }

    private fun getContentType(contentType: String?): FileType {
        return if (!contentType.isNullOrEmpty()) {
            if (contentType.startsWith("image/")){
                FileType.IMG
            }else{
                FileType.FILE
            }
        } else {
            FileType.FILE
        }
    }

}