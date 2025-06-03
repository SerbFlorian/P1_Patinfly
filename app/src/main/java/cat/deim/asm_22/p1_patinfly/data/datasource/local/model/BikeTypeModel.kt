package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import com.google.gson.annotations.SerializedName
import cat.deim.asm_22.p1_patinfly.domain.models.BikeType

/**
 * Modelo de datos que representa el tipo de una bicicleta.
 * Este modelo es utilizado para mapear los datos del tipo de bicicleta desde una fuente de datos externa
 * (como JSON) hacia el dominio de la aplicación.
 */
data class BikeTypeModel(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String
) {

    /**
     * Convierte el modelo de datos [BikeTypeModel] a un objeto del dominio [BikeType].
     *
     * @return Objeto [BikeType] que representa el tipo de bicicleta en el dominio de la aplicación.
     */
    fun toDomain(): BikeType {
        return BikeType(
            uuid = uuid,
            name = name,
            type = type
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [BikeType] a un modelo de datos [BikeTypeModel].
         *
         * @param bikeType Objeto del dominio [BikeType] que se desea convertir a modelo de datos.
         * @return [BikeTypeModel] que representa el tipo de bicicleta en el formato utilizado por la fuente de datos.
         */
        fun fromDomain(bikeType: BikeType): BikeTypeModel {
            return BikeTypeModel(
                uuid = bikeType.uuid,
                name = bikeType.name,
                type = bikeType.type
            )
        }
    }
}
