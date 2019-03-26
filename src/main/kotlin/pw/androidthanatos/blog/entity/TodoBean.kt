package pw.androidthanatos.blog.entity

import pw.androidthanatos.blog.common.annotation.TodoDelType
import pw.androidthanatos.blog.common.annotation.TodoListType
import pw.androidthanatos.blog.common.annotation.TodoRemindType
import pw.androidthanatos.blog.common.annotation.TodoType
import pw.androidthanatos.blog.common.contract.TODO_DEL_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_LIST_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_REMIND_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_TYPE_NORMAL
import java.util.*

/**
 * 待办事项实体类
 */
data class TodoBean(val todoId: String = "",
                    var todoTitle: String = "",
                    var todoContent: String = "",
                    val todoCreateDate: Long = Date().time,
                    var todoPlannedFinishDate: Long? = null,
                    @TodoType
                    var todoType: Int = TODO_TYPE_NORMAL,
                    var todoFinishDate: Long? = null,
                    @TodoListType
                    var todoTop: Int = TODO_LIST_NORMAL,
                    var todoUserId: String = "",
                    @TodoDelType
                    var todoDel: Int = TODO_DEL_NORMAL,
                    @TodoRemindType
                    var todoRemind: Int = TODO_REMIND_NORMAL)