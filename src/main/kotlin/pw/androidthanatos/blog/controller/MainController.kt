package pw.androidthanatos.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw.androidthanatos.blog.annotation.Login
import java.lang.Exception

/**
 * 主控制器
 */
@RestController
@RequestMapping("main")
class MainController {


    @GetMapping
    @Login
    fun index() :Array<String>{
        return arrayOf("hello word!","asdsadsad")
    }
}