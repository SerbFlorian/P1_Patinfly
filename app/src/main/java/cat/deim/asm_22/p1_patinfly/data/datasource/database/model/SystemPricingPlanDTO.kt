package cat.deim.asm_22.p1_patinfly.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cat.deim.asm_22.p1_patinfly.domain.models.SystemPricingPlan
import cat.deim.asm_22.p1_patinfly.domain.models.DataPlanDomain
import com.google.gson.Gson

/**
 * Clase de datos que representa un plan de precios del sistema almacenado en la base de datos local.
 * Utiliza la anotación @Entity para su uso con Room.
 *
 * @property version Versión del plan de precios.
 * @property lastUpdated Fecha de la última actualización del plan.
 * @property ttl Tiempo de vida del plan (time-to-live), expresado en segundos.
 * @property dataJson Cadena en formato JSON que contiene los datos del plan de precios.
 */
@Entity(tableName = "systempricingplan")
data class SystemPricingPlanDTO(
    @PrimaryKey val version: String,
    val lastUpdated: String,
    val ttl: Int,
    val dataJson: String
) {
    /**
     * Convierte el modelo de datos SystemPricingPlanDTO a un objeto del dominio SystemPricingPlan.
     * Utiliza Gson para deserializar el contenido JSON a un objeto del dominio.
     *
     * @return Objeto SystemPricingPlan que representa el plan de precios en el dominio de la aplicación.
     */
    fun toDomain(): SystemPricingPlan {
        val gson = Gson()
        val dataPlanDomain = gson.fromJson(dataJson, DataPlanDomain::class.java)
        return SystemPricingPlan(
            version = version,
            lastUpdated = lastUpdated,
            ttl = ttl,
            data = dataPlanDomain
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio SystemPricingPlan a un modelo de datos SystemPricingPlanDTO.
         * Serializa el campo data a una cadena JSON para su almacenamiento.
         *
         * @param systemPricingPlan Objeto del dominio que se desea convertir.
         * @return Objeto SystemPricingPlanDTO para ser almacenado en la base de datos.
         */
        fun fromDomain(systemPricingPlan: SystemPricingPlan): SystemPricingPlanDTO {
            val gson = Gson()
            val dataJson = gson.toJson(systemPricingPlan.data)
            return SystemPricingPlanDTO(
                version = systemPricingPlan.version,
                lastUpdated = systemPricingPlan.lastUpdated,
                ttl = systemPricingPlan.ttl,
                dataJson = dataJson
            )
        }
    }
}
