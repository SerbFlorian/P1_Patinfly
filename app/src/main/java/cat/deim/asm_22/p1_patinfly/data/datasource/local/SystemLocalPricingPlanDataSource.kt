package cat.deim.asm_22.p1_patinfly.data.datasource.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import cat.deim.asm_22.p1_patinfly.data.datasource.ISystemPricingPlanDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.SystemPricingPlanModel
import com.google.gson.GsonBuilder
import java.util.concurrent.Executors

/**
 * Implementación de ISystemPricingPlanDataSource que utiliza un archivo JSON en la carpeta
 * de recursos raw para almacenar los planes de precios del sistema.
 */
class SystemLocalPricingPlanDataSource : ISystemPricingPlanDataSource {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SystemLocalPricingPlanDataSource? = null

        private const val TAG = "SystemPricingPlanSource"

        /**
         * Obtiene una instancia de SystemPricingPlanDataSource.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de SystemPricingPlanDataSource.
         */
        fun getInstance(context: Context): SystemLocalPricingPlanDataSource =
            instance ?: synchronized(this) {
                instance ?: SystemLocalPricingPlanDataSource().apply {
                    this.context = context
                    loadPricingPlanData()
                }.also { instance = it }
            }
    }

    private lateinit var context: Context
    private val pricingPlansMap: MutableMap<String, SystemPricingPlanModel> = HashMap()

    /**
     * Carga los datos de los planes de precios del sistema desde un archivo JSON en la carpeta de
     * recursos raw.
     */
    private fun loadPricingPlanData() {
        Executors.newSingleThreadExecutor().execute {
            try {
                val jsonData = AssetsProvider.getJsonDataFromRawAsset(context, "system_pricing_plans")
                if (jsonData.isNullOrEmpty()) {
                    Log.e(TAG, "No se pudo cargar el archivo JSON.")
                    return@execute
                }
                Log.d(TAG, "JSON cargado: $jsonData")

                val pricingPlan = parseJson(jsonData)
                pricingPlan?.data?.plans?.forEach { plan ->
                    save(pricingPlan.copy(data = pricingPlan.data.copy(plans = listOf(plan))))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar datos de planes de precios: ${e.message}")
            }
        }
    }


    /**
     * Parsea un JSON en un objeto SystemPricingPlanModel.
     *
     * @param json JSON a parsear.
     * @return Objeto SystemPricingPlanModel.
     */
    private fun parseJson(json: String): SystemPricingPlanModel? {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
            .create()
        return try {
            gson.fromJson(json, SystemPricingPlanModel::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error al parsear JSON: ${e.message}")
            null
        }
    }

    /**
     * Guarda un plan de precios en el mapa de planes de precios.
     *
     * @param plan Plan de precios a guardar.
     */
    private fun save(plan: SystemPricingPlanModel) {
        pricingPlansMap[plan.data.plans.first().planId] = plan
    }

    /**
     * Inserta un nuevo plan de precios en el mapa si no existe uno con el mismo ID.
     *
     * @param systemPricingPlanModel Modelo del plan de precios a insertar.
     * @return `true` si el plan se insertó correctamente, `false` si ya existe un plan con ese ID.
     */
    override fun insert(systemPricingPlanModel: SystemPricingPlanModel): Boolean {
        val planId = systemPricingPlanModel.data.plans.first().planId
        if (pricingPlansMap.containsKey(planId)) {
            Log.d(TAG, "El plan con ID $planId ya existe.")
            return false
        }
        pricingPlansMap[planId] = systemPricingPlanModel
        Log.d(TAG, "Plan de precios insertado: $systemPricingPlanModel")
        return true
    }

    /**
     * Inserta o actualiza un plan de precios en el mapa.
     *
     * @param systemPricingPlanModel Modelo del plan de precios a insertar o actualizar.
     * @return `true` si la operación fue exitosa.
     */
    override fun insertOrUpdate(systemPricingPlanModel: SystemPricingPlanModel): Boolean {
        val planId = systemPricingPlanModel.data.plans.first().planId
        pricingPlansMap[planId] = systemPricingPlanModel
        return true
    }

    /**
     * Obtiene el primer plan de precios disponible en el mapa.
     *
     * @return El primer plan de precios o `null` si no hay planes.
     */
    override fun getPlan(): SystemPricingPlanModel? {
        val plan = pricingPlansMap.values.firstOrNull()
        Log.d(TAG, "Obteniendo primer plan de precios: $plan")
        return plan
    }

    /**
     * Obtiene un plan de precios por su ID.
     *
     * @param planId ID del plan de precios a obtener.
     * @return El plan de precios correspondiente o `null` si no se encuentra.
     */
    override fun getPlanById(planId: String): SystemPricingPlanModel? {
        return pricingPlansMap[planId]
    }

    /**
     * Actualiza un plan de precios en el mapa.
     *
     * @param systemPricingPlanModel Modelo del plan de precios a actualizar.
     * @return El plan de precios actualizado o `null` si no existe un plan con ese ID.
     */
    override fun update(systemPricingPlanModel: SystemPricingPlanModel): SystemPricingPlanModel? {
        val planId = systemPricingPlanModel.data.plans.first().planId
        return if (pricingPlansMap.containsKey(planId)) {
            pricingPlansMap[planId] = systemPricingPlanModel
            systemPricingPlanModel
        } else {
            null
        }
    }

    /**
     * Elimina el primer plan de precios disponible en el mapa.
     *
     * @return El plan de precios eliminado o `null` si el mapa está vacío.
     */
    override fun delete(): SystemPricingPlanModel? {
        val firstPlan = pricingPlansMap.values.firstOrNull()
        if (firstPlan != null) {
            pricingPlansMap.remove(firstPlan.data.plans.first().planId)
        }
        return firstPlan
    }

    /**
     * Obtiene todos los planes de precios almacenados.
     *
     * @return Una colección de todos los planes de precios.
     */
    override fun getAll(): Collection<SystemPricingPlanModel> {
        return pricingPlansMap.values.toList()
    }
}
