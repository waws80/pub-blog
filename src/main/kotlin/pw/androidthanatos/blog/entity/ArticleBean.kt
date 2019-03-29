package pw.androidthanatos.blog.entity

import java.util.*

/**
 * 文章实体类
 */
data class ArticleBean(val articleId: String = "",
                       val articleTitle: String = "",
                       var articleContent: String = "",
                       val articleCreateDate: Long = Date().time,
                       var articleType: String = "", //文章二级分类
                       var articleSuperType: String = "", // 文章一级分类
                       var articleUserId: String = "",
                       var articleUrl: String = "",
                       var articleVisitsCount: Long = 0,
                       var likeCount: Long = 0)