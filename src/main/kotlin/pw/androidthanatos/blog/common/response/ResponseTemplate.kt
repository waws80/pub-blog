package pw.androidthanatos.blog.common.response

/**
 * 响应类模板
 */
interface ResponseTemplate {

    /**
     * 执行业务逻辑并返回响应结果
     */
    fun process(): ResponseBean
}