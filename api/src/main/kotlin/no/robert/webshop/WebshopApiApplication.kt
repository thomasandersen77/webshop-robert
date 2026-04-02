package no.robert.webshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebshopApiApplication

fun main(args: Array<String>) {
    runApplication<WebshopApiApplication>(*args)
}
