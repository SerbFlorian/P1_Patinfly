package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.SystemPricingPlan
import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa un plan de precios del sistema, con información adicional
 * como la fecha de la última actualización, tiempo de vida (TTL), versión y los datos de planes asociados.
 * Este modelo se utiliza para mapear los datos del sistema de precios desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property lastUpdated Fecha de la última actualización de los datos.
 * @property ttl Tiempo de vida en segundos para los datos.
 * @property version La versión del sistema de precios.
 * @property data Los datos de los planes de precios.
 */
data class SystemPricingPlanModel(
    @SerializedName("last_updated") val lastUpdated: String,
    val ttl: Int,
    val version: String,
    @SerializedName("data") val data: DataPlan
) {
    /**
     * Convierte el modelo de datos [SystemPricingPlanModel] a un objeto del dominio [SystemPricingPlan].
     *
     * @return Objeto [SystemPricingPlan] que representa el plan de precios del sistema en el dominio de la aplicación.
     */
    fun toDomain(): SystemPricingPlan {
        return SystemPricingPlan(
            lastUpdated = lastUpdated,
            ttl = ttl,
            version = version,
            data = data.toDomain()
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [SystemPricingPlan] a un modelo de datos [SystemPricingPlanModel].
         *
         * @param domain Objeto del dominio [SystemPricingPlan] que se desea convertir a modelo de datos.
         * @return [SystemPricingPlanModel] que representa el sistema de precios en el formato utilizado por la fuente de datos.
         */
        fun fromDomain(domain: SystemPricingPlan): SystemPricingPlanModel {
            return SystemPricingPlanModel(
                lastUpdated = domain.lastUpdated,
                ttl = domain.ttl,
                version = domain.version,
                data = DataPlan.fromDomain(domain.data)
            )
        }
    }
}
