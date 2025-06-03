package cat.deim.asm_22.p1_patinfly.data.datasource

import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.UserModel

/**
 * Interfaz que define las operaciones básicas de acceso a datos
 * para los usuarios del sistema.
 */
interface IUserDataSource {

    /**
     * Inserta un nuevo usuario en la fuente de datos.
     *
     * @param userModel Modelo del usuario a insertar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insert(userModel: UserModel): Boolean

    /**
     * Inserta o actualiza un usuario en la fuente de datos.
     * Si el usuario ya existe, se actualiza; si no, se inserta.
     *
     * @param userModel Modelo del usuario a insertar o actualizar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insertOrUpdate(userModel: UserModel): Boolean

    /**
     * Obtiene un usuario cualquiera de la fuente de datos.
     * La implementación concreta decide qué usuario devolver.
     *
     * @return Un modelo de usuario o null si no hay disponibles
     */
    fun getUser(): UserModel?

    /**
     * Obtiene un usuario a partir de su identificador único.
     *
     * @param uuid Identificador único del usuario
     * @return El modelo del usuario correspondiente o null si no se encuentra
     */
    fun getUserById(uuid: String): UserModel?

    /**
     * Actualiza la información de un usuario en la fuente de datos.
     *
     * @param userModel Modelo del usuario actualizado
     * @return El modelo del usuario actualizado o null si no se pudo actualizar
     */
    fun update(userModel: UserModel): UserModel?

    /**
     * Elimina un usuario de la fuente de datos.
     * La implementación concreta decide qué usuario eliminar.
     *
     * @return El modelo del usuario eliminado o null si no se eliminó ninguno
     */
    fun delete(): UserModel?

    /**
     * Obtiene todos los usuarios almacenados en la fuente de datos.
     *
     * @return Lista de modelos de usuarios
     */
    fun getAllUsers(): List<UserModel>

    /**
     * Obtiene un usuario a partir de su dirección de correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return El modelo del usuario correspondiente o null si no se encuentra
     */
    fun getUser(email: String): UserModel?
}
