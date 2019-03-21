package pw.androidthanatos.blog.common.extension


/**
 * [Boolean] 扩展类
 */

fun Boolean?.safeUse(def: Boolean = false): Boolean{
    return this ?: def
}

fun Boolean.yes(next:()->Unit): Boolean{
    if (this){
        next.invoke()
    }
    return this
}

fun Boolean.no(next:()->Unit): Boolean{
    if (!this){
        next.invoke()
    }
    return this
}

fun Boolean.check(yes:()->Unit, no:()->Unit): Boolean{
    yes(yes)
    no(no)
    return this
}