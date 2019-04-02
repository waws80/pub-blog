package pw.androidthanatos.blog.controller

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pw.androidthanatos.blog.common.annotation.ApiVersion
import pw.androidthanatos.blog.common.annotation.Login
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.exception.ResNotFoundException
import pw.androidthanatos.blog.common.extension.isURL
import pw.androidthanatos.blog.common.extension.logi
import pw.androidthanatos.blog.common.extension.no
import pw.androidthanatos.blog.common.extension.safeNotEmptyGet
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.response.ResponseHandle
import pw.androidthanatos.blog.common.response.ResponseWrapper
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.ArticleBean
import pw.androidthanatos.blog.service.article.ArticleService
import java.lang.Exception
import javax.servlet.http.HttpServletRequest

/**
 * 文章控制器
 */
@Suppress("MVCPathVariableInspection")
@ApiVersion(1)
@RestController
@RequestMapping("article/{version}")
class ArticleController : BaseController() {

    @Autowired
    private lateinit var mArticleService: ArticleService

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

    /**
     * 添加文章
     */
    @Login
    @PostMapping("add")
    fun addArticle() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(this.getParamsMap(request, "articleTitle", "articleContent",
                            "articleSuperType", "articleType", "articleUrl"))
                    this.checkEmpty(params, "articleTitle", "articleSuperType")

                    checkTypes(params, params["articleSuperType"], params["articleType"])
                    if (params["articleContent"].isNullOrEmpty() && params["articleUrl"].isNullOrEmpty()){
                        throw ParamsErrorException()
                    }
                    if (!params["articleUrl"]!!.isURL()){
                        throw ParamsErrorException()
                    }

