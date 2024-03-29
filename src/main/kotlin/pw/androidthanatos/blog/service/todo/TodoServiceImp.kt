package pw.androidthanatos.blog.service.todo

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pw.androidthanatos.blog.common.extension.decodeByBase64
import pw.androidthanatos.blog.common.extension.toBase64Encode
import pw.androidthanatos.blog.entity.PageBean
import pw.androidthanatos.blog.entity.TodoBean
import pw.androidthanatos.blog.mapper.TodoMapper
import java.sql.Date

/**
 * 待办事项服务实现类
 */
@Service
class TodoServiceImp  : TodoService{

    @Autowired
    private lateinit var mTodoMapper: TodoMapper

    override fun addTodo(todoBean: TodoBean): Boolean {
        return mTodoMapper.addTodo(todoBean.apply {
            encode(this)
        }) > 0
    }

    override fun updateTodoDelStatus(todoId: String, todoDel: Int): Boolean {
        return mTodoMapper.updateTodoDelStatus(todoId,todoDel) > 0
    }

    override fun updateTodoFinish(todoId: String, finishDate: Long): Boolean {
        return mTodoMapper.updateTodoFinish(todoId, finishDate) > 0
    }

    override fun updateTodoTop(todoId: String, todoTop: Int): Boolean {
        return mTodoMapper.updateTodoTop(todoId, todoTop) > 0
    }

    override fun updateTodoInfo(todoId: String, todoContent: String, todoTop: Int, todoPlannedFinishDate: Long?, todoRemind: Int): Boolean {
        return mTodoMapper.updateTodoInfo(todoId, todoContent.toBase64Encode(), todoTop, todoPlannedFinishDate, todoRemind) > 0
    }

    override fun findTodoById(todoId: String, todoUserId: String): TodoBean? {
        return mTodoMapper.findTodoById(todoId, todoUserId).apply {
            decode(this)
        }
    }

    override fun findTodoByUserId(todoUserId: String, page: Int, pageSize: Int): PageBean<TodoBean> {
        PageHelper.startPage<TodoBean>(page, pageSize)
        val list = mTodoMapper.findTodoByUserId(todoUserId).apply {
            forEach {
                decode(it)
            }
        }
        val count = mTodoMapper.findTodoByUserIdCount(todoUserId)
        return PageBean(page, pageSize, count, list)
    }

    override fun findDelTodoByUserId(todoUserId: String, page: Int, pageSize: Int): PageBean<TodoBean> {
        PageHelper.startPage<TodoBean>(page, pageSize)
        val list = mTodoMapper.findDelTodoByUserId(todoUserId).apply {
            forEach {
                decode(it)
            }
        }
        val count = mTodoMapper.findDelTodoByUserIdCount(todoUserId)
        return PageBean(page, pageSize, count, list)
    }


    override fun hasTodoTitle(todoTitle: String): Boolean {
        return mTodoMapper.findTodoByTitle(todoTitle.toBase64Encode()) > 0
    }

    override fun findTodoByType(todoUserId: String, todoType: Int, page: Int, pageSize: Int): PageBean<TodoBean> {
        PageHelper.startPage<TodoBean>(page, pageSize)
        val list = mTodoMapper.findTodoByType(todoUserId, todoType).apply {
            forEach {
                decode(it)
            }
        }
        val count = mTodoMapper.findTodoByTypeCount(todoUserId, todoType)
        return PageBean(page, pageSize, count, list)
    }

    override fun realDelByTodoId(todoId: String): Boolean {
        return mTodoMapper.realDelByTodoId(todoId) > 0
    }

    override fun realDelByUserId(todoUserId: String): Boolean {
        return mTodoMapper.realDelByUserId(todoUserId, 2) > 0
    }

    override fun encode(bean: TodoBean?): TodoBean? {
        return bean?.apply {
            this.todoTitle = this.todoTitle.toBase64Encode()
            this.todoContent = this.todoContent.toBase64Encode()
        }
    }

    override fun decode(bean: TodoBean?): TodoBean? {
        return bean?.apply {
            this.todoTitle = this.todoTitle.decodeByBase64()
            this.todoContent = this.todoContent.decodeByBase64()
        }
    }
}