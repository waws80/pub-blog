package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pw.androidthanatos.blog.common.annotation.*
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.exception.ResNotFoundException
import pw.androidthanatos.blog.common.exception.ServiceErrorException
import pw.androidthanatos.blog.common.extension.safeNotEmptyGet
import pw.androidthanatos.blog.common.extension.toTimeStamp
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.response.ResponseHandle
import pw.androidthanatos.blog.common.response.ResponseWrapper
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.TodoBean
import pw.androidthanatos.blog.service.todo.TodoService
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

/**
 * 待办事项工具类
 */
@Suppress("MVCPathVariableInspection")
@ApiVersion(1)
@RestController
@RequestMapping("todo/{version}")
class TodoController : BaseController(){

    @Autowired
    private lateinit var mTodoService: TodoService

    @GetMapping("index")
    fun index() = ResponseBean(data = "欢迎访问TODO接口")

    /**
     * 添加一个待办事项
     */
    @Login
    @PostMapping("addTodo")
    fun addTodo() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "todoTitle",
                            "todoContent","todoPlannedFinishDate", "todoType",
                            "todoTop", "todoRemind"))
                    checkEmpty(params, "todoTitle", "todoContent")
                    val user = getUserInfoByToken()!!
                    val userId = user.userId
                    val todoId = createId()
                    val todoPlannedFinishDate = convertLong(params["todoPlannedFinishDate"])
                    val todoBean = TodoBean(todoId, params["todoTitle"]!!, params["todoContent"]!!,
                            Date().time, todoPlannedFinishDate,
                            parseTodoType(params["todoType"]), null,
                            parseTodoTop(params["todoTop"]), userId, TODO_DEL_NORMAL, parseTodoRemind(params["todoRemind"]))
                    tags["todo"] = todoBean
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val add = mTodoService.addTodo(tags["todo"] as TodoBean)
                    if (!add){
                        responseBean.buildServiceError()
                    }
                }
            }).process()


    /**
     * 更新待办事项删除状态
     */
    @Login
    @PutMapping("updateDelType")
    fun updateTodoDelType() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    //校验参数
                    params.putAll(getParamsMap(request, "todoId", "todoDel"))
                    checkEmpty(params, "todoId", "todoDel")
                    checkInt(params["todoDel"])
                    params["todoDel"] = parseTodoDel(params["todoDel"]).toString()
                    //判断当前todo属于当前用户
                    val currentUserId = getUserInfoByToken()?.userId!!
                    if (mTodoService.findTodoById(params.safeNotEmptyGet("todoId"), currentUserId) == null){
                        throw ResNotFoundException()
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    //更新待办事项
                    val update = mTodoService.updateTodoDelStatus(
                            params.safeNotEmptyGet("todoId"),
                            params.safeNotEmptyGet("todoDel").toInt())
                    if (!update){
                        responseBean.buildServiceError()
                    }
                }

            }).process()


    /**
     * 完成了待办事项
     */
    @Login
    @PutMapping("finish")
    fun finishTodo(): ResponseBean{

        return ResponseWrapper(request,
                object : ResponseHandle<HttpServletRequest>{
                    override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                        //校验参数
                        val todoId = this.getParamsNotEmpty(request, "todoId")
                        //判断当前todo属于当前用户
                        val currentUserId = getUserInfoByToken()?.userId!!
                        val todo = mTodoService.findTodoById(todoId, currentUserId)
                        //非法请求
                        if (todo == null || todo.todoFinishDate != null){
                            throw ResNotFoundException()
                        }
                        params["todoId"] = todoId
                    }

                    override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                        //更新完成状态
                        val finish = mTodoService.updateTodoFinish(params.safeNotEmptyGet("todoId"),
                                Date().time)
                        if (!finish){
                            responseBean.buildServiceError()
                        }
                    }

                }).process()
    }


    /**
     * 更新待办事项是否置顶
     */
    @Login
    @PutMapping("updateTop")
    fun updateTodoTop(): ResponseBean{
        return ResponseWrapper(request, object :ResponseHandle<HttpServletRequest>{
            override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                params.putAll(getParamsMap(request,"todoId", "todoTop"))
                checkEmpty(params, "todoId", "todoTop")

                tags["todo"] = checkTodo(params.safeNotEmptyGet("todoId")).apply {
                    this.todoTop = parseTodoTop(params["todoTop"])
                    checkFinish(this)
                }
            }

            override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                (tags["todo"] as TodoBean).apply {
                    val update = mTodoService.updateTodoTop(todoId, todoTop)
                    if (!update){
                        throw ServiceErrorException()
                    }
                }

            }

        }).process()
    }


    /**
     * 更新待办事项信息
     */
    @Login
    @PutMapping("updateInfo")
    fun updateTodoInfo() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "todoId", "todoContent", "todoTop",
                            "todoPlannedFinishDate", "todoRemind"))
                    checkEmpty(params,"todoId", "todoContent")
                    tags["todo"] = checkTodo(params.safeNotEmptyGet("todoId")).apply {
                        //检查完成状态
                        checkFinish(this)
                        //设置参数
                        this.todoContent = params.safeNotEmptyGet("todoContent")
                        notEmptyUse(params["todoTop"]){
                            this.todoTop = parseTodoTop(it)
                        }
                        notEmptyUse(params["todoPlannedFinishDate"]){
                            this.todoPlannedFinishDate = it.toTimeStamp { throw ParamsErrorException() }
                        }
                        notEmptyUse(params["todoRemind"]){
                            this.todoRemind = parseTodoRemind(it)
                        }
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    (tags["todo"] as TodoBean).apply {
                        val update = mTodoService.updateTodoInfo(todoId, todoContent, todoTop,
                                todoPlannedFinishDate, todoRemind)
                        if (!update){
                            throw ServiceErrorException()
                        }
                        responseBean.data = this
                    }
                }

            }).process()


    /**
     * 获取待办事项详情
     */
    @Login
    @GetMapping("get")
    fun findByTodoId() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val todoId = this.getParamsNotEmpty(request,"todoId")
                    tags["todo"] = checkTodo(todoId)
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = tags["todo"]
                }
            }).process()


    /**
     * 获取当前用户未删除的待办事项
     */
    @Login
    @GetMapping("all")
    fun findByUserId() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {

                    responseBean.data = mTodoService.findTodoByUserId(getUserInfoByToken()?.userId!!,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取当前用户逻辑删除的待办事项
     */
    @Login
    @GetMapping("allDel")
    fun findDelTodoByUser() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize"))
                    params.forEach {
                        checkInt(it.value)
                    }
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mTodoService.findDelTodoByUserId(getUserInfoByToken()?.userId!!,
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 获取某个类型的待办事项
     */
    @Login
    @GetMapping("type")
    fun findByType() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    params.putAll(getParamsMap(request, "pageNum", "pageSize", "todoType"))
                    params.forEach {
                        checkInt(it.value)
                    }
                    params["todoType"] = parseTodoType(params["todoType"]).toString()

                    val userId = getUserInfoByToken()!!.userId
                    params["userId"] = userId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    responseBean.data = mTodoService.findTodoByType(
                            params.safeNotEmptyGet("userId"),
                            params.safeNotEmptyGet("todoType").toInt(),
                            params.safeNotEmptyGet("pageNum").toInt(),
                            params.safeNotEmptyGet("pageSize").toInt())
                }

            }).process()


    /**
     * 删除某个待办事项
     */
    @Login
    @DeleteMapping("del")
    fun delTodoById() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val todoId = getParamsNotEmpty(request, "todoId")
                    val userId = getUserInfoByToken()!!.userId
                    if (mTodoService.findTodoById(todoId, userId) == null){
                        throw ResNotFoundException()
                    }
                    params["todoId"] = todoId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val del = mTodoService.realDelByTodoId(params.safeNotEmptyGet("todoId"))
                    if (!del){
                        responseBean.buildServiceError()
                    }
                }
            }).process()


    /**
     * 删除某个用户逻辑删除的待办事项
     */
    @Login
    @DeleteMapping("delAll")
    fun delTodoByUserId() = ResponseWrapper(request,
            object : ResponseHandle<HttpServletRequest>{
                override fun preHandleWithParams(request: HttpServletRequest, params: HashMap<String, String>, tags: HashMap<String, Any>) {
                    val userId = getUserInfoByToken()!!.userId
                    params["userId"] = userId
                }

                override fun handle(params: HashMap<String, String>, tags: HashMap<String, Any>, responseBean: ResponseBean) {
                    val del = mTodoService.realDelByUserId(params.safeNotEmptyGet("userId"))
                    if (!del){
                        responseBean.buildServiceError()
                    }
                }
            }).process()


    /**
     * 解析待办事项类型标记
     * @param todoType 待办事项类型文本标记
     */
    @TodoType
    private fun parseTodoType(todoType: String?): Int{
        try {
            val type = todoType?.toInt()
            return when(type){
                TODO_TYPE_NORMAL -> TODO_TYPE_NORMAL
                TODO_TYPE_STUDY -> TODO_TYPE_STUDY
                TODO_TYPE_WORK -> TODO_TYPE_WORK
                TODO_TYPE_PLAY -> TODO_TYPE_PLAY
                TODO_TYPE_FAMILY -> TODO_TYPE_FAMILY
                TODO_TYPE_OTHER -> TODO_TYPE_OTHER
                else -> throw ParamsErrorException()
            }
        }catch (e: Exception){
            throw ParamsErrorException()
        }
    }

    /**
     * 待办事项置顶标记
     * @param todoTop 待办事项置顶文本标记
     */
    @TodoListType
    private fun parseTodoTop(todoTop: String?): Int{
        try {
            val top = todoTop?.toInt()
            return when(top){
                TODO_LIST_NORMAL -> TODO_LIST_NORMAL
                TODO_LIST_TOP -> TODO_LIST_TOP
                else -> throw ParamsErrorException()
            }
        }catch (e: Exception){
            throw ParamsErrorException()
        }
    }

    /**
     * 待办事项提醒状态标记
     * @param todoRemind 待办事项提醒文本状态标记
     */
    @TodoRemindType
    private fun parseTodoRemind(todoRemind: String?): Int{
        try {
            val remind = todoRemind?.toInt()
            return when(remind){
                TODO_REMIND_NORMAL -> TODO_REMIND_NORMAL
                TODO_REMIND_REMIND -> TODO_REMIND_REMIND
                else -> throw ParamsErrorException()
            }
        }catch (e: Exception){
            throw ParamsErrorException()
        }
    }

    /**
     * 解析待办事项删除状态
     * @param todoDel 待办事项　文本删除状态
     */
    @TodoDelType
    private fun parseTodoDel(todoDel: String?): Int{
        try {
            val del = todoDel?.toInt()
            return when(del){
                TODO_DEL_NORMAL -> TODO_DEL_NORMAL
                TODO_DEL_DEL -> TODO_DEL_DEL
                else -> throw ParamsErrorException()
            }
        }catch (e: Exception){
            throw ParamsErrorException()
        }
    }

    /**
     * 检查待办事项是否合法
     */
    private fun checkTodo(todoId: String): TodoBean{
        val user = getUserInfoByToken()!!
        return mTodoService.findTodoById(todoId, user.userId) ?: throw ResNotFoundException()
    }

    /**
     * 判断待办事项是否已经完成
     */
    private fun checkFinish(todo: TodoBean){
        if (todo.todoFinishDate != null){
            //完成的待办事项不可修改
            throw ResNotFoundException()
        }
    }
}