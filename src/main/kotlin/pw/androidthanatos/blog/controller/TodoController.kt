package pw.androidthanatos.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.common.annotation.*
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.common.exception.ParamsErrorException
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
        val responseBean = ResponseBean()

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
            responseBean.code = CODE_SERVICE_ERROR
            responseBean.msg = MSG_SERVICE_ERROR
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