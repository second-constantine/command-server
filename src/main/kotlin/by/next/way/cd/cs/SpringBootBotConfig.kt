package by.next.way.cd.cs

import com.google.gson.Gson
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource

@PropertySource("classpath:app.properties")
@SpringBootApplication
@ComponentScan("by.next.way")
class SpringBootBotConfig : ApplicationRunner {

  @Bean
  fun customOpenAPI(): OpenAPI? {
    return OpenAPI().addServersItem(Server().url("http://localhost:8889"))
      .info(
        Info().title("Command Server API").version("V1.0")
      )
  }

  @Bean
  fun gson() = Gson()

  override fun run(
    args: ApplicationArguments?
  ) {
  }
}
