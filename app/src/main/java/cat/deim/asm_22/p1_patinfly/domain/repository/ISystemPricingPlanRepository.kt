package cat.deim.asm_22.p1_patinfly.domain.repository

import cat.deim.asm_22.p1_patinfly.domain.models.SystemPricingPlan

/**
 * Interfaz que define las operaciones para gestionar los planes de precios del sistema.
 */
interface ISystemPricingPlanRepository {

    /**
     * Inserta un plan de precios en el repositorio.
     *
     * @param systemPricingPlan Objeto SystemPricingPlan que se desea insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    suspend fun setPricingPlan(systemPricingPlan: SystemPricingPlan): Boolean

    /**
     * Obtiene el plan de precios actual del sistema.
     *
     * @return SystemPricingPlan actual o null si no existe ninguno
     */
    suspend fun getPricingPlan(): SystemPricingPlan?

    /**
     * Actualiza un plan de precios existente en el repositorio.
     *
     * @param systemPricingPlan Objeto SystemPricingPlan con los datos actualizados
     * @return SystemPricingPlan actualizado o null si no se pudo actualizar
     */
    suspend fun updatePricingPlan(systemPricingPlan: SystemPricingPlan): SystemPricingPlan?

    /**
     * Elimina el plan de precios del repositorio.
     *
     * @return SystemPricingPlan eliminado o null si no existía ninguno
     */
    suspend fun deletePricingPlan(): SystemPricingPlan?
}
