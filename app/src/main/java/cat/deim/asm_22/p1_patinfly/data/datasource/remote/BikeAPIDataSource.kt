package cat.deim.asm_22.p1_patinfly.data.datasource.remote

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.model.BikeApiModel
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Clase singleton que gestiona el acceso remoto a la API de bicicletas.
 */
class BikeAPIDataSource private constructor() {
    private var context: Context? = null

    companion object {
        private const val BASE_URL = "https://patinfly.com/"
        private const val TAG = "BikeAPIDataSource"
        private lateinit var retrofit: APIService

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BikeAPIDataSource? = null

        /**
         * Devuelve la instancia única de BikeAPIDataSource.
         *
         * @param context Contexto de la aplicación
         * @return Instancia única de BikeAPIDataSource
         */
        fun getInstance(context: Context): BikeAPIDataSource =
            instance ?: synchronized(this) {
                instance ?: BikeAPIDataSource().also {
                    instance = it
                    it.context = context
                    it.initializeRetrofit()
                }
            }
    }

    /**
     * Inicializa la instancia de Retrofit con la configuración adecuada.
     */
    private fun initializeRetrofit() {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(APIService::class.java)
    }

    /**
     * Obtiene el estado actual del servidor desde la API.
     *
     * @return Modelo de estado del servidor (StatusApiModel)
     */
    fun getStatus(): BikeApiModel.StatusApiModel = runBlocking {
        withContext(Dispatchers.IO) {
            try {
                val status = retrofit.getServerStatus()
                Log.d(TAG, "Server status: $status")
                status
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching server status", e)
                BikeApiModel.StatusApiModel(
                    status = BikeApiModel.StatusInfo(
                        version = "0.0",
                        build = "0",
                        update = "",
                        name = "error"
                    )
                )
            }
        }
    }

    /**
     * Recupera todas las bicicletas desde la API remota.
     *
     * @return Lista de modelos BikeApiModel o lista vacía si ocurre un error
     */
    suspend fun getBikes(): List<BikeApiModel> {
        return try {
            val bikes = retrofit.getBikes()
            Log.d(TAG, "Fetched ${bikes.size} bikes from API")
            bikes
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching bikes", e)
            emptyList()
        }
    }

    /**
     * Recupera una bicicleta específica a partir de su identificador único.
     *
     * @param id Identificador de la bicicleta
     * @return Objeto BikeApiModel correspondiente o null si ocurre un error
     */
    suspend fun getBikeById(id: String): BikeApiModel? {
        return try {
            val bike = retrofit.getBikeById(id)
            Log.d(TAG, "Fetched bike: ${bike?.id}")
            bike
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching bike with id: $id", e)
            null
        }
    }

    /**
     * Recupera todas las bicicletas desde la API y las convierte al modelo de dominio.
     *
     * @return Colección de objetos Bike del dominio o lista vacía si ocurre un error
     */
    suspend fun getAll(): Collection<Bike> {
        return try {
            val apiBikes = getBikes()
            val domainBikes = apiBikes.map { it.toDomain() }

            Log.d(TAG, "Successfully fetched and converted ${domainBikes.size} bikes")
            domainBikes
        } catch (e: Exception) {
            Log.e(TAG, "Error in getAll()", e)
            emptyList()
        }
    }
}
