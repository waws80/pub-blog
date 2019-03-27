package pw.androidthanatos.blog.common.extension

import pw.androidthanatos.blog.common.exception.ParamsErrorException


fun <R> List<R>?.listSafeUse(): List<R>{
    return if (this.isNullOrEmpty()){
        emptyList()
    }else{
        this
    }
}

fun <R> MutableList<R>?.mutableSafeUse(): MutableList<R>{
    return if (this.isNullOrEmpty()){
        mutableListOf()
    }else{
        this
    }
}

inline fun <reified R> Array<R>?.arraySafeUse(): Array<R>{
    return if (this.isNullOrEmpty()) {
        emptyArray()
    }else{
        this
    }
}

fun <K, V> Map<K, V>?.mapSafeUse(): Map<K, V>{
    return if (this.isNullOrEmpty()){
        emptyMap()
    }else{
        this
    }
}

/**
 * 过滤空值
 */
fun <K, V, M : Map<K, V>> M?.filterEmptyValue(): M{
    @Suppress("UNCHECKED_CAST")
    return this.mapSafeUse().filterValues { it.toString().isNotEmpty() } as M
}

/**
 * [Map<K, String>] 安全的获取值
 */
fun <K, M : Map<K, String>> M?.safeNotEmptyGet(key: K, notEmpty:(String)->Unit = {}): String{
    @Suppress("UNCHECKED_CAST")
    val value = this.mapSafeUse()[key]
    if (!value.isNullOrEmpty()){
        notEmpty.invoke(value)
        return value
    }
    throw ParamsErrorException()
}

fun <V> List<V>?.filterEmptyValue(save:((V)->Boolean)? = null): List<V>{
    return this.listSafeUse().filter {
        save?.invoke(it) ?: it.toString().isNotEmpty()
    }
}
