package cat.deim.asm_22.p1_patinfly.domain.models.converter

import androidx.room.TypeConverter
import cat.deim.asm_22.p1_patinfly.domain.models.BikeType

/**
 * Convertidor para la persistencia de objetos BikeType en la base de datos Room.
 * Permite transformar un objeto BikeType a un String (normalmente su uuid)
 * y viceversa, para que Room pueda almacenar y recuperar el dato correctamente.
 */
class BikeTypeConverter {

    /**
     * Convierte un objeto BikeType en un String que representa su identificador único (uuid).
     *
     * @param bikeType Objeto BikeType a convertir, puede ser null
     * @return El uuid del BikeType como String, o null si el objeto es null
     */
    @TypeConverter
    fun fromBikeType(bikeType: BikeType?): String? {
        return bikeType?.uuid
    }

    /**
     * Convierte un String (uuid) en un objeto BikeType.
     *
     * Nota: Este metodo debe ajustarse para recuperar correctamente el BikeType
     * a partir del uuid, ya sea consultando una fuente de datos o construyendo
     * el objeto de forma adecuada.
     *
     * @param uuid Identificador único del BikeType, puede ser null
     * @return Un objeto BikeType correspondiente al uuid, o null si uuid es null
     */
    @TypeConverter
    fun toBikeType(uuid: String?): BikeType? {
        return uuid?.let { BikeType(it, "name", "type") }
    }
}
