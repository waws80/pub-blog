package pw.androidthanatos.blog.controller

import pw.androidthanatos.blog.common.exception.ParamsErrorException

/**
 * 控制器基类
 */
abstract class BaseController {

    /**
     * 检查参数是否合法
     */
    protected fun checkParams(vararg params: Any?){
        params.forEach {
            if (null == it){
                if (it is String){
                    if ((it as String).isNotEmpty()){
                        return
                    }
                }
                throw ParamsErrorException()
            }
        }

    }
}