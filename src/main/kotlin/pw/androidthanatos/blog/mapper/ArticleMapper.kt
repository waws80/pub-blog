package pw.androidthanatos.blog.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.entity.ArticleBean
import pw.androidthanatos.blog.entity.TodoBean
import java.util.*

/**
 * 文章数据库查询
 */
@Component
interface ArticleMapper {


    /**
     * 添加文章
     */
    @Insert("insert into tb_article (articleId, articleTitle, articleContent, " +
            "articleCreateDate, articleType, articleUserId,articleSuperType, articleUrl, " +
            "articleVisitsCount) " +
            "values (#{articleId}, #{articleTitle}, #{articleContent}, #{articleCreateDate}, " +
            "#{articleType}, #{articleUserId}, #{articleSuperType}, #{articleUrl}, " +
            "#{articleVisitsCount}) ")
    fun addArticle(articleBean: ArticleBean): Int

    /**
     * 删除文章
     */
    @Delete("delete from tb_article " +
            "where articleId = #{articleId} ")
    fun delArticle(articleId: String): Int

    /**
     * 更新文章文本信息
     */
    @Update("update tb_article " +
            "set articleContent= #{articleContent} " +
            "where articleId = #{articleId} and articleUserId = #{articleUserId} ")
    fun updateArticleContent(articleId: String, articleUserId: String, articleContent: String): Int

    /**
     * 更新文章类型
     */
    @Update("update tb_article " +
            "set articleType = #{articleType}, articleSuperType = #{articleSuperType} " +
            "where articleId = #{articleId} and articleUserId = #{articleUserId} ")
    fun updateArticleType(articleId: String, articleUserId: String,
                          articleType: String, articleSuperType: String): Int

    /**
     * 更新文章url并内部动态更新文本
     */
    @Update("update tb_article " +
            "set articleUrl = #{articleUrl} " +
            "where articleId = #{articleId} and articleUserId = #{articleUserId} ")
    fun updateArticleUrl(articleId: String, articleUserId: String, articleUrl: String):Int


    /**
     * 更新文章访问次数
     */
    @Update("update tb_article " +
            "set articleVisitsCount = articleVisitsCount + 1 " +
            "where articleId = #{articleId} ")
    fun updateArticleVisitsCount(articleId: String): Int


    /**
     * 获取文章访问次数
     */
    @Select("select articleVisitsCount " +
            "from tb_article " +
            "where articleId = #{articleId} ")
    fun findArticleVisitsCount(articleId: String): Long

    /**
     * 添加用户点击了喜欢文章
     */
    @Insert("insert into tb_article_like (articleLikeArticleId, articleLikeUserId) " +
            "values (#{articleId}, #{userId})")
    fun addLikeArticle(articleId: String, userId: String): Int

    /**
     * 移除用户对文章的喜欢
     */
    @Delete("delete from tb_article_like " +
            "where articleLikeArticleId = #{articleId} and articleLikeUserId = #{userId} ")
    fun removeLikeArticle(articleId: String, userId: String): Int

    /**
     * 移除文章的喜欢
     */
    @Delete("delete from tb_article_like " +
            "where articleLikeArticleId = #{articleId} ")
    fun removeLikeArticleByArticleId(articleId: String): Int


    /**
     * 获取喜欢文章的人数
     */
    @Select("select count(articleLikeUserId) " +
            "from tb_article_like where articleLikeArticleId = #{articleId} ")
    fun findArticleLikeCount(articleId: String): Long

    /**
     * 获取当前用户有没有对某篇文章点击喜欢
     */
    @Select("select count(*)\n" +
            "from tb_article_like\n" +
            "where articleLikeArticleId = #{articleId} and articleLikeUserId = #{userId}")
    fun findArticleLike(articleId: String, userId: String): Long


    /**
     * 获取文章详情
     */

    @Select("select count(articleLikeArticleId) as likeCount, articleId, articleTitle, articleContent, " +
            "articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "from tb_article_like left join tb_article on articleLikeArticleId = articleId " +
            "where articleId = #{articleId} ")
    fun findArticleByArticleId(articleId: String): ArticleBean?


    /**
     * 通过待办事项标题获取实体
     */
    @Select("select count(*) " +
            "from tb_article " +
            "where articleTitle = #{articleTitle} ")
    fun findArticleByTitle(articleTitle: String): Int

