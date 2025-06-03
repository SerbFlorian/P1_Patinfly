package cat.deim.asm_22.p1_patinfly.data.datasource.database

import androidx.room.*
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO

/**
 * Interfaz DAO (Data Access Object) para acceder a los datos de la entidad [BikeDTO].
 * Proporciona métodos para insertar, consultar, actualizar y eliminar bicicletas en la base de datos.
 */
@Dao
interface BikeDatasource {

    /**
     * Inserta una bicicleta en la base de datos.
     * Si ya existe una con el mismo UUID, se reemplaza.
     *
     * @param bike Objeto [BikeDTO] a insertar o reemplazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(bike: BikeDTO)

    /**
     * Obtiene una bicicleta a partir de su UUID.
     *
     * @param uuid UUID de la bicicleta.
     * @return Objeto [BikeDTO] si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM bike WHERE uuid = :uuid")
    suspend fun getByUUID(uuid: String): BikeDTO?

    /**
     * Obtiene todas las bicicletas almacenadas en la base de datos.
     *
     * @return Lista de objetos [BikeDTO].
     */
    @Query("SELECT * FROM bike")
    suspend fun getAll(): List<BikeDTO>

    /**
     * Elimina una bicicleta de la base de datos.
     *
     * @param bike Objeto [BikeDTO] que se desea eliminar.
     */
    @Delete
    suspend fun delete(bike: BikeDTO)

    /**
     * Actualiza los datos de una bicicleta existente.
     *
     * @param bike Objeto [BikeDTO] con los datos actualizados.
     */
    @Update
    suspend fun updateBike(bike: BikeDTO)

    /**
     * Actualiza el estado de actividad de una bicicleta.
     *
     * @param bikeId UUID de la bicicleta.
     * @param isActive Nuevo valor del estado de actividad.
     */
    @Query("UPDATE bike SET isActive = :isActive WHERE uuid = :bikeId")
    suspend fun updateBikeStatus(bikeId: String, isActive: Boolean)

    /**
     * Actualiza el estado de alquiler de una bicicleta.
     *
     * @param bikeId UUID de la bicicleta.
     * @param isRented Nuevo valor del estado de alquiler.
     */
    @Query("UPDATE bike SET isRented = :isRented WHERE uuid = :bikeId")
    suspend fun updateBikeRentStatus(bikeId: String, isRented: Boolean)

    /**
     * Obtiene una bicicleta por su UUID.
     * Similar a [getByUUID] pero no devuelve null.
     *
     * @param bikeId UUID de la bicicleta.
     * @return Objeto [BikeDTO] correspondiente al UUID.
     */
    @Query("SELECT * FROM bike WHERE uuid = :bikeId")
    suspend fun getBikeById(bikeId: String): BikeDTO
}
