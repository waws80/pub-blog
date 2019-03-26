package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pw.androidthanatos.blog.common.annotation.*
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.exception.IllegalRequestException
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.exception.ResNotFoundException
import pw.androidthanatos.blog.common.extension.toTimeStamp
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.common.util.createId
import pw.androidthanatos.blog.entity.TodoBean
import pw.androidthanatos.blog.service.todo.TodoService
import java.text.DateFormat
import java.util.*

/**
 * 待办事项工具类
 */
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
    fun addTodo(): ResponseBean{
        var responseBean = ResponseBean()

        val paramsMap = convertParamsToMap("todoTitle",
                "todoContent","todoPlannedFinishDate", "todoType",
                "todoTop", "todoRemind")

        checkParamsByKey(paramsMap, "todoTitle", "todoContent", "todoPlannedFinishDate")
        val user = getUserInfoByToken()!!
        val userId = user.userId
        val todoId = createId()
        val todoBean = TodoBean(todoId, paramsMap["todoTitle"]!!, paramsMap["todoContent"]!!,
                Date().time, paramsMap["todoPlannedFinishDate"].toTimeStamp { throw ParamsErrorException() },
                parseTodoType(paramsMap["todoType"]), null,
                parseTodoTop(paramsMap["todoTop"]), userId, TODO_DEL_NORMAL, parseTodoRemind(paramsMap["todoRemind"]))

        val add = mTodoService.addTodo(todoBean)
        if (!add){
            responseBean = ResponseBean.serviceError()
        }
        return responseBean
    }


    /**
     * 更新待办事项删除状态
     */
    @Login
    @PutMapping("updateDelType")
    fun updateTodoDelType(): ResponseBean{
        var responseBean = ResponseBean()
        //校验参数
        val paramsMap = convertParamsToMap("todoId", "todoDel")
        checkParamsByKey(paramsMap, "todoId", "todoDel")
        //获取参数
        val todoId = paramsMap["todoId"] as String
        val todoDelType = parseTodoDel(paramsMap["todoDel"])

        //判断当前todo属于当前用户
        val currentUserId = getUserInfoByToken()?.userId!!
        val todo = mTodoService.findTodoById(todoId)
        //非法请求
        if (todo == null || todo.todoUserId != currentUserId){
            throw ResNotFoundException()
        }else{
            //更新待办事项
            val update = mTodoService.updateTodoDelStatus(todoId, todoDelType)
            if (!update){
                responseBean = ResponseBean.serviceError()
            }
        }
        return responseBean
    }


    /**
     * 完成了待办事项
     */
    @Login
    @PutMapping("finish")
    fun finishTodo(): ResponseBean{

        var responseBean = ResponseBean()
        //校验参数
        val todoId = getParamsNotEmpty("todoId")
        //判断当前todo属于当前用户
        val currentUserId = getUserInfoByToken()?.userId!!
        val todo = mTodoService.findTodoById(todoId)
        //非法请求
        if (todo == null || todo.todoUserId != currentUserId){
            throw ResNotFoundException()
        }else{
            //更新完成状态
            val finish = mTodoService.updateTodoFinish(todoId, Date().time)

            if (!finish){
                responseBean = ResponseBean.serviceError()
            }
        }

        return responseBean
    }


















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
}