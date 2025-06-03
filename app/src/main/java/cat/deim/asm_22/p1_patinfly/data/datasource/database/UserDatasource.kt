package cat.deim.asm_22.p1_patinfly.data.datasource.database

import androidx.room.*
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.UserDTO
import java.util.UUID

/**
 * Interfaz DAO (Data Access Object) para acceder a los datos de los usuarios.
 * Proporciona operaciones para insertar, consultar y eliminar registros en la tabla user.
 */
@Dao
interface UserDatasource {

    /**
     * Inserta un usuario en la base de datos.
     * Si ya existe un usuario con la misma UUID, se reemplaza.
     *
     * @param user Objeto UserDTO que se desea guardar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: UserDTO)

    /**
     * Recupera un usuario a partir de su UUID.
     *
     * @param uuid Identificador único universal del usuario.
     * @return Objeto UserDTO correspondiente al UUID, o null si no se encuentra.
     */
    @Query("SELECT * FROM user WHERE uuid = :uuid")
    suspend fun getByUUID(uuid: UUID): UserDTO?

    /**
     * Recupera un usuario a partir de su correo electrónico.
     *
     * @param email Dirección de correo electrónico del usuario.
     * @return Objeto UserDTO correspondiente al email, o null si no se encuentra.
     */
    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getByEmail(email: String): UserDTO?

    /**
     * Recupera todos los usuarios almacenados en la base de datos.
     *
     * @return Lista de objetos UserDTO.
     */
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<UserDTO>

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user Objeto UserDTO que se desea eliminar.
     */
    @Delete
    suspend fun delete(user: UserDTO)
}
