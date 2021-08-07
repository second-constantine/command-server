package by.next.way.cd.cs

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletResponse

@Tag(name = "command distributor server api", description = "Командный сервер")
@RestController
@CrossOrigin
class CommandController(
  private val commandCache: Cache<String, String> = Caffeine.newBuilder()
    .expireAfterWrite(1, TimeUnit.HOURS)
    .maximumSize(100)
    .build(),
  private val resultCache: Cache<String, String> = Caffeine.newBuilder()
    .expireAfterWrite(1, TimeUnit.HOURS)
    .maximumSize(100)
    .build(),
  private val results: MutableMap<String, DeferredResult<String>> = ConcurrentHashMap()
) {

  @Operation(description = "Отправка команды")
  @PostMapping("api/send-command/{token}")
  fun sendCommand(
    @PathVariable("token") token: String,
    @RequestBody command: String
  ): String? {
    commandCache.put(token, command)
    val deferredResult = results[token]
    return if (deferredResult != null) {
      log.info("[$token] Send command: $command")
      deferredResult.setResult(command)
      command
    } else {
      log.warn("[$token] Token not found! $command")
      null
    }
  }

  @Operation(description = "Отправка результата")
  @PostMapping("api/send-result/{token}")
  fun sendResult(
    @PathVariable("token") token: String,
    @RequestBody commandResult: String
  ): String {
    resultCache.put(token, commandResult)
    log.info("[$token] Send result: $commandResult")
    return commandResult
  }

  @Operation(description = "Получение результата")
  @GetMapping("api/get-result/{token}")
  fun getResult(
    @PathVariable("token") token: String
  ): String? {
    val result = resultCache.getIfPresent(token)
    log.info("[$token] Get result: $result")
    return result
  }

  @Operation(description = "Получение команд по токену")
  @GetMapping("api/get-commands/{token}")
  fun getCommands(
    @PathVariable("token") token: String
  ): DeferredResult<String> {
    val deferredResult = DeferredResult<String>(TIME_OUT, "")
    results[token] = deferredResult
    log.info("[$token] Get commands event..")
    return deferredResult
  }

  @Operation(description = "Главная страница (редирект на сваггер)")
  @GetMapping("/")
  fun index(response: HttpServletResponse) {
    response.sendRedirect("/swagger-ui.html")
  }

  companion object {
    const val TIME_OUT = 30_000L
    private val log = LogManager.getLogger()
  }
}