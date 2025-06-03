package cat.deim.asm_22.p1_patinfly.data.datasource.remote

import cat.deim.asm_22.p1_patinfly.data.datasource.remote.model.BikeApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    /**
     * Obtiene el estado del servidor
     * GET https://patinfly.com/status
     *
     * @return StatusApiModel con información de versión y estado del servidor
     */
    @GET("status")
    suspend fun getServerStatus(): BikeApiModel.StatusApiModel

    /**
     * Obtiene todas las bicicletas disponibles
     * GET https://patinfly.com/bikes
     *
     * @return Lista de BikeApiModel con información de las bicicletas
     */
    @GET("bikes")
    suspend fun getBikes(): List<BikeApiModel>

    /**
     * Obtiene una bicicleta específica por su ID
     * GET https://patinfly.com/bikes/{id}
     *
     * @param id Identificador único de la bicicleta
     * @return BikeApiModel con la información de la bicicleta solicitada o null si no existe
     */
    @GET("bikes/{id}")
    suspend fun getBikeById(@Path("id") id: String): BikeApiModel?
}