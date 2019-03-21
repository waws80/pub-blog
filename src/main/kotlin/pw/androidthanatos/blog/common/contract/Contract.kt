package pw.androidthanatos.blog.common.contract


//====================global header==============
//token的key
const val KEY_HEADER_TOKEN = "token"

//刷新token的key
const val KEY_HEADER_REFRESH_TOKEN = "refresh_token"

//===================== code and msg=================

const val CODE_SUCCESS = 1000
const val MSG_SUCCESS = "请求成功"

const val CODE_FAILURE = 2000
const val MSG_FAILURE = "请求失败"

const val CODE_PARAMS_ERROR = 2001
const val MSG_PARAMS_ERROR = "请求参数错误"

const val CODE_HEADER_NO_TOKEN = 2002
const val MSG_HEADER_NO_TOKEN = "请求token不存在"

const val CODE_PATH_NOT_FOUND = 3000
const val MSG_PATH_NOT_FOUND = "请求资源不存在"

const val CODE_TOKEN_ERROR = 4000
const val MSG_TOKEN_ERROR = "登录token出错"

const val CODE_TOKEN_TIME_OUT = 4001
const val MSG_TOKEN_TIME_OUT = "登录状态过期"

const val CODE_SERVICE_ERROR = 5000
const val MSG_SERVICE_ERROR = "服务器繁忙"

//================== md5 extension string ===============

const val STR_EXTENSION_MD5 = "pw.androidthanatos.blog-md5"

const val STR_EXTENSION_BASE64 = "pw.androidthanatos.blog-base64"

const val KEY_EXTENSION_BASE64 = "pw.androidthanatos.blog-base64-extension-key"

const val KEY_VALUE_BASE64 = "pw.androidthanatos.blog-base64-value-key"


//==================== user status =======================

const val STATUS_USER_NORMAL = 1 // 正常用户

const val STATUS_USER_BLACK = 2 //拉黑用户

const val TYPE_USER_NORMAL = 1 // 普通用户

const val TYPE_USER_ADMIN = 2 //管理员用户
