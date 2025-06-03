package cat.deim.asm_22.p1_patinfly.data.datasource.remote.model

import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.BikeType
import cat.deim.asm_22.p1_patinfly.domain.models.ServerStatus
import kotlinx.serialization.SerialName

/**
 * Modelo de datos que representa una bicicleta obtenida desde una API remota.
 *
 * @property id Identificador único de la bicicleta.
 * @property name Nombre de la bicicleta.
 * @property bikeType Tipo de bicicleta como cadena de texto.
 * @property creationDate Fecha de creación de la bicicleta.
 * @property lastMaintenanceDate Fecha de la última revisión o mantenimiento, si existe.
 * @property inMaintenance Indica si la bicicleta está actualmente en mantenimiento.
 * @property isActive Indica si la bicicleta está activa.
 * @property isDeleted Indica si la bicicleta ha sido eliminada lógicamente.
 * @property batteryLevel Nivel de batería actual de la bicicleta.
 * @property meters Distancia total recorrida en metros.
 * @property isRented Indica si la bicicleta está actualmente alquilada.
 */
data class BikeApiModel(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("bikeType") val bikeType: String,
    @SerialName("creationDate") val creationDate: String,
    @SerialName("lastMaintenanceDate") val lastMaintenanceDate: String?,
    @SerialName("inMaintenance") val inMaintenance: Boolean,
    @SerialName("isActive") val isActive: Boolean,
    @SerialName("isDeleted") val isDeleted: Boolean,
    @SerialName("batteryLevel") val batteryLevel: Int,
    @SerialName("meters") val meters: Int,
    @SerialName("isRented") val isRented: Boolean
) {
    /**
     * Clase que representa la información del estado del servidor.
     *
     * @property version Versión del servidor.
     * @property build Número de compilación del servidor.
     * @property update Fecha o código de la última actualización.
     * @property name Nombre del servidor o sistema.
     */
    data class StatusInfo(
        @SerialName("version") val version: String,
        @SerialName("build") val build: String,
        @SerialName("update") val update: String,
        @SerialName("name") val name: String
    )

    /**
     * Modelo de datos que encapsula la respuesta del estado del servidor obtenida de la API.
     *
     * @property status Objeto con la información detallada del estado del servidor.
     */
    data class StatusApiModel(
        @SerialName("status") val status: StatusInfo
    ) {
        /**
         * Convierte el modelo [StatusApiModel] a un objeto del dominio [ServerStatus].
         *
         * @return Objeto del dominio que representa el estado del servidor.
         */
        fun toStatusDomain(): ServerStatus {
            return ServerStatus(
                version = status.version,
                build = status.build,
                update = status.update,
                name = status.name
            )
        }
    }

    /**
     * Convierte este modelo [BikeApiModel] a un objeto del dominio [Bike].
     *
     * @return Objeto del dominio que representa la bicicleta dentro de la lógica de la aplicación.
     */
    fun toDomain(): Bike {
        return Bike(
            uuid = id,
            name = name,
            bikeType = BikeType(uuid = "", name = bikeType, type = bikeType),
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
         * Convierte un objeto del dominio [Bike] a un modelo de datos [BikeApiModel].
         *
         * @param bike Objeto del dominio que se desea convertir.
         * @return Modelo de datos que representa la bicicleta en el formato esperado por la API.
         */
        fun fromDomain(bike: Bike): BikeApiModel {
            return BikeApiModel(
                id = bike.uuid,
                name = bike.name,
                bikeType = bike.bikeType.type,
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