    /**
     * 获取所有的文章
     */
    @Select("select count(articleLikeArticleId) as likeCount, articleId, articleTitle, " +
            "            articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "            from tb_article left join tb_article_like on articleId = articleLikeArticleId " +
            "            group by articleId order by articleCreateDate desc ")
    fun findAllArticle(): List<ArticleBean>


    /**
     * 获取所有文章的数量
     */
    @Select("select count(articleId) " +
            "from tb_article ")
    fun findAllArticleCount(): Long


    /**
     * 获取分类文章列表（当二级分类为空的时候，只匹配一级分类）
     */
    @Select("select count(articleLikeArticleId) as likeCount, articleId, articleTitle, " +
            "articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "from tb_article left join tb_article_like on articleLikeArticleId = articleId " +
            "where if( (#{articleType} is null), " +
            "(articleSuperType = #{articleSuperType}), " +
            "(articleSuperType = #{articleSuperType} and articleType = #{articleType})) " +
            "group by articleId order by articleCreateDate desc ")
    fun findArticleBySuperType(articleSuperType: String, articleType: String?): List<ArticleBean>

    /**
     * 获取分类文章的数量
     */
    @Select("select count(articleId) " +
            "from tb_article " +
            "where if( (#{articleType} is null), " +
            "(articleSuperType = #{articleSuperType}), " +
            "(articleSuperType = #{articleSuperType} and articleType = #{articleType})) ")
    fun findArticleBySuperTypeCount(articleSuperType: String, articleType: String?): Long
    /**
     * 查找某个用户的所有文章
     */
    @Select("select count(articleLikeArticleId) as likeCount, articleId, articleTitle, " +
            "articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "from tb_article left join tb_article_like on articleLikeArticleId = articleId " +
            "where articleUserId = #{articleUserId} " +
            "group by articleId order by articleCreateDate desc")
    fun findArticleByUserId(articleUserId: String): List<ArticleBean>


    /**
     * 获取某个用户的所有文章个数
     */
    @Select("select count(articleId) " +
            "from tb_article " +
            "where articleUserId = #{articleUserId} ")
    fun findArticleByUserIdCount(articleUserId: String): Long

    /**
     * 获取当前用户喜欢的文章列表
     */
    @Select("select count(articleLikeArticleId) as likeCount, articleId, articleTitle, " +
            "articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "from tb_article left join tb_article_like on articleId = articleLikeArticleId " +
            "where articleLikeUserId = #{articleLikeUserId} " +
            "group by articleId order by articleCreateDate desc ")
    fun findCurrentUserLikeArticle(articleLikeUserId: String): List<ArticleBean>


    /**
     * 获取当前用户喜欢的文章列表的数量
     */
    @Select("select count(articleLikeArticleId)  " +
            "from tb_article_like where articleLikeUserId = #{articleLikeUserId} ")
    fun findCurrentUserLikeArticleCount(articleLikeUserId: String): Long


    /**
     * 获取当前用户阅读历史记录
     */
    @Select("select count(articleLikeArticleId) as likeCount, tb_article_history.createDate as historyDate, articleId, articleTitle, " +
            "            articleCreateDate, articleType, articleUserId, articleSuperType, articleUrl, articleVisitsCount " +
            "            from tb_article " +
            "            left join tb_article_like on articleId = articleLikeArticleId " +
            "            left join tb_article_history  on articleId = articleHistoryArticleId " +
            "            where articleUserId = #{articleHistoryUserId} and articleHistoryUserId = #{articleHistoryUserId} " +
            "  group by articleId order by articleCreateDate desc ")
    fun findCurrentUserHistoryRead(articleHistoryUserId: String): List<ArticleBean>


    /**
     * 获取当前用户浏览的文章列表的数量
     */
    @Select("select count(articleHistoryArticleId)  " +
            "from tb_article_history where articleHistoryUserId = #{articleHistoryUserId} ")
    fun findCurrentUserHistoryReadCount(articleHistoryUserId: String): Long

    /**
     * 将文章添加到当前用户的阅读记录
     */
    @Insert("insert into tb_article_history (articleHistoryArticleId, articleHistoryUserId, createDate) " +
            "values (#{articleId}, #{userId}, #{createDate}) ")
    fun addToHistory(articleId: String, userId: String, createDate: Long = Date().time): Int

    /**
     * 查找特定文章对特定人的阅读记录
     */
    @Select("select count(*) " +
            "from tb_article_history " +
            "where articleHistoryArticleId = #{articleId} and articleHistoryUserId = #{userId} ")
    fun findArticleHistoryInfo(articleId: String, userId: String): Int
}