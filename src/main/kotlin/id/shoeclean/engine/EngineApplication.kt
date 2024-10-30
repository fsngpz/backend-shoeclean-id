package id.shoeclean.engine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class EngineApplication

fun main(args: Array<String>) {
    runApplication<EngineApplication>(*args)
}
