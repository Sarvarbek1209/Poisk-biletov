import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import org.json.JSONObject

fun main() {
    val url = "https://run.mocky.io/v3/38b5205d-1a3d-4c2f-9d77-2f9d1ef01a4a"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).execute().use { response: Response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println("Response Code: ${response.code}")
        println("Response Body: ${response.body?.string()}")

        // Получаем ответ в виде строки

        }
    }


