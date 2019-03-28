package pw.androidthanatos.blog.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.entity.ArticleBean

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
            "articleLike, articleVisitsCount) " +
            "values (#{articleId}, #{articleTitle}, #{articleContent}, #{articleCreateDate}, " +
            "#{articleType}, #{articleUserId}, #{articleSuperType}, #{articleUrl}, #{articleLike}, " +
            "#{articleVisitsCount}) ")
    fun addArticle(articleBean: ArticleBean): Int

    /**
     * 删除文章
     */
    @Delete("delete from tb_article " +
            "where articleId = #{articleId} and articleUserId = #{articleUserId} ")
    fun delArticle(articleId: String, articleUserId: String): Int

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
            "where articleLikeArticleId = #{articleId} articleLikeUserId = #{userId} ")
    fun removeLikeArticle(articleId: String, userId: String): Int


    /**
     * 获取喜欢文章的人数
     */
    @Select("select count(articleLikeUserId) " +
            "from tb_article_like where articleLikeArticleId = #{articleId} ")
    fun findArticleLikeCount(articleId: String): Long


    /**
     * 获取所有的文章
     */
    @Select("select * from tab_article order by articleCreateDate desc")
    fun findAllArticle(): List<ArticleBean>


    fun findArticleBySuperType()
}