package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.DataPlanDomain
import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa un conjunto de planes en el sistema.
 * Este modelo es utilizado para mapear los datos de los planes desde una fuente de datos externa
 * (como JSON) hacia el dominio de la aplicación.
 *
 * @property plans Lista de los planes disponibles.
 */
data class DataPlan(
    @SerializedName("plans") val plans: List<Plan>
) {

    /**
     * Convierte este modelo de datos [DataPlan] en un objeto del dominio [DataPlanDomain].
     *
     * @return Objeto del dominio [DataPlanDomain] equivalente.
     */
    fun toDomain(): DataPlanDomain {
        return DataPlanDomain(
            plans = plans.map { it.toDomain() }
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [DataPlanDomain] en un modelo de datos [DataPlan].
         *
         * @param domain Objeto del dominio que se desea convertir.
         * @return Objeto [DataPlan] que representa los datos del dominio en el formato de fuente externa.
         */
        fun fromDomain(domain: DataPlanDomain): DataPlan {
            return DataPlan(
                plans = domain.plans.map { Plan.fromDomain(it) }
            )
        }
    }
}
