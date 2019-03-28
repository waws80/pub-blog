package pw.androidthanatos.blog.service.article

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pw.androidthanatos.blog.entity.ArticleBean
import pw.androidthanatos.blog.mapper.ArticleMapper

@Service
class ArticleServiceImp : ArticleService {

    @Autowired
    private lateinit var mArticleMapper: ArticleMapper

    override fun addArticle(articleBean: ArticleBean): Boolean {
        return mArticleMapper.addArticle(articleBean) > 0
    }

    override fun delArticle(articleId: String, articleUserId: String): Boolean {
        return mArticleMapper.delArticle(articleId, articleUserId) > 0
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

    override fun findArticleLikeCount(articleId: String): Long {
        return mArticleMapper.findArticleLikeCount(articleId)
    }
}