package cat.deim.asm_22.p1_patinfly.data.repository

import android.content.Context
import android.util.Log
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.database.BikeDatasource
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO
import cat.deim.asm_22.p1_patinfly.data.datasource.local.BikeLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.BikeAPIDataSource
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.ServerStatus
import cat.deim.asm_22.p1_patinfly.domain.repository.IBikeRepository

/**
 * Implementación del repositorio de bicicletas que interactúa con la fuente de datos para gestionar
 * operaciones de almacenamiento, recuperación, actualización y eliminación de bicicletas.
 *
 * @param bikeDatasource Fuente de datos que se utilizará para realizar las operaciones (local, remota, etc.)
 */
class BikeRepository(
    private val bikeDatasource: BikeDatasource,
    private val apiDataSource: BikeAPIDataSource,
    private val bikeLocalDataSource: BikeLocalDataSource,
    private val context: Context) : IBikeRepository {
    companion object {
        /**
         * Metodo factoría para crear una instancia de [BikeRepository] configurada con la fuente de datos local.
         *
         * @param context Contexto de la aplicación necesario para inicializar el data source local.
         * @return Instancia de [BikeRepository] configurada con [BikeDatasource] como fuente de datos.
         */
        fun create(context: Context): IBikeRepository {
            val dataSource = AppDatabase.getDatabase(context).bikeDataSource()
            val api = BikeAPIDataSource.getInstance(context)
            val bikeLocalDataSource = BikeLocalDataSource.getInstance(context)
            return BikeRepository(dataSource, api, bikeLocalDataSource, context)
        }
    }
    suspend fun getFirstActiveBike(): Bike? {
        return getAll().firstOrNull { it.isActive }
    }

    /**
     * Inserta una bicicleta en la fuente de datos.
     *
     * @param bike Objeto [Bike] que representa la bicicleta a insertar.
     * @return [Boolean] Indica si la operación de inserción fue exitosa.
     */
    override suspend fun setBike(bike: Bike): Boolean {
        return try {
            bikeDatasource.save(BikeDTO.fromDomain(bike))
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getBike(): Bike? {
        val bikes = BikeLocalDataSource.getInstance(context).getBike()  // Recupera todas las bicicletas
        return bikes?.toDomain()  // Devuelve la primera bicicleta o null si no hay ninguna
    }


    override suspend fun getBike(uuid: String): Bike? {
        // 1. Buscar en la base de datos local
        val localBike = bikeDatasource.getByUUID(uuid)?.toDomain()
        if (localBike != null) {
            Log.d("BikeRepository", "Found bike $uuid in local DB")
            return localBike
        }

        // 2. Si no está en DB, buscar en API
        try {
            val apiBike = apiDataSource.getBikeById(uuid)?.toDomain()
            if (apiBike != null) {
                Log.d("BikeRepository", "Fetched bike $uuid from API, saving to DB")
                bikeDatasource.save(BikeDTO.fromDomain(apiBike))
                return apiBike
            }
        } catch (e: Exception) {
            Log.e("BikeRepository", "Error fetching bike $uuid from API", e)
        }

        // 3. (Opcional) Si no está en API, buscar en archivo local
        val localFileBike = BikeLocalDataSource.getInstance(context).getBike()?.toDomain()
        if (localFileBike != null && localFileBike.uuid == uuid) {
            Log.d("BikeRepository", "Found bike $uuid in local file, saving to DB")
            bikeDatasource.save(BikeDTO.fromDomain(localFileBike))
            return localFileBike
        }

        // 4. No encontrado en ninguna fuente
        Log.d("BikeRepository", "Bike $uuid not found in any data source")
        return null
    }

    /**
     * Actualiza una bicicleta en la fuente de datos.
     *
     * @param bike Objeto [Bike] que contiene los datos de la bicicleta a actualizar.
     * @return [Bike]? Objeto [Bike] actualizado, o null si la actualización no fue exitosa.
     */
    override suspend fun updateBike(bike: Bike): Bike? {
        return try {
            setBike(bike)
            bike
        } catch (e: Exception) {
            null
        }
    }


    /**
     * Elimina la bicicleta de la fuente de datos.
     *
     * @return [Bike]? Objeto [Bike] que fue eliminado, o null si la eliminación no fue exitosa.
     */
    override suspend fun deleteBike(): Bike? {
        val bike = getBike()
        bike?.let {
            bikeDatasource.delete(BikeDTO.fromDomain(it))
        }
        return bike
    }



    override suspend fun getAll(): Collection<Bike> {
        return try {
            // 1. Consultar primero la base de datos local
            val localBikes = bikeDatasource.getAll().map { it.toDomain() }
            if (localBikes.isNotEmpty()) {
                Log.d("BikeRepository", "Returning ${localBikes.size} bikes from local DB")
                return localBikes
            }

            // 2. Si no hay en DB, consultar la API REST
            val apiBikes = apiDataSource.getBikes().map { it.toDomain() }
            if (apiBikes.isNotEmpty()) {
                Log.d("BikeRepository", "Fetched ${apiBikes.size} bikes from API, saving to DB")

                // Guardar en DB para futuras consultas
                apiBikes.forEach { bike ->
                    bikeDatasource.save(BikeDTO.fromDomain(bike))
                }
                return apiBikes
            }

            // 3. (Opcional) Si no hay en API, consultar archivo local como último recurso
            val localFileBikes = BikeLocalDataSource.getInstance(context).getAll().map { it.toDomain() }
            if (localFileBikes.isNotEmpty()) {
                Log.d("BikeRepository", "Fetched ${localFileBikes.size} bikes from local file")

                // Guardar en DB para futuras consultas
                localFileBikes.forEach { bike ->
                    bikeDatasource.save(BikeDTO.fromDomain(bike))
                }
                return localFileBikes
            }

            // 4. Si no hay datos en ninguna fuente
            Log.d("BikeRepository", "No bikes found in any data source")
            emptyList()
        } catch (e: Exception) {
            Log.e("BikeRepository", "Error fetching bikes", e)
            emptyList()
        }
    }
    fun getBikesByCategory(category: String): List<Bike> {
        // Obtiene todas las bicicletas desde la fuente de datos local
        val bikeModels = bikeLocalDataSource.getAll()

        // Filtra las bicicletas que coinciden con la categoría especificada
        return bikeModels
            .filter { bikeModel ->
                // Asegúrate de que bikeModel.bikeType.name sea el atributo correcto para filtrar
                bikeModel.bikeType.name.equals(category, ignoreCase = true)
            }
            .map { bikeModel ->
                // Mapea el modelo a la entidad Bike
                Bike(
                    uuid = bikeModel.uuid,
                    name = bikeModel.name,
                    bikeType = bikeModel.bikeType.toDomain(), // Valor predeterminado en caso de que bikeType sea nulo
                    creationDate = bikeModel.creationDate,
                    lastMaintenanceDate = bikeModel.lastMaintenanceDate,
                    inMaintenance = bikeModel.inMaintenance,
                    isActive = bikeModel.isActive,
                    isDeleted = bikeModel.isDeleted,
                    batteryLevel = bikeModel.batteryLevel,
                    meters = bikeModel.meters,
                    isRented = bikeModel.isRented
                )
            }
    }
    suspend fun updateBikeStatus(bikeId: String, isActive: Boolean) {
        bikeDatasource.updateBikeStatus(bikeId, isActive)
    }

    suspend fun updateBikeRentStatus(bikeId: String, isRented: Boolean) {
        bikeDatasource.updateBikeRentStatus(bikeId, isRented)
    }

    suspend fun getActiveBikes(): List<BikeDTO> {
        return bikeDatasource.getAll().filter { it.isActive }
    }
    override suspend fun updateBikeRentStatus(bike: Bike): Boolean {
        return try {
            bikeDatasource.save(BikeDTO.fromDomain(bike))  // sobrescribe la entrada en la BD
            true
        } catch (e: Exception) {
            Log.e("BikeRepository", "Failed to update bike rent status", e)
            false
        }
    }

    override fun status(): ServerStatus {
        val status = apiDataSource.getStatus().toStatusDomain()
        return status
    }

}
