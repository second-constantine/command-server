package by.next.way.cd.cs

import org.springframework.boot.SpringApplication

object CommandServerApp {

  @JvmStatic
  fun main(
    args: Array<String>
  ) {
    SpringApplication.run(SpringBootBotConfig::class.java, *args)
  }
}