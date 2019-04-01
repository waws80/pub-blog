package pw.androidthanatos.blog.service.article

import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pw.androidthanatos.blog.entity.ArticleBean
import pw.androidthanatos.blog.entity.PageBean
import pw.androidthanatos.blog.mapper.ArticleMapper

/**
 * 文章服务实现类
 */
@Service
class ArticleServiceImp : ArticleService {

    @Autowired
    private lateinit var mArticleMapper: ArticleMapper

    override fun addArticle(articleBean: ArticleBean): Boolean {
        return mArticleMapper.addArticle(articleBean) > 0
    }

    override fun delArticle(articleId: String): Boolean {
        return mArticleMapper.delArticle(articleId) > 0
    }

    override fun updateArticleContent(articleId: String, articleUserId: String, articleContent: String): Boolean {
        return mArticleMapper.updateArticleContent(articleId, articleUserId, articleContent) > 0
    }

    override fun updateArticleType(articleId: String, articleUserId: String, articleType: String, articleSuperType: String): Boolean {
        return mArticleMapper.updateArticleType(articleId, articleUserId, articleType, articleSuperType) > 0
    }

    override fun updateArticleUrl(articleId: String, articleUserId: String, articleUrl: String): Boolean {
        return mArticleMapper.updateArticleUrl(articleId, articleUserId, articleUrl) > 0
    }

    override fun updateArticleVisitsCount(articleId: String): Boolean {
        return mArticleMapper.updateArticleVisitsCount(articleId) > 0
    }

    override fun findArticleVisitsCount(articleId: String): Long {
        return mArticleMapper.findArticleVisitsCount(articleId)
    }

    override fun addLikeArticle(articleId: String, userId: String): Boolean {
        return mArticleMapper.addLikeArticle(articleId, userId) > 0
    }

    override fun removeLikeArticle(articleId: String, userId: String): Boolean {
        return mArticleMapper.removeLikeArticle(articleId, userId) > 0
    }

    override fun removeLikeArticle(articleId: String): Boolean {
        return mArticleMapper.removeLikeArticleByArticleId(articleId) > 0
    }

    override fun findArticleLike(articleId: String, userId: String): Boolean {
        return mArticleMapper.findArticleLike(articleId, userId) > 0
    }

    override fun findArticleLikeCount(articleId: String): Long {
        return mArticleMapper.findArticleLikeCount(articleId)
    }

    override fun findArticleByArticleId(articleId: String): ArticleBean?{
        return mArticleMapper.findArticleByArticleId(articleId)
    }


    override fun hasByArticleTitle(articleTitle: String): Boolean {
        return mArticleMapper.findArticleByTitle(articleTitle) > 0
    }


    override fun findAllArticle(pageNumber: Int, pageSize: Int): PageBean<ArticleBean>{
        PageHelper.startPage<ArticleBean>(pageNumber, pageSize)
        val list = mArticleMapper.findAllArticle()
        val count = mArticleMapper.findAllArticleCount()
        return PageBean(pageNumber, pageSize, count, list)
    }


    override fun findArticleBySuperType(articleSuperType: String, articleType: String?,
                                        pageNumber: Int, pageSize: Int): PageBean<ArticleBean>{
        PageHelper.startPage<ArticleBean>(pageNumber, pageSize)
        val list = mArticleMapper.findArticleBySuperType(articleSuperType, articleType)
        val count = mArticleMapper.findArticleBySuperTypeCount(articleSuperType, articleType)
        return PageBean(pageNumber, pageSize, count, list)
    }


    override fun findArticleByUserId(articleUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean>{
        PageHelper.startPage<ArticleBean>(pageNumber, pageSize)
        val list = mArticleMapper.findArticleByUserId(articleUserId)
        val count = mArticleMapper.findArticleByUserIdCount(articleUserId)
        return PageBean(pageNumber, pageSize, count, list)
    }


    override fun findCurrentUserLikeArticle(articleLikeUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean> {
        PageHelper.startPage<ArticleBean>(pageNumber, pageSize)
        val list = mArticleMapper.findCurrentUserLikeArticle(articleLikeUserId)
        val count = mArticleMapper.findCurrentUserLikeArticleCount(articleLikeUserId)
        return PageBean(pageNumber, pageSize, count, list)
    }

    override fun findCurrentUserHistoryRead(articleHistoryUserId: String, pageNumber: Int, pageSize: Int): PageBean<ArticleBean> {
        PageHelper.startPage<ArticleBean>(pageNumber, pageSize)
        val list = mArticleMapper.findCurrentUserHistoryRead(articleHistoryUserId)
        val count = mArticleMapper.findCurrentUserHistoryReadCount(articleHistoryUserId)
        return PageBean(pageNumber, pageSize, count, list)
    }

    override fun addToHistory(articleId: String, userId: String): Boolean {
        return mArticleMapper.addToHistory(articleId, userId) > 0
    }

    override fun findArticleHistoryInfo(articleId: String, userId: String): Boolean {
        return mArticleMapper.findArticleHistoryInfo(articleId, userId) > 0
    }
}