                    val bean = ArticleBean(articleId = createId(), articleTitle = params["articleTitle"]!!,
                            articleContent = params["articleContent"]!!, articleSuperType = params["articleSuperType"]!!,
                            articleType = params["articleType"]!!,articleUrl = params["articleUrl"]!!,
                            articleUserId = getUserInfoByToken()!!.userId)
                    tags["bean"] = bean

                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val bean = tags["bean"] as ArticleBean
                    if (mArticleService.hasByArticleTitle(bean.articleTitle)){
                        responseBean.code = CODE_INFO_TILE_REPEAT
                        responseBean.msg = MSG_INFO_TILE_REPEAT
                    }else{
                        val add = mArticleService.addArticle(bean)
                        if (!add){
                            responseBean.buildServiceError()
                        }
                    }
                }
            }).process()


    /**
     * 删除文章
     */
    @Login
    @DeleteMapping("del")
    fun delArticle() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {

                    val articleId = this.getParamsNotEmpty(request, "articleId")
                    val userId = getUserInfoByToken()!!.userId
                    params["articleId"] = articleId
                    params["userId"] = userId

                    val bean = mArticleService.findArticleByArticleId(articleId)
                    if (bean == null || bean.articleUserId != userId){
                        throw ResNotFoundException()
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val articleId = params["articleId"]!!
                    val delLike = mArticleService.removeLikeArticle(articleId)
                    if (!delLike){
                        responseBean.buildServiceError()
                    }
                    val del = mArticleService.delArticle(articleId)
                    if (!del){
                        responseBean.buildServiceError()
                    }
                }

            }).process()


    /**
     * 更新文章内容
     */
    @Login
    @PutMapping("updateContent")
    fun updateArticleContent() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val articleContent = request.getParameter("articleContent")
                    val articleId = request.getParameter("articleId")
                    val userId = getUserInfoByToken()!!.userId
                    checkParamsEmpty(articleId, articleContent)
                    params["articleId"] = articleId
                    params["articleContent"] = articleContent
                    params["userId"] = userId
                    val bean = mArticleService.findArticleByArticleId(articleId)
                    if (bean == null || userId != bean.articleUserId){
                        throw ResNotFoundException()
                    }

                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val articleId = params["articleId"]!!
                    val articleContent = params["articleContent"]!!
                    val userId = params["userId"]!!
                    val update = mArticleService.updateArticleContent(articleId,userId,articleContent)
                    if (!update){
                        responseBean.buildServiceError()
                    }
                }

            }).process()


    /**
     * 更新文章内容
     */
    @Login
    @PutMapping("updateUrl")
    fun updateArticleUrl() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val articleUrl = request.getParameter("articleUrl")
                    val articleId = request.getParameter("articleId")
                    val loadBody = request.getParameter("loadBody")
                    val userId = getUserInfoByToken()!!.userId
                    checkParamsEmpty(articleId, articleUrl, loadBody)
                    if (!articleUrl.isURL()){
                        throw ParamsErrorException()
                    }
                    if (convertBool(loadBody)){
                        params["loadBody"] = true.toString()
                        try {
                            val body = Jsoup.connect(articleUrl).get()
                            val content = body.getElementsByTag("body").html()
                            params["success"] = true.toString()
                            params["articleContent"] = if (content.isNotEmpty()) content else ""
                        }catch (e: Exception){
                            logi("获取链接内容失败")
                            params["success"] = false.toString()
                        }
                    }else{
                        params["loadBody"] = false.toString()
                    }
                    params["articleId"] = articleId
                    params["articleUrl"] = articleUrl
                    params["userId"] = userId
                    val bean = mArticleService.findArticleByArticleId(articleId)
                    if (bean == null || userId != bean.articleUserId){
                        throw ResNotFoundException()
                    }

                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val articleId = params["articleId"]!!
                    val articleUrl = params["articleUrl"]!!
                    val userId = params["userId"]!!
                    if (params["loadBody"]!!.toBoolean()){
                        if (!params["success"]!!.toBoolean()){
                            responseBean.code = CODE_LOAD_URL_BODY_ERROR
                            responseBean.msg = MSG_LOAD_URL_BODY_ERROR
                            return
                        }else{
                            //更新文本
                            val articleContent = params["articleContent"]!!
                            val update = mArticleService.updateArticleContent(articleId, userId, articleContent)
                            if (!update){
                                responseBean.buildServiceError()
                                return
                            }
                        }

                    }
                    val update = mArticleService.updateArticleUrl(articleId,userId,articleUrl)
                    if (!update){
                        responseBean.buildServiceError()
                    }
                }

            }).process()


    /**
     * 更新文章类型
     */
    @Login
    @PutMapping("updateTypes")
    fun updateArticleTypes() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "articleId", "articleSuperType", "articleType"))

                    checkEmpty(params, "articleId", "articleSuperType", "articleType")
                    checkTypes(params, params["articleSuperType"],params["articleType"])
                    val userId = getUserInfoByToken()!!.userId
                    params["userId"] = userId
                    val bean = mArticleService.findArticleByArticleId(params["articleId"]!!)
                    if (bean == null || bean.articleUserId != userId){
                        throw ResNotFoundException()
                    }

                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val articleId = params["articleId"]!!
                    val userId = params["userId"]!!
                    val articleSuperType = params["articleSuperType"]!!
                    val articleType = params["articleType"]!!
                    val update = mArticleService.updateArticleType(articleId, userId, articleType,articleSuperType)
                    if (!update){
                        responseBean.buildServiceError()
                    }
                }

            }).process()


    /**
     * 添加用户对文章的喜欢
     */
    @Login
    @PutMapping("like")
    fun likeArticle() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val userId = getUserInfoByToken()!!.userId
                    val articleId = request.getParameter("articleId")
                    checkParamsEmpty(articleId)
                    val bean = mArticleService.findArticleByArticleId(articleId)
                    checkBean(bean)
                    params["userId"] = userId
                    params["articleId"] = articleId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val like = mArticleService.findArticleLike(params["articleId"]!!,  params["userId"]!!)
                    if (like){
                        val sub = mArticleService.removeLikeArticle(params["articleId"]!!,  params["userId"]!!)
                        if (!sub){
                            responseBean.buildServiceError()
                        }
                        responseBean.data = HashMap<String, Any>().apply {
                            put("count",mArticleService.findArticleLikeCount(params["articleId"]!!))
                        }
                    }else{
                        val add = mArticleService.addLikeArticle(params["articleId"]!!,  params["userId"]!!)
                        if (!add){
                            responseBean.buildServiceError()
                        }
                        responseBean.data = HashMap<String, Any>().apply {
                            put("count",mArticleService.findArticleLikeCount(params["articleId"]!!))
                        }
                    }

                }
            }).process()


    /**
     * 获取文章详情
     */
    @Login
    @GetMapping("detail")
    fun articleDetail() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "articleId"))
                    checkEmpty(params, "articleId")
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val articleId = params["articleId"]!!
                    //先添加浏览记录和次数再获取文章详情
                    if (mArticleService.updateArticleVisitsCount(articleId)){
                        if (!mArticleService.findArticleHistoryInfo(articleId, getUserInfoByToken()!!.userId)){
                            //没有阅读过当前文章，先添加阅读历史
                            if (mArticleService.addToHistory(articleId, getUserInfoByToken()!!.userId)){
                                val bean = mArticleService.findArticleByArticleId(articleId)
                                checkBean(bean)
                                responseBean.data = bean
                            }
                        }else{
                            //阅读过直接获取详情
                            val bean = mArticleService.findArticleByArticleId(articleId)
                            checkBean(bean)
                            responseBean.data = bean
                        }
                    }else{
                        responseBean.buildServiceError()
                    }
                }

            }).process()

    /**
     * 获取所有文章
     */
    @GetMapping("allArticle")
    fun getAllArticle() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findAllArticle(
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取制定类型的文章
     */
    @GetMapping("filterTypes")
    fun articleByTypes() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                    params.putAll(getParamsMap(request, "articleSuperType", "articleType"))
                    checkEmpty(params,"articleSuperType")
                    checkSuperType(params, params["articleSuperType"])
                    if (params["articleType"].isNullOrEmpty()){
                        params.remove("articleType")
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findArticleBySuperType(
                            params["articleSuperType"]!!,
                            params["articleType"],
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取某个人所有文章
     */
    @GetMapping("allWithUserId")
    fun getAllArticleByUserId() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                    val userId = request.getParameter("userId")
                    checkParamsEmpty(userId)
                    val user = userService.findUserByUserId(userId)
                    if (user == null){
                        throw ResNotFoundException()
                    }
                    params["userId"] = userId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findArticleByUserId(params["userId"]!!,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取当前用户所有文章
     */
    @Login
    @GetMapping("allWithCurrentUserId")
    fun getAllArticleByCurrentUserId() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                    val userId = getUserInfoByToken()!!.userId
                    checkParamsEmpty(userId)
                    val user = userService.findUserByUserId(userId)
                    if (user == null){
                        throw ResNotFoundException()
                    }
                    params["userId"] = userId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findArticleByUserId(params["userId"]!!,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取当前用户喜欢的文章列表
     */
    @Login
    @GetMapping("likeList")
    fun getUserLikeArticleList() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findCurrentUserLikeArticle(
                            getUserInfoByToken()!!.userId,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取当前用户浏览过的文章列表
     */
    @Login
    @GetMapping("historyList")
    fun getUserHistoryArticleList() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mArticleService.findCurrentUserHistoryRead(
                            getUserInfoByToken()!!.userId,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()



    /**
     * 校验类型
     */
    private fun checkTypes(params: HashMap<String, String>, articleSuperType: String?, articleType: String?) {
        checkSuperType(params, articleSuperType)
        checkType(params, articleType)
    }

    /**
     * 校验一级类型
     */
    fun checkSuperType(params: HashMap<String, String>, articleSuperType: String?){
        if (articleSuperType.isNullOrEmpty()){
            params["articleSuperType"] = "其它"
        }else{
            var can = false
            ARTICLE_TYPE.forEach {
                if (articleSuperType == it["name"]){
                    can = true
                }
            }
            can.no { throw ParamsErrorException() }
        }
    }

    /**
     * 校验二级类型
     */
    fun checkType(params: HashMap<String, String>, articleType: String?){
        if (articleType.isNullOrEmpty()){
            params["articleType"] = "其它"
        }else{
            var can = false
            ARTICLE_TYPE.forEach {
                (it["value"] as List<String>).forEach {
                    if (articleType == it){
                        can = true
                    }
                }
            }
            can.no { throw ParamsErrorException() }
        }
    }


    private fun checkBean(bean: ArticleBean?){
        if (bean == null || bean.articleId.isNullOrEmpty()){
            throw ResNotFoundException()
        }
    }


}