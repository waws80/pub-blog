package pw.androidthanatos.blog



const val debug = true


/**
 * token过期天数
 */
val TOKEN_EXPIRES_DAY = if (debug) 2 else 7

/**
 * 上传文件路径
 */
val FILE_PATH = if (debug) "/home/thanatos/files/" else "C:\\project\\files\\"

/**
 * 邮箱更新密码网页路径
 */
val EMAIL_HTML_PATH = if (debug) "http://localhost:8080/reSetPass.html" else "https://androidthanatos.pw/reSetPass.html"

