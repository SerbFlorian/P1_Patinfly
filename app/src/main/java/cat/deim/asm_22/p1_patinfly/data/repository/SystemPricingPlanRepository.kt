package cat.deim.asm_22.p1_patinfly.data.repository

import android.content.Context
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.database.SystemPricingPlanDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.SystemPricingPlanDTO
import cat.deim.asm_22.p1_patinfly.data.datasource.local.SystemLocalPricingPlanDataSource
import cat.deim.asm_22.p1_patinfly.domain.models.SystemPricingPlan
import cat.deim.asm_22.p1_patinfly.domain.repository.ISystemPricingPlanRepository

/**
 * Implementación del repositorio de planes de precios del sistema que interactúa con la fuente de datos
 * para gestionar operaciones de almacenamiento, recuperación, actualización y eliminación de planes de precios.
 */
class SystemPricingPlanRepository(
    private val systemPricingPlanDataSource: SystemPricingPlanDataSource,
    private val context: Context
) : ISystemPricingPlanRepository {

    companion object {
        /**
         * Metodo factoría para crear una instancia de [SystemPricingPlanRepository] configurada con la fuente de datos local.
         *
         * @param context Contexto de la aplicación necesario para inicializar el data source local.
         * @return Instancia de [SystemPricingPlanRepository] configurada con [SystemPricingPlanDataSource] como fuente de datos.
         */
        fun create(context: Context): ISystemPricingPlanRepository {
            val dataSource = AppDatabase.getDatabase(context).systemPricingPlanDataSource()
            return SystemPricingPlanRepository(dataSource, context)
        }
    }

    /**
     * Inserta un plan de precios en la fuente de datos.
     *
     * @param systemPricingPlan Objeto [SystemPricingPlan] que representa el plan de precios a insertar.
     * @return [Boolean] Indica si la operación de inserción fue exitosa.
     */
    override suspend fun setPricingPlan(systemPricingPlan: SystemPricingPlan): Boolean {
        return try {
            systemPricingPlanDataSource.save(SystemPricingPlanDTO.fromDomain(systemPricingPlan))
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Recupera el plan de precios por su versión.
     *
     * @return Objeto [SystemPricingPlan] si se encuentra, o null si no existe un plan de precios.
     */
    override suspend fun getPricingPlan(): SystemPricingPlan? {
        val pricingPlanDTO = SystemLocalPricingPlanDataSource.getInstance(context).getPlan()
        return pricingPlanDTO?.toDomain()
    }

    /**
     * Recupera el plan de precios por la versión específica.
     *
     * @param version La versión del plan de precios que se desea obtener.
     * @return Objeto [SystemPricingPlan] correspondiente a la versión, o null si no existe.
     */
     suspend fun getPricingPlan(version: String): SystemPricingPlan? {
        val pricingPlanDTO = systemPricingPlanDataSource.getByVersion(version)?.toDomain()
        if(pricingPlanDTO != null) return pricingPlanDTO

        val remotePricingPlan = SystemLocalPricingPlanDataSource.getInstance(context).getPlan()?.toDomain()
        if(remotePricingPlan != null){
            systemPricingPlanDataSource.save(SystemPricingPlanDTO.fromDomain(remotePricingPlan))
            return remotePricingPlan
        }
        return null
     }

    /**
     * Actualiza un plan de precios en la fuente de datos.
     *
     * @param systemPricingPlan Objeto [SystemPricingPlan] que contiene los datos del plan de precios a actualizar.
     * @return [SystemPricingPlan]? Objeto actualizado, o null si la actualización no fue exitosa.
     */
    override suspend fun updatePricingPlan(systemPricingPlan: SystemPricingPlan): SystemPricingPlan? {
        return try {
            systemPricingPlanDataSource.update(SystemPricingPlanDTO.fromDomain(systemPricingPlan))
            systemPricingPlan
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Elimina un plan de precios de la fuente de datos.
     *
     * @return [SystemPricingPlan]? Objeto [SystemPricingPlan] que fue eliminado, o null si la eliminación no fue exitosa.
     */
    override suspend fun deletePricingPlan(): SystemPricingPlan? {
        val pricingPlanDTO = systemPricingPlanDataSource.getAll().firstOrNull()
        return pricingPlanDTO?.toDomain()?.apply {
            systemPricingPlanDataSource.delete(pricingPlanDTO)
        }
    }
}
