package cat.deim.asm_22.p1_patinfly.domain.models

/**
 * Representa el plan de precios del sistema con información de actualización,
 * tiempo de vida y los datos específicos de los planes.
 *
 * @property lastUpdated Fecha de la última actualización del plan de precios
 * @property ttl Tiempo en segundos que indica la validez de la información (time to live)
 * @property version Versión del plan de precios
 * @property data Datos que contienen la lista de planes disponibles
 */
data class SystemPricingPlan(
    val lastUpdated: String,
    val ttl: Int,
    val version: String,
    val data: DataPlanDomain
)

/**
 * Contiene la lista de planes disponibles en el sistema.
 *
 * @property plans Lista de planes de precios
 */
data class DataPlanDomain(
    val plans: List<PlanDomain>
)

/**
 * Representa un plan de precios específico.
 *
 * @property planId Identificador único del plan
 * @property name Lista de nombres del plan en distintos idiomas
 * @property currency Moneda en la que se expresa el precio
 * @property price Precio base del plan
 * @property isTaxable Indica si el plan está sujeto a impuestos
 * @property description Lista de descripciones del plan en distintos idiomas
 * @property perKmPricing Lista de tarifas aplicables por kilómetro
 * @property perMinPricing Lista de tarifas aplicables por minuto
 */
data class PlanDomain(
    val planId: String,
    val name: List<NameDomain>,
    val currency: String,
    val price: Double,
    val isTaxable: Boolean,
    val description: List<DescriptionDomain>,
    val perKmPricing: List<PricingKmRateDomain>,
    val perMinPricing: List<PricingMinRateDomain>
)

/**
 * Representa el nombre de un plan en un idioma específico.
 *
 * @property text Texto con el nombre del plan
 * @property language Código o nombre del idioma
 */
data class NameDomain(
    val text: String,
    val language: String
)

/**
 * Representa la descripción de un plan en un idioma específico.
 *
 * @property text Texto con la descripción del plan
 * @property language Código o nombre del idioma
 */
data class DescriptionDomain(
    val text: String,
    val language: String
)

/**
 * Representa una tarifa aplicada por kilómetro dentro de un rango específico.
 *
 * @property start Valor de inicio del tramo (kilómetros) para esta tarifa
 * @property rate Precio aplicado por kilómetro en este tramo
 * @property interval Intervalo en kilómetros para la aplicación de la tarifa
 */
data class PricingKmRateDomain(
    val start: Double,
    val rate: Double,
    val interval: Int
)

/**
 * Representa una tarifa aplicada por minuto dentro de un rango específico.
 *
 * @property start Valor de inicio del tramo (minutos) para esta tarifa
 * @property rate Precio aplicado por minuto en este tramo
 * @property interval Intervalo en minutos para la aplicación de la tarifa
 */
data class PricingMinRateDomain(
    val start: Double,
    val rate: Double,
    val interval: Int
)
