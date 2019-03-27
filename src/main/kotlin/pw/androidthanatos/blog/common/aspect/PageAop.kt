package pw.androidthanatos.blog.common.aspect

import com.github.pagehelper.PageHelper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import pw.androidthanatos.blog.common.annotation.PagingQuery
import pw.androidthanatos.blog.common.exception.ParamsErrorException
import pw.androidthanatos.blog.common.response.ResponseBean
import pw.androidthanatos.blog.entity.PageBean

@Aspect
@Component
class PageAop {

    @Around("@annotation(pagingQuery)")
    fun pagingQuery(joinPoint: ProceedingJoinPoint, pagingQuery: PagingQuery): Any{

        val signature = joinPoint.signature as MethodSignature
        val returnType = signature.method.returnType
        if (returnType == List::class.java){
            val pageNumParameterName = pagingQuery.pageNum
            val pageSizeParameterName = pagingQuery.pageSize
            //获取request,从中获取分页参数
            val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            val request = requestAttributes.request
            val pageNum = request.getParameter(pageNumParameterName)
            val pageSize = request.getParameter(pageSizeParameterName)
            if (pageNum.isNullOrEmpty() || pageSize.isNullOrEmpty()){
                throw ParamsErrorException()
            }
            val page: Int
            val size: Int
            try {
                page = pageNum.toInt()
                size = pageSize.toInt()
            }catch (e: Exception){
                throw ParamsErrorException()
            }
            try {
                PageHelper.startPage<Any>(page,
                        size)
                val result = joinPoint.proceed() as List<*>
                return PageBean(page, size,result.size, result)
            }finally {
                if (PageHelper.getLocalPage<Any>() != null){
                    PageHelper.clearPage()
                }
            }
        }
        return joinPoint.proceed()
    }
}