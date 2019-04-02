package pw.androidthanatos.blog.common.extension

import pw.androidthanatos.blog.common.util.*

/**
 * 字符串扩展类
 */

/**
 * 字符串安全使用
 */
fun String?.safeUse(def: String = ""): String{
    return if (this.isNullOrEmpty()) def else this
}

/**
 * md5加密
 */
fun String.toMd5(): String{
    return md5(this)
}

/**
 * base64加密
 */
fun String.toBase64Encode(): String{
    return base64Encode(this)
}

/**
 * 解码base64　
 * [sysEncode] 是否使用的是当前程序加密
 */
fun String.decodeByBase64(sysEncode: Boolean = true): String{
    return base64Decode(this, sysEncode)
}

fun String?.toTimeStamp(error:()->Unit): Long{
    if(isNullOrEmpty()){
        error.invoke()
    }else{
        try {
            return this?.toLong()!!
        }catch (e: Exception){
            error.invoke()
        }
    }
    return 0
}

/**
 * 判断是否符合密码
 */
fun String.isPassWord(): Boolean{
    return RegexUtil.isPassWord(this)
}

/**
 * 判断是否符合手机
 */
fun String.isMobileSimple(): Boolean{
    return RegexUtil.isMobileSimple(this)
}

/**
 * 判断是否符合手机
 */
fun String.isMobile(): Boolean{
    return RegexUtil.isMobile(this)
}

/**
 * 判断是否符合邮箱格式
 */
fun String.isEmail(): Boolean{
    return RegexUtil.isEmail(this)
}

/**
 * 是否是url格式
 */
fun String.isURL(): Boolean{
    return this.startsWith("http") && RegexUtil.isURL(this)
}

/**
 * 是否符合用户名格式
 */
fun String.isUsername(): Boolean{
    return RegexUtil.isUsername(this)
}

/**
 * 是否符合ip格式
 */
fun String.isIP(): Boolean{
    return RegexUtil.isIP(this)
}

/**
 * 是否匹配正则
 */
fun String.isMatch(regex: String): Boolean{
    return RegexUtil.isMatch(regex, this)
}

/**
 * 获取正则匹配部分
 */
fun String.getMatch(regex: String):List<String>{
    return RegexUtil.getMatches(regex, this)
}

/**
 * 替换掉正则匹配的第一部分
 */
fun String.getReplaceFirstMath(regex: String, replace: String): String{
    return RegexUtil.getReplaceFirst(this, regex, replace)
}

/**
 * 替换掉正则匹配的所有部分
 */
fun String.getReplaceAllMath(regex: String, replace: String): String{
    return RegexUtil.getReplaceAll(this, regex, replace)
}