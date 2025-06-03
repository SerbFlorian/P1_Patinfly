package cat.deim.asm_22.p1_patinfly.domain.repository

import cat.deim.asm_22.p1_patinfly.domain.models.User

/**
 * Interfaz que define las operaciones para gestionar usuarios en el repositorio.
 */
interface IUserRepository {

    /**
     * Inserta un usuario en el repositorio.
     *
     * @param user Objeto User que se desea insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    suspend fun setUser(user: User): Boolean

    /**
     * Obtiene el usuario almacenado actualmente.
     *
     * @return Usuario actual o null si no existe ninguno
     */
    suspend fun getUser(): User?

    /**
     * Obtiene un usuario por su email.
     *
     * @param email Correo electrónico del usuario a buscar
     * @return Usuario correspondiente al email o null si no se encuentra
     */
    suspend fun getUser(email: String): User?

    /**
     * Actualiza un usuario existente en el repositorio.
     *
     * @param user Objeto User con los datos actualizados
     * @return Usuario actualizado o null si no se pudo actualizar
     */
    suspend fun updateUser(user: User): User?

    /**
     * Elimina el usuario almacenado.
     *
     * @return Usuario eliminado o null si no existía ninguno
     */
    suspend fun deleteUser(): User?
}
