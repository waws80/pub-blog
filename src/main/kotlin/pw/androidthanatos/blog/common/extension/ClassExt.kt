package pw.androidthanatos.blog.common.extension

import java.lang.RuntimeException


/**
 * 实例化自定义类
 */
inline fun < reified T> T?.classSafeUse(): T{
    val pkg = T::class.java.`package`

    if ( pkg!= null && pkg.name.contains("pw.androidthanatos.blog")){
        return this ?: T::class.java.newInstance()
    }else{
        throw RuntimeException("当前类不支持实例化===》$this")
    }
}