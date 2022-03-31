package no.nav.yrkesskade.skadeforklaring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SkadeforklaringStarter

fun main(args: Array<String>) {
    runApplication<SkadeforklaringStarter>(*args)
}
