package pw.androidthanatos.blog.common.contract


//====================global header==============
//token的key
const val KEY_HEADER_TOKEN = "token"

//刷新token的key
const val KEY_HEADER_REFRESH_TOKEN = "Refresh-token"

//===================== code and msg=================

const val CODE_SUCCESS = 1000
const val MSG_SUCCESS = "请求成功"

const val CODE_FAILURE = 2000
const val MSG_FAILURE = "请求失败"

const val CODE_PARAMS_ERROR = 2001
const val MSG_PARAMS_ERROR = "请求参数错误"

const val CODE_PHONE_HAS_REGISTER = 2002
const val MSG_PHONE_HAS_REGISTER = "手机号已注册"

const val CODE_PHONE_NOT_REGISTER = 2003
const val MSG_PHONE_NOT_REGISTER = "手机号未注册"

const val CODE_LOGIN_PARAMS_ERROR = 2004
const val MSG_LOGIN_PARAMS_ERROR = "登录信息出错"

const val CODE_HEADER_NO_TOKEN = 2005
const val MSG_HEADER_NO_TOKEN = "请求token不存在"

const val CODE_PERMISSION_DENIED = 2006
const val MSG_PERMISSION_DENIED = "权限不足"

const val CODE_USER_STATUS_BLACK = 2007
const val MSG_USER_STATUS_BLACK = "用户被拉黑"

const val CODE_PATH_NOT_FOUND = 3000
const val MSG_PATH_NOT_FOUND = "请求路径不存在"

const val CODE_RES_NOT_FOUND = 3001
const val MSG_RES_NOT_FOUND = "请求资源不存在"

const val CODE_TOKEN_ERROR = 4000
const val MSG_TOKEN_ERROR = "登录token出错"

const val CODE_TOKEN_TIME_OUT = 4001
const val MSG_TOKEN_TIME_OUT = "登录状态过期"

const val CODE_PERMISSION_ERROR = 4002
const val MSG_PERMISSION_ERROR = "权限不足"

const val CODE_SERVICE_ERROR = 5000
const val MSG_SERVICE_ERROR = "服务器繁忙"

const val CODE_ILLEGAL_REQUEST = 5001
const val MSG_ILLEGAL_REQUEST = "非法请求"

const val MSG_LINK_EXPIRES = "链接失效"

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


//========================= to do type ======================

const val TODO_TYPE_NORMAL = 1 //普通

const val TODO_TYPE_STUDY = 2 //学习

const val TODO_TYPE_WORK = 3 //工作

const val TODO_TYPE_PLAY = 4 //娱乐

const val TODO_TYPE_FAMILY = 5 //家庭

const val TODO_TYPE_OTHER = 6 //其他


const val TODO_LIST_NORMAL = 1 //普通

const val TODO_LIST_TOP = 2 //置顶



const val TODO_DEL_NORMAL = 1//没有删除

const val TODO_DEL_DEL = 2//已经删除



const val TODO_REMIND_NORMAL = 1 //不提醒

const val TODO_REMIND_REMIND = 2 //提醒



//=========== article 类型==============================

val ARTICLE_TYPE = mutableListOf<HashMap<String, Any>>().apply {
    add(hashMapOf<String, Any>().apply {
        put("name","安卓")
        put("value", listOf("全部", "控件", "图片加载", "网络访问", "新特性", "其他"))
    })
    add(hashMapOf<String, Any>().apply {
        put("name","后端")
        put("value", listOf("全部", "其他"))
    })
    add(hashMapOf<String, Any>().apply {
        put("name","前端")
        put("value", listOf("全部", "其他"))
    })
}
