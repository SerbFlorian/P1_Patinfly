package cat.deim.asm_22.p1_patinfly.data.datasource.database

import androidx.room.*
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.SystemPricingPlanDTO

/**
 * Interfaz DAO (Data Access Object) para acceder a los datos de los planes de precios del sistema.
 * Proporciona operaciones para insertar, actualizar, consultar y eliminar registros en la tabla systempricingplan.
 */
@Dao
interface SystemPricingPlanDataSource {

    /**
     * Inserta un plan de precios en la base de datos.
     * Si ya existe un plan con la misma versión, se reemplaza.
     *
     * @param plan Objeto SystemPricingPlanDTO que se desea guardar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(plan: SystemPricingPlanDTO)

    /**
     * Actualiza un plan de precios existente en la base de datos.
     *
     * @param plan Objeto SystemPricingPlanDTO con los datos actualizados.
     */
    @Update
    suspend fun update(plan: SystemPricingPlanDTO)

    /**
     * Recupera un plan de precios a partir de su versión.
     *
     * @param version Cadena que identifica la versión del plan.
     * @return Objeto SystemPricingPlanDTO correspondiente a la versión, o null si no se encuentra.
     */
    @Query("SELECT * FROM systempricingplan WHERE version = :version")
    suspend fun getByVersion(version: String): SystemPricingPlanDTO?

    /**
     * Recupera todos los planes de precios almacenados en la base de datos.
     *
     * @return Lista de objetos SystemPricingPlanDTO.
     */
    @Query("SELECT * FROM systempricingplan")
    suspend fun getAll(): List<SystemPricingPlanDTO>

    /**
     * Elimina un plan de precios de la base de datos.
     *
     * @param plan Objeto SystemPricingPlanDTO que se desea eliminar.
     */
    @Delete
    suspend fun delete(plan: SystemPricingPlanDTO)
}
