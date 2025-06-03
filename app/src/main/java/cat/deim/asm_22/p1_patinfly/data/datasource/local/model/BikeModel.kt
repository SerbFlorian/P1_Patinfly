package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import com.google.gson.annotations.SerializedName
import cat.deim.asm_22.p1_patinfly.domain.models.Bike

/**
 * Modelo de datos que representa una bicicleta.
 * Este modelo es utilizado para mapear los datos de una bicicleta desde una fuente de datos externa (como JSON)
 * hacia el dominio de la aplicación.
 */
data class BikeModel(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("bike_type") val bikeType: BikeTypeModel,
    @SerializedName("creation_date") val creationDate: String,
    @SerializedName("last_maintenance_date") val lastMaintenanceDate: String?,
    @SerializedName("in_maintenance") val inMaintenance: Boolean,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("battery_level") val batteryLevel: Int,
    @SerializedName("meters") val meters: Int,
    @SerializedName("is_rented") val isRented: Boolean // Añadido el campo isRented
) {

    /**
     * Convierte el modelo de datos [BikeModel] a un objeto del dominio [Bike].
     *
     * @return Objeto [Bike] que representa la bicicleta en el dominio de la aplicación.
     */
    fun toDomain(): Bike {
        return Bike(
            uuid = uuid,
            name = name,
            bikeType = bikeType.toDomain(),
            creationDate = creationDate,
            lastMaintenanceDate = lastMaintenanceDate,
            inMaintenance = inMaintenance,
            isActive = isActive,
            isDeleted = isDeleted,
            batteryLevel = batteryLevel,
            meters = meters,
            isRented = isRented // Mapeo de isRented
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [Bike] a un modelo de datos [BikeModel].
         *
         * @param bike Objeto del dominio [Bike] que se desea convertir a modelo de datos.
         * @return [BikeModel] que representa la bicicleta en el formato utilizado por la fuente de datos.
         */
        fun fromDomain(bike: Bike): BikeModel {
            return BikeModel(
                uuid = bike.uuid,
                name = bike.name,
                bikeType = BikeTypeModel.fromDomain(bike.bikeType),
                creationDate = bike.creationDate,
                lastMaintenanceDate = bike.lastMaintenanceDate,
                inMaintenance = bike.inMaintenance,
                isActive = bike.isActive,
                isDeleted = bike.isDeleted,
                batteryLevel = bike.batteryLevel,
                meters = bike.meters,
                isRented = bike.isRented // Añadido el campo isRented
            )
        }
    }
}
