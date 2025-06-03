package cat.deim.asm_22.p1_patinfly.domain.models

import com.google.gson.annotations.SerializedName

/**
 * Representa una bicicleta con sus propiedades principales.
 *
 * @property uuid Identificador único de la bicicleta
 * @property name Nombre de la bicicleta
 * @property bikeType Tipo de bicicleta (objeto BikeType)
 * @property creationDate Fecha de creación de la bicicleta en formato String
 * @property lastMaintenanceDate Fecha de la última revisión o mantenimiento, puede ser null si no ha tenido mantenimiento
 * @property inMaintenance Indica si la bicicleta está actualmente en mantenimiento
 * @property isActive Indica si la bicicleta está activa para ser usada
 * @property isDeleted Indica si la bicicleta ha sido eliminada lógicamente
 * @property batteryLevel Nivel de batería actual, representado como un entero
 * @property meters Distancia recorrida en metros
 * @property isRented Indica si la bicicleta está actualmente alquilada
 */
data class Bike(
    val uuid: String,
    val name: String,

    @SerializedName("bike_type")
    val bikeType: BikeType,

    @SerializedName("creation_date")
    val creationDate: String,

    @SerializedName("last_maintenance_date")
    val lastMaintenanceDate: String?,

    @SerializedName("in_maintenance")
    val inMaintenance: Boolean,

    @SerializedName("is_active")
    var isActive: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    @SerializedName("battery_level")
    val batteryLevel: Int,

    val meters: Int,

    val isRented: Boolean
)
