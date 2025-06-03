package cat.deim.asm_22.p1_patinfly.data.datasource.local.model

import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO
import cat.deim.asm_22.p1_patinfly.domain.models.User
import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Modelo de datos que representa un usuario, con información como su UUID, nombre, correo electrónico,
 * contraseña cifrada, fecha de creación, última conexión y el ID del dispositivo.
 * Este modelo se utiliza para mapear los datos del usuario desde una fuente externa (como JSON)
 * hacia el dominio de la aplicación.
 *
 * @property uuid Identificador único del usuario.
 * @property name Nombre completo del usuario.
 * @property email Dirección de correo electrónico del usuario.
 * @property hashedPassword Contraseña cifrada del usuario.
 * @property creationDate Fecha en la que el usuario fue creado.
 * @property lastConnection Fecha de la última conexión del usuario.
 * @property deviceId Identificador único del dispositivo del usuario.
 * @property rentalHistory Historial de alquileres del usuario (lista de bicicletas).
 */
data class UserModel(
    val uuid: UUID,
    val name: String,
    val email: String,
    @SerializedName("hashed_password") val hashedPassword: String,
    @SerializedName("creation_date") val creationDate: String,
    @SerializedName("last_connection") val lastConnection: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("rental_history") val rentalHistory: List<BikeDTO>  // Se asegura que esta propiedad sea correctamente mapeada
) {
    /**
     * Convierte el modelo de datos [UserModel] a un objeto del dominio [User].
     *
     * @return Objeto [User] que representa el usuario en el dominio de la aplicación.
     */
    fun toDomain(): User {
        return User(
            uuid = uuid,
            name = name,
            email = email,
            hashedPassword = hashedPassword,
            creationDate = creationDate,
            lastConnection = lastConnection,
            deviceId = deviceId
        )
    }

    companion object {
        /**
         * Convierte un objeto del dominio [User] a un modelo de datos [UserModel].
         *
         * @param user Objeto del dominio [User] que se desea convertir a modelo de datos.
         * @return [UserModel] que representa al usuario en el formato utilizado por la fuente de datos.
         */
        fun fromDomain(user: User, rentalHistory: List<BikeDTO> = emptyList()): UserModel {
            return UserModel(
                uuid = user.uuid,
                name = user.name,
                email = user.email,
                hashedPassword = user.hashedPassword,
                creationDate = user.creationDate,
                lastConnection = user.lastConnection,
                deviceId = user.deviceId,
                rentalHistory = rentalHistory // Asigna el historial de alquileres si está disponible
            )
        }

    }
}
