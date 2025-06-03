package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.PricingKmRateDomain

/**
 * Modelo de datos que representa la tarifa por kilómetro con un intervalo de aplicación.
 * Este modelo se utiliza para mapear los datos de la tarifa desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property start Valor inicial a partir del cual se empieza a aplicar la tarifa.
 * @property rate Tarifa aplicada por kilómetro.
 * @property interval Intervalo en kilómetros en el que se aplica la tarifa.
 */
data class PricingKmRate(
    val start: Double,
    val rate: Double,
    val interval: Int
) {

    /**
     * Convierte este modelo de datos en un objeto del dominio PricingKmRateDomain.
     *
     * @return Objeto del dominio que representa la tarifa por kilómetro dentro de la lógica de negocio de la aplicación.
     */
    fun toDomain(): PricingKmRateDomain {
        return PricingKmRateDomain(
            start = start,
            rate = rate,
            interval = interval
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio PricingKmRateDomain en un modelo de datos PricingKmRate.
         *
         * @param domain Objeto del dominio que se desea convertir.
         * @return Modelo de datos que representa la tarifa por kilómetro según la estructura de datos local.
         */
        fun fromDomain(domain: PricingKmRateDomain): PricingKmRate {
            return PricingKmRate(
                start = domain.start,
                rate = domain.rate,
                interval = domain.interval
            )
        }
    }
}
