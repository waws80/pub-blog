package pw.androidthanatos.blog.entity

/**
 * 分页实体类
 */
data class PageBean<T>(var currentPage: Int = 1,
                       var pageSize: Int = 10,
                       var totalNum: Long = 0,
//                       var totalPage: Int = 0,
//                       var hasMore: Boolean = false,
//                       var startIndex: Int = 0,
                       var data: List<T> = emptyList()){

//    init {
//        hasMore = currentPage < totalPage
//    }
}