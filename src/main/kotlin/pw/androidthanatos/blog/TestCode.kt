package pw.androidthanatos.blog

import pw.androidthanatos.blog.common.util.log
import java.net.URL
import java.util.*

fun main(){


    log.info(UUID.randomUUID().toString().replace("-",""))

    URL("http://127.0.0.1:8080").openConnection().getInputStream()
}