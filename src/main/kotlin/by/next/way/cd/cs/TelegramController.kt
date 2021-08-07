package by.next.way.cd.cs

import com.google.gson.Gson
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@Tag(name = "telegram notify api", description = "Нотификации через телеграмм")
@RestController
class TelegramController(
  private val gson: Gson,
  private val client: CloseableHttpClient = HttpClientBuilder.create().build()
) {

  @Operation(description = "Отправка результата")
  @PostMapping("api/notify/{telegram_token}/{chat_id}")
  fun sendMessage(
    @PathVariable("telegram_token") token: String,
    @PathVariable("chat_id") chatId: Long,
    @RequestBody text: String
  ): String {
    return sendPost(
      url = "https://api.telegram.org/bot$token/sendMessage",
      json = gson.toJson(
        TelegramMessage(
          chat_id = chatId,
          text = text
        )
      )
    )
  }

  fun sendPost(
    url: String,
    json: String? = null
  ) = try {
    val request = HttpPost(url)
    if (json != null) {
      request.entity = StringEntity(json, "utf-8")
    }
    request.setHeader("Content-type", "application/json; charset=utf-8")
    val response = client.execute(request)
    val result = EntityUtils.toString(response?.entity)
    response.close()
    result ?: ""
  } catch (e: IOException) {
    ""
  }
}