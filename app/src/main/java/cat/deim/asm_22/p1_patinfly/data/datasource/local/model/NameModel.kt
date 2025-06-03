package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.domain.models.NameDomain

/**
 * Modelo de datos que representa un nombre con su texto y el idioma en el que está escrito.
 * Este modelo se utiliza para mapear los datos del nombre desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property text El texto del nombre.
 * @property language El idioma en el que está escrito el nombre.
 */
data class Name(
    val text: String,
    val language: String
) {

    /**
     * Convierte este modelo de datos Name en un objeto del dominio NameDomain.
     *
     * @return Objeto del dominio NameDomain que representa el nombre dentro de la lógica de negocio de la aplicación.
     */
    fun toDomain(): NameDomain {
        return NameDomain(
            text = text,
            language = language
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio NameDomain en un modelo de datos Name.
         *
         * @param domain Objeto del dominio que se desea transformar en modelo de datos.
         * @return Objeto Name que representa los datos del nombre en el formato utilizado por la fuente externa.
         */
        fun fromDomain(domain: NameDomain): Name {
            return Name(
                text = domain.text,
                language = domain.language
            )
        }
    }
}
