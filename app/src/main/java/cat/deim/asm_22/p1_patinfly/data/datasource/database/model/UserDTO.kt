package cat.deim.asm_22.p1_patinfly.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cat.deim.asm_22.p1_patinfly.domain.models.User
import java.util.UUID

/**
 * Clase de datos que representa a un usuario en la base de datos local.
 * Anotada como @Entity para su uso con Room.
 *
 * @property uuid Identificador único del usuario.
 * @property name Nombre del usuario.
 * @property email Correo electrónico del usuario.
 * @property hashedPassword Contraseña del usuario en formato hash.
 * @property creationDate Fecha de creación de la cuenta del usuario.
 * @property lastConnection Fecha de la última conexión del usuario.
 * @property deviceId Identificador del dispositivo asociado al usuario.
 */
@Entity(tableName = "user")
data class UserDTO(
    @PrimaryKey val uuid: UUID,
    val name: String,
    val email: String,
    val hashedPassword: String,
    val creationDate: String,
    val lastConnection: String,
    val deviceId: String
) {
    /**
     * Convierte el modelo de datos UserDTO a un objeto del dominio User.
     *
     * @return Objeto User que representa al usuario en el dominio de la aplicación.
     */
    fun toDomain() = User(uuid, name, email, hashedPassword, creationDate, lastConnection, deviceId)

    companion object {
        /**
         * Convierte un objeto del dominio User a un modelo de datos UserDTO.
         *
         * @param user Objeto del dominio que se desea convertir.
         * @return Objeto UserDTO para ser almacenado en la base de datos.
         */
        fun fromDomain(user: User) = UserDTO(
            uuid = user.uuid,
            name = user.name,
            email = user.email,
            hashedPassword = user.hashedPassword,
            creationDate = user.creationDate,
            lastConnection = user.lastConnection,
            deviceId = user.deviceId
        )
    }
}
