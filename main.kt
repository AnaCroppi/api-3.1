
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// Modelo de datos
data class ProductResponse(
    @SerializedName("products") val products: List<Product>
)

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    @SerializedName("in_stock") val inStock: Boolean
)

// Función principal
fun main() = runBlocking {
    val apiUrl = "https://jsonkeeper.com/b/MX0A"

    val json = fetchJsonFromUrl(apiUrl)
    if (json != null) {
        val gson = Gson()
        val productResponse = gson.fromJson(json, ProductResponse::class.java)
        
        println("Listado de productos disponibles:")
        productResponse.products.forEach { product ->
            println("Producto: ${product.name} - Precio: ${product.price} ${product.currency}")
        }
    } else {
        println("Error al obtener los datos de la API.")
    }
}

// Función para obtener datos desde la URL
suspend fun fetchJsonFromUrl(apiUrl: String): String? = withContext(Dispatchers.IO) {
    try {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            return@withContext connection.inputStream.bufferedReader().readText()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return@withContext null
}
