package pw.androidthanatos.blog.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Component
import pw.androidthanatos.blog.common.annotation.TodoDelType
import pw.androidthanatos.blog.common.annotation.TodoListType
import pw.androidthanatos.blog.common.annotation.TodoRemindType
import pw.androidthanatos.blog.common.annotation.TodoType
import pw.androidthanatos.blog.entity.TodoBean
import java.sql.Date
import pw.androidthanatos.blog.common.contract.TODO_DEL_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_DEL_DEL
import pw.androidthanatos.blog.common.contract.TODO_TYPE_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_TYPE_STUDY
import pw.androidthanatos.blog.common.contract.TODO_TYPE_WORK
import pw.androidthanatos.blog.common.contract.TODO_TYPE_PLAY
import pw.androidthanatos.blog.common.contract.TODO_TYPE_FAMILY
import pw.androidthanatos.blog.common.contract.TODO_TYPE_OTHER
import pw.androidthanatos.blog.common.contract.TODO_LIST_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_LIST_TOP
import pw.androidthanatos.blog.common.contract.TODO_REMIND_NORMAL
import pw.androidthanatos.blog.common.contract.TODO_REMIND_REMIND

/**
 * 待办事项查询
 */
@Component
interface TodoMapper {

    /**
     * 添加一个待办事项
     * @param todoBean 待办事项实体类
     */
    @Insert("INSERT INTO tb_todo (todoId, todoTitle, todoContent, todoCreateDate, todoPlannedFinishDate," +
            "                     todoType, todoTop, todoUserId, todoRemind)" +
            "VALUES (#{todoId}, #{todoTitle}, #{todoContent}, #{todoCreateDate}, #{todoPlannedFinishDate}," +
            "                     #{todoType}, #{todoTop}, #{todoUserId}, #{todoRemind})")
    fun addTodo(todoBean: TodoBean): Int

    /**
     * 更新待办事项状态（恢复或者逻辑删除）
     * @param todoId 待办事项id
     * @param todoDel 待办事项删除状态[TODO_DEL_NORMAL] [TODO_DEL_DEL]
     */
    @Update("update tb_todo" +
            "set todoDel = #{todoDel}" +
            "where todoId = #{todoId}")
    fun updateTodoDelStatus(todoId: String, @TodoDelType todoDel: Int):Int

    /**
     * 将待办事项设为完成
     * @param todoId 待办事项id
     * @param finishDate 完成时间
     */
    @Update("update tb_todo" +
            "set finishDate = #{finishDate}" +
            "where todoId = #{todoId}")
    fun updateTodoFinish(todoId: String, finishDate: Long): Int

    /**
     * 更新待办事项是否置顶
     * @param todoId　待办事项id
     * @param todoTop 待办事项置顶状态　[TODO_LIST_NORMAL] [TODO_LIST_TOP]
     */
    @Update("update tb_todo" +
            "set todoTop = #{todoTop}" +
            "where todoId = #{todoId}")
    fun updateTodoTop(todoId: String, @TodoListType todoTop: Int): Int

    /**
     * 更新待办事项
     * @param todoId
     * @param todoContent
     * @param todoTop [TODO_LIST_NORMAL] [TODO_LIST_TOP]
     * @param todoPlannedFinishDate 待办事项计划完成日期
     * @param todoRemind 待办事项是否提醒　[TODO_REMIND_NORMAL] [TODO_REMIND_REMIND]
     */
    @Update("update tb_todo" +
            "set todoContent = #{todoContent}, todoTop = #{todoTop}, " +
            "todoPlannedFinishDate = #{todoPlannedFinishDate} todoRemind = #{todoRemind}" +
            "where todoId = #{todoId}")
    fun updateTodoInfo(todoId: String, todoContent: String,
                       @TodoListType todoTop: Int, todoPlannedFinishDate: Long,
                       @TodoRemindType todoRemind: Int): Int

    /**
     * 查找待办事项
     * @param todoId 待办事项id
     */
    @Select("select *" +
            "from tb_todo " +
            "where todoId = #{todoId}")
    fun findTodoById(todoId: String): TodoBean?

    /**
     * 查找待办事项
     * @param todoUserId 待办事项所属人id
     */
    @Select("select *" +
            "from tb_todo " +
            "where todoUserId = #{todoUserId}")
    fun findTodoByUserId(todoUserId: String): List<TodoBean>


    /**
     * 查找用户的某个类型待办事项
     * @param todoUserId 用户id
     * @param todoType 待办事项类型[TODO_TYPE_NORMAL]
     * [TODO_TYPE_PLAY] [TODO_TYPE_WORK]
     * [TODO_TYPE_STUDY] [TODO_TYPE_FAMILY]
     * [TODO_TYPE_OTHER]
     */
    @Select("select *" +
            "from tb_todo " +
            "where todoUserId = #{todoUserId} and todoType = #{todoType}")
    fun findTodoByType(todoUserId: String, @TodoType todoType: Int): List<TodoBean>

}