package pw.androidthanatos.blog.service.todo

import pw.androidthanatos.blog.common.annotation.TodoDelType
import pw.androidthanatos.blog.common.annotation.TodoListType
import pw.androidthanatos.blog.common.annotation.TodoRemindType
import pw.androidthanatos.blog.common.annotation.TodoType
import pw.androidthanatos.blog.common.contract.*
import pw.androidthanatos.blog.entity.PageBean
import pw.androidthanatos.blog.entity.TodoBean

interface TodoService{

    /**
     * 添加一个待办事项
     * @param todoBean 待办事项实体类
     */
    fun addTodo(todoBean: TodoBean): Boolean

    /**
     * 更新待办事项状态（恢复或者逻辑删除）
     * @param todoId 待办事项id
     * @param todoDel 待办事项删除状态[TODO_DEL_NORMAL] [TODO_DEL_DEL]
     */
    fun updateTodoDelStatus(todoId: String, @TodoDelType todoDel: Int):Boolean

    /**
     * 将待办事项设为完成
     * @param todoId 待办事项id
     * @param finishDate 完成时间
     */
    fun updateTodoFinish(todoId: String, finishDate: Long): Boolean

    /**
     * 更新待办事项是否置顶
     * @param todoId　待办事项id
     * @param todoTop 待办事项置顶状态　[TODO_LIST_NORMAL] [TODO_LIST_TOP]
     */
    fun updateTodoTop(todoId: String, @TodoListType todoTop: Int): Boolean

    /**
     * 更新待办事项
     * @param todoId
     * @param todoContent
     * @param todoTop [TODO_LIST_NORMAL] [TODO_LIST_TOP]
     * @param todoPlannedFinishDate 待办事项计划完成日期
     * @param todoRemind 待办事项是否提醒　[TODO_REMIND_NORMAL] [TODO_REMIND_REMIND]
     */
    fun updateTodoInfo(todoId: String, todoContent: String,
                       @TodoListType todoTop: Int, todoPlannedFinishDate: Long?,
                       @TodoRemindType todoRemind: Int): Boolean

    /**
     * 查找待办事项
     * @param todoId 待办事项id
     */
    fun findTodoById(todoId: String): TodoBean?

    /**
     * 查找待办事项
     * @param todoUserId 待办事项所属人id
     */
    fun findTodoByUserId(todoUserId: String, page: Int, pageSize: Int): PageBean<TodoBean>


    /**
     * 查找用户的某个类型待办事项
     * @param todoUserId 用户id
     * @param todoType 待办事项类型[TODO_TYPE_NORMAL]
     * [TODO_TYPE_PLAY] [TODO_TYPE_WORK]
     * [TODO_TYPE_STUDY] [TODO_TYPE_FAMILY]
     * [TODO_TYPE_OTHER]
     */
    fun findTodoByType(todoUserId: String, @TodoType todoType: Int, page: Int, pageSize: Int): List<TodoBean>


    /**
     * 加密待办事项实体类
     * @param bean
     */
    fun encode(bean: TodoBean?): TodoBean?

    /**
     * 解密待办事项实体类
     * @param bean
     */
    fun decode(bean: TodoBean?): TodoBean?

}