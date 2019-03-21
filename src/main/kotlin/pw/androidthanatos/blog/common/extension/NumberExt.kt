package pw.androidthanatos.blog.common.extension

/**
 * [Number] 扩展类
 */
fun Number?.safeUse(def: Number = 0):Number{
    return this?:def
}

/**
 * 数字转字符串并转换最大显示限制
 */
fun Number.formatMaxValue(def: Number = 999): String{
    return if (this.toDouble() > def.toDouble()) "$def+" else this.toString()
}