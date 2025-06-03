package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.DescriptionDomain

/**
 * Modelo de datos que representa una descripción con su texto y el idioma en el que está escrita.
 * Este modelo se utiliza para mapear los datos de la descripción desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property text El texto de la descripción.
 * @property language El idioma en el que está escrita la descripción.
 */
data class Description(
    val text: String,
    val language: String
) {

    /**
     * Convierte este modelo de datos [Description] en un objeto del dominio [DescriptionDomain].
     *
     * @return Objeto del dominio [DescriptionDomain] que representa la descripción dentro del dominio de la aplicación.
     */
    fun toDomain(): DescriptionDomain {
        return DescriptionDomain(
            text = text,
            language = language
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [DescriptionDomain] en un modelo de datos [Description].
         *
         * @param domain Objeto del dominio que se desea transformar en modelo de datos.
         * @return Objeto [Description] con los datos necesarios para interactuar con fuentes externas.
         */
        fun fromDomain(domain: DescriptionDomain): Description {
            return Description(
                text = domain.text,
                language = domain.language
            )
        }
    }
}
