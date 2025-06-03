package cat.deim.asm_22.p1_patinfly.data.datasource

import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.SystemPricingPlanModel

/**
 * Interfaz que define las operaciones básicas de acceso a datos
 * para los planes de precios del sistema.
 */
interface ISystemPricingPlanDataSource {

    /**
     * Inserta un nuevo plan de precios en la fuente de datos.
     *
     * @param systemPricingPlanModel Modelo del plan de precios a insertar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insert(systemPricingPlanModel: SystemPricingPlanModel): Boolean

    /**
     * Inserta o actualiza un plan de precios en la fuente de datos.
     * Si el plan ya existe, se actualiza; si no, se inserta.
     *
     * @param systemPricingPlanModel Modelo del plan de precios a insertar o actualizar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insertOrUpdate(systemPricingPlanModel: SystemPricingPlanModel): Boolean

    /**
     * Obtiene un plan de precios cualquiera de la fuente de datos.
     * La implementación concreta decide qué plan devolver.
     *
     * @return Un modelo del plan de precios o null si no hay disponibles
     */
    fun getPlan(): SystemPricingPlanModel?

    /**
     * Obtiene un plan de precios a partir de su identificador único.
     *
     * @param planId Identificador del plan de precios
     * @return El modelo del plan de precios correspondiente o null si no se encuentra
     */
    fun getPlanById(planId: String): SystemPricingPlanModel?

    /**
     * Actualiza la información de un plan de precios en la fuente de datos.
     *
     * @param systemPricingPlanModel Modelo del plan de precios actualizado
     * @return El modelo del plan actualizado o null si no se pudo actualizar
     */
    fun update(systemPricingPlanModel: SystemPricingPlanModel): SystemPricingPlanModel?

    /**
     * Elimina un plan de precios de la fuente de datos.
     * La implementación concreta decide qué plan eliminar.
     *
     * @return El modelo del plan de precios eliminado o null si no se eliminó ninguno
     */
    fun delete(): SystemPricingPlanModel?

    /**
     * Obtiene todos los planes de precios almacenados en la fuente de datos.
     *
     * @return Colección de modelos de planes de precios
     */
    fun getAll(): Collection<SystemPricingPlanModel>
}
