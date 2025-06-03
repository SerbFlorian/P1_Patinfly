package cat.deim.asm_22.p1_patinfly.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.BikeType

/**
 * Clase de datos que representa una bicicleta en la base de datos local.
 * Utiliza anotaciones de Room para definir la entidad y su clave primaria.
 *
 * @property uuid Identificador único de la bicicleta.
 * @property name Nombre de la bicicleta.
 * @property bikeType Tipo de bicicleta (por ejemplo, eléctrica o convencional).
 * @property creationDate Fecha de creación de la bicicleta.
 * @property lastMaintenanceDate Fecha de la última revisión de mantenimiento. Puede ser nulo.
 * @property inMaintenance Indica si la bicicleta está actualmente en mantenimiento.
 * @property isActive Indica si la bicicleta está activa y disponible para su uso.
 * @property isDeleted Indica si la bicicleta ha sido marcada como eliminada.
 * @property batteryLevel Nivel de batería de la bicicleta (solo aplicable a bicicletas eléctricas).
 * @property meters Distancia total recorrida por la bicicleta en metros.
 * @property isRented Indica si la bicicleta está actualmente alquilada.
 */
@Entity(tableName = "bike")
data class BikeDTO(
    @PrimaryKey val uuid: String,
    val name: String,
    val bikeType: BikeType,
    val creationDate: String,
    val lastMaintenanceDate: String?,
    val inMaintenance: Boolean,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val batteryLevel: Int,
    val meters: Int,
    var isRented: Boolean
) {
    /**
     * Convierte el modelo BikeDTO a un objeto del dominio Bike.
     *
     * @return Objeto Bike que representa la bicicleta en el dominio de la aplicación.
     */
    fun toDomain(): Bike {
        return Bike(
            uuid = uuid,
            name = name,
            bikeType = bikeType,
            creationDate = creationDate,
            lastMaintenanceDate = lastMaintenanceDate,
            inMaintenance = inMaintenance,
            isActive = isActive,
            isDeleted = isDeleted,
            batteryLevel = batteryLevel,
            meters = meters,
            isRented = isRented
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio Bike a un modelo de datos BikeDTO.
         *
         * @param bike Objeto del dominio Bike que se desea convertir.
         * @return Objeto BikeDTO que representa la bicicleta en la base de datos.
         */
        fun fromDomain(bike: Bike): BikeDTO {
            return BikeDTO(
                uuid = bike.uuid,
                name = bike.name,
                bikeType = bike.bikeType,
                creationDate = bike.creationDate,
                lastMaintenanceDate = bike.lastMaintenanceDate,
                inMaintenance = bike.inMaintenance,
                isActive = bike.isActive,
                isDeleted = bike.isDeleted,
                batteryLevel = bike.batteryLevel,
                meters = bike.meters,
                isRented = bike.isRented
            )
        }
    }
}
