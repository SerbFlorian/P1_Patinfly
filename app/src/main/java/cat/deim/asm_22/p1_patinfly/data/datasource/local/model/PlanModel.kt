package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.PlanDomain
import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa un plan de precios con varias características asociadas.
 * Este modelo se utiliza para mapear los datos del plan desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property planId El identificador único del plan.
 * @property name Lista de nombres del plan en diferentes idiomas o contextos.
 * @property currency La moneda en la que se establece el precio del plan.
 * @property price El precio del plan en la moneda especificada.
 * @property isTaxable Indica si el plan es sujeto a impuestos.
 * @property description Lista de descripciones del plan en diferentes idiomas o contextos.
 * @property perKmPricing Lista de tarifas por kilómetro para el plan.
 * @property perMinPricing Lista de tarifas por minuto para el plan.
 */
data class Plan(
    @SerializedName("plan_id") val planId: String,
    val name: List<Name>,  // Mantenerlo como lista de 'Name'
    val currency: String,
    val price: Double,
    @SerializedName("is_taxable") val isTaxable: Boolean,
    val description: List<Description>,
    @SerializedName("per_km_pricing") val perKmPricing: List<PricingKmRate>,
    @SerializedName("per_min_pricing") val perMinPricing: List<PricingMinRate>
) {
    /**
     * Convierte el modelo de datos [Plan] a un objeto del dominio [PlanDomain].
     *
     * @return Objeto [PlanDomain] que representa el plan en el dominio de la aplicación.
     */
    fun toDomain(): PlanDomain {
        return PlanDomain(
            planId = planId,
            name = name.map { it.toDomain() },  // Convertir cada 'Name' a 'NameDomain'
            currency = currency,
            price = price,
            isTaxable = isTaxable,
            description = description.map { it.toDomain() },
            perKmPricing = perKmPricing.map { it.toDomain() },
            perMinPricing = perMinPricing.map { it.toDomain() }
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [PlanDomain] a un modelo de datos [Plan].
         *
         * @param domain Objeto del dominio [PlanDomain] que se desea convertir a modelo de datos.
         * @return [Plan] que representa el plan en el formato utilizado por la fuente de datos.
         */
        fun fromDomain(domain: PlanDomain): Plan {
            return Plan(
                planId = domain.planId,
                name = domain.name.map { Name.fromDomain(it) },  // Convertir cada 'NameDomain' a 'Name'
                currency = domain.currency,
                price = domain.price,
                isTaxable = domain.isTaxable,
                description = domain.description.map { Description.fromDomain(it) },
                perKmPricing = domain.perKmPricing.map { PricingKmRate.fromDomain(it) },
                perMinPricing = domain.perMinPricing.map { PricingMinRate.fromDomain(it) }
            )
        }
    }
}
