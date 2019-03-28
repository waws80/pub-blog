package pw.androidthanatos.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.common.annotation.ApiVersion
import pw.androidthanatos.blog.common.contract.ARTICLE_TYPE
import pw.androidthanatos.blog.common.response.ResponseBean

/**
 * 文章控制器
 */
@Suppress("MVCPathVariableInspection")
@ApiVersion(1)
@RestController
@RequestMapping("article/{version}")
class ArticleController : BaseController() {

    /**
     * 文章欢迎接口
     */
    @GetMapping("index")
    fun index() = ResponseBean(data = "欢迎使用文章接口")

    /**
     * 获取文章类型列表
     */
    @GetMapping("types")
    fun getArticleType() = ResponseBean(data = hashMapOf(Pair("types",
            ARTICLE_TYPE)))





}