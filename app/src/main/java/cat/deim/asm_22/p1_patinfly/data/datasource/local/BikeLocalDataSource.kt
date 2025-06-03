package cat.deim.asm_22.p1_patinfly.data.datasource.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import cat.deim.asm_22.p1_patinfly.data.datasource.IBikeDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.BikeModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

/**
 * Clase que implementa la fuente de datos local para las bicicletas.
 * Esta clase maneja el acceso y almacenamiento de los datos de bicicletas de manera local.
 */
class BikeLocalDataSource private constructor(): IBikeDataSource {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BikeLocalDataSource? = null

        private const val TAG = "BikeLocalDataSource"

        /**
         * Obtiene la instancia singleton de BikeLocalDataSource.
         *
         * @param context Contexto de la aplicación para acceder a los recursos.
         * @return Instancia de BikeLocalDataSource.
         */
        fun getInstance(context: Context): BikeLocalDataSource =
            instance ?: synchronized(this) {
                instance ?: BikeLocalDataSource().also {
                    instance = it
                    it.context = context
                    it.loadBikeData()
                }
            }
    }

    private var context: Context? = null
    private val bikesMap: MutableMap<String, BikeModel> = HashMap()

    /**
     * Carga los datos de bicicletas desde los recursos locales (archivo JSON).
     * Procesa el archivo JSON y guarda las bicicletas en la memoria local.
     */
    private fun loadBikeData() {
        context?.let { context ->
            try {
                val jsonData = AssetsProvider.getJsonDataFromRawAsset(context, "bikes")
                Log.d(TAG, "JSON leído: $jsonData")

                if (jsonData.isNullOrEmpty()) {
                    Log.e(TAG, "El JSON está vacío o no se pudo leer.")
                    return
                }

                val bikes = parseJson(jsonData)
                if (bikes.isNullOrEmpty()) {
                    Log.e(TAG, "No se pudieron parsear las bicicletas o la lista está vacía.")
                    return
                }

                bikes.forEach { bikeModel ->
                    save(bikeModel)
                    Log.d(TAG, "Bicicleta guardada: ${bikeModel.name} (UUID: ${bikeModel.uuid})")
                }

                Log.d(TAG, "Total bicicletas guardadas: ${bikesMap.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar datos de bicicletas: ${e.message}")
                e.printStackTrace()
            }
        } ?: run {
            Log.e(TAG, "El contexto es nulo.")
        }
    }

    /**
     * Parsea el JSON recibido a una lista de objetos BikeModel.
     *
     * @param json Cadena JSON que representa los datos de bicicletas.
     * @return Lista de objetos BikeModel o null si ocurre un error al parsear.
     */
    private fun parseJson(json: String): List<BikeModel>? {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        return try {
            val jsonObject = gson.fromJson(json, JsonObject::class.java)
            val bikesArray = jsonObject.getAsJsonArray("bike")
            val bikeListType = object : TypeToken<List<BikeModel>>() {}.type
            gson.fromJson(bikesArray, bikeListType)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Error al parsear JSON: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado al parsear JSON: ${e.message}")
            null
        }
    }

    /**
     * Guarda una bicicleta en el mapa de bicicletas.
     *
     * @param bikeModel El modelo de bicicleta a guardar.
     */
    private fun save(bikeModel: BikeModel) {
        try {
            bikesMap[bikeModel.uuid] = bikeModel
            Log.d(TAG, "Bicicleta guardada: ${bikeModel.name} (UUID: ${bikeModel.uuid})")
            Log.d(TAG, "Total bicicletas guardadas: ${bikesMap.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar bicicleta: ${e.message}")
        }
    }

    /**
     * Inserta una nueva bicicleta si no existe una con el mismo UUID.
     *
     * @param bikeModel El modelo de bicicleta a insertar.
     * @return true si la bicicleta fue insertada, false si ya existía una con el mismo UUID.
     */
    override fun insert(bikeModel: BikeModel): Boolean {
        return if (!bikesMap.containsKey(bikeModel.uuid)) {
            bikesMap[bikeModel.uuid] = bikeModel
            true
        } else {
            false
        }
    }

    /**
     * Inserta o actualiza una bicicleta en el mapa de bicicletas.
     *
     * @param bikeModel El modelo de bicicleta a insertar o actualizar.
     * @return true siempre.
     */
    override fun insertOrUpdate(bikeModel: BikeModel): Boolean {
        bikesMap[bikeModel.uuid] = bikeModel
        return true
    }

    /**
     * Obtiene una bicicleta de forma aleatoria del mapa de bicicletas.
     *
     * @return Una bicicleta si existe, o null si no hay bicicletas.
     */
    override fun getBike(): BikeModel? {
        return bikesMap.values.firstOrNull()
    }

    /**
     * Obtiene una bicicleta por su UUID.
     *
     * @param uuid El UUID de la bicicleta a buscar.
     * @return La bicicleta correspondiente al UUID, o null si no se encuentra.
     */
    override fun getBikeById(uuid: String): BikeModel? {
        return bikesMap[uuid]
    }

    /**
     * Obtiene todas las bicicletas almacenadas en el mapa.
     *
     * @return Una colección de todos los modelos de bicicletas almacenadas.
     */
    override fun getAll(): Collection<BikeModel> = bikesMap.values

    /**
     * Actualiza una bicicleta en el mapa de bicicletas.
     *
     * @param bikeModel El modelo de bicicleta con los nuevos datos.
     * @return El modelo actualizado de bicicleta, o null si no existe en el mapa.
     */
    override fun update(bikeModel: BikeModel): BikeModel? {
        return bikesMap[bikeModel.uuid]?.apply {
            bikesMap[bikeModel.uuid] = bikeModel
        }
    }
    fun getAll(bikeTypeFilter: String? = null): Collection<BikeModel> {
        return if (bikeTypeFilter.isNullOrEmpty()) {
            // Si no hay filtro, devuelve todas las bicicletas
            bikesMap.values
        } else {
            // Si hay filtro, filtra por el tipo de bicicleta
            bikesMap.values.filter { bikeModel ->
                bikeModel.bikeType.name.equals(bikeTypeFilter, ignoreCase = true)
            }
        }
    }

    /**
     * Elimina la primera bicicleta del mapa de bicicletas.
     *
     * @return La bicicleta eliminada, o null si no hay bicicletas para eliminar.
     */
    override fun delete(): BikeModel? {
        return bikesMap.values.firstOrNull()?.also { bikesMap.remove(it.uuid) }
    }
}
