package pw.androidthanatos.blog

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["pw.androidthanatos.blog"])
@MapperScan(basePackages = ["pw.androidthanatos.blog.mapper"])
class BlogApplication

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}
