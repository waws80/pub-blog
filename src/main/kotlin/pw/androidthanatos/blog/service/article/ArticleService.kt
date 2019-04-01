package pw.androidthanatos.blog.service.article

import pw.androidthanatos.blog.entity.ArticleBean
import pw.androidthanatos.blog.entity.PageBean

interface ArticleService {

    /**
     * 添加文章
     * @param articleBean 文章实体类
     */
    fun addArticle(articleBean: ArticleBean): Boolean

    /**
     * 删除文章
     * @param articleId 文章id
     */
    fun delArticle(articleId: String): Boolean

    /**
     * 更新文章文本信息
     * @param articleId 文章id
     * @param articleUserId 文章所属人id
     * @param articleContent 文章内容
     */
    fun updateArticleContent(articleId: String, articleUserId: String, articleContent: String): Boolean

    /**
     * 更新文章类型
     * @param articleId 文章id
     * @param articleUserId 文章所属人id
     * @param articleType 文章二级分类
     * @param articleSuperType 文章一级分类
     */
    fun updateArticleType(articleId: String, articleUserId: String,
                          articleType: String, articleSuperType: String): Boolean

    /**
     * 更新文章url并内部动态更新文本
     * @param articleId 文章id
     * @param articleUserId 文章所属人id
     * @param articleUrl 文章原文链接
     */
    fun updateArticleUrl(articleId: String, articleUserId: String, articleUrl: String): Boolean


    /**
     * 更新文章访问次数
     * @param articleId 文章id
     */
    fun updateArticleVisitsCount(articleId: String): Boolean


    /**
     * 获取文章访问次数
     * @param articleId 文章id
     */
    fun findArticleVisitsCount(articleId: String): Long

    /**
     * 添加用户点击了喜欢文章
     * @param articleId 文章id
     * @param userId 用户id
     */
    fun addLikeArticle(articleId: String, userId: String): Boolean

    /**
     * 移除用户对文章的喜欢
     * @param articleId 文章id
     * @param userId 用户id
     */
    fun removeLikeArticle(articleId: String, userId: String): Boolean

    /**
     * 移除文章的喜欢
     * @param articleId 文章id
     * @param userId 用户id
     */
    fun removeLikeArticle(articleId: String): Boolean

    /**
     * 判断用户有没有点击当前文章的喜欢
     */
    fun findArticleLike(articleId: String, userId: String): Boolean


    /**
     * 获取喜欢文章的人数
     * @param articleId 文章id
     */
    fun findArticleLikeCount(articleId: String): Long


    /**
     * 通过文章id获取详情
     */
    fun findArticleByArticleId(articleId: String): ArticleBean?


    /**
     * 判断当前标题的文章是否存在
     */
    fun hasByArticleTitle(articleTitle: String): Boolean

    /**
     * 获取所有的文章
     */
    fun findAllArticle(pageNumber: Int, pageSize: Int): PageBean<ArticleBean>


    /**
     * 获取分类文章列表（当二级分类为空的时候，只匹配一级分类）
     */
    fun findArticleBySuperType(articleSuperType: String, articleType: String?,
                               pageNumber: Int, pageSize: Int): PageBean<ArticleBean>

    /**
     * 查找某个用户的所有文章
     */
    fun findArticleByUserId(articleUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean>



    /**
     * 获取当前用户喜欢的文章列表
     */
    fun findCurrentUserLikeArticle(articleLikeUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean>


    /**
     * 获取当前用户阅读历史记录
     */
    fun findCurrentUserHistoryRead(articleHistoryUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean>

    /**
     * 将文章添加到当前用户的阅读记录
     */
    fun addToHistory(articleId: String, userId: String): Boolean

    /**
     * 查找特定文章对特定人的阅读记录
     */
    fun findArticleHistoryInfo(articleId: String, userId: String): Boolean
}