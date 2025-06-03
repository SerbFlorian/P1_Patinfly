package cat.deim.asm_22.p1_patinfly.data.repository

import android.content.Context
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.database.UserDatasource
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.UserDTO
import cat.deim.asm_22.patinfly.data.datasource.local.UserLocalDataSource
import cat.deim.asm_22.p1_patinfly.domain.models.User
import cat.deim.asm_22.p1_patinfly.domain.repository.IUserRepository

/**
 * Implementación del repositorio para gestionar los usuarios en el sistema.
 *
 * Este repositorio interactúa con la fuente de datos para realizar operaciones de almacenamiento,
 * actualización, eliminación y obtención de usuarios.
 *
 * @param userDatasource Fuente de datos utilizada para realizar las operaciones sobre los usuarios.
 */
class UserRepository(private val userDatasource: UserDatasource, private val context: Context) :
    IUserRepository {

    companion object {
        /**
         * Metodo factoría para crear una instancia de [UserRepository] configurada con la fuente de datos local.
         *
         * Este metodo sigue el patrón de diseño Singleton para garantizar que solo exista una instancia
         * del repositorio en la aplicación, utilizando [UserLocalDataSource] como fuente de datos subyacente.
         *
         * @param context Contexto de la aplicación necesario para inicializar la fuente de datos local.
         * @return Instancia única de [UserRepository] configurada y lista para su uso.
         *
         * @see UserLocalDataSource.getInstance
         */
        fun create(context: Context): IUserRepository {
            val dataSource = AppDatabase.getDatabase(context).userDataSource()
            return UserRepository(dataSource, context)
        }
    }

    /**
     * Inserta un nuevo usuario en el sistema.
     *
     * @param user Usuario a insertar.
     * @return `true` si el usuario se insertó correctamente, `false` en caso contrario.
     */
    override suspend fun setUser(user: User): Boolean {
        return try {
            userDatasource.save(UserDTO.fromDomain(user))
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @return El usuario con el ID especificado, `null` si no existe.
     */
    override suspend fun getUser(): User? {
        val localUser = UserLocalDataSource.getInstance(context).getUser()
        return localUser?.toDomain()
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return El usuario con el correo electrónico especificado, null si no existe.
     */
    override suspend fun getUser(email: String): User? {
        // 1. Buscar en la base de datos local
        val localUser = userDatasource.getByEmail(email)?.toDomain()
        if (localUser != null) return localUser

        // 2. Buscar en la fuente remota (archivo JSON, en este caso)
        val remoteUser = UserLocalDataSource.getInstance(context).getUser(email)?.toDomain()
        if (remoteUser != null) {
            // 3. Guardar en la base de datos local y devolver
            userDatasource.save(UserDTO.fromDomain(remoteUser))
            return remoteUser
        }

        return null
    }


    /**
     * Actualiza un usuario en el sistema.
     *
     * @param user Usuario a actualizar.
     * @return El usuario actualizado, `null` si no se pudo actualizar.
     */
    override suspend fun updateUser(user: User): User {
        // En Room, no es necesario realizar una actualización si el objeto ya existe.
        // Simplemente volvemos a insertar el objeto con el mismo ID.
        setUser(user)
        return user
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @return El usuario eliminado, `null` si no se pudo eliminar.
     */
    override suspend fun deleteUser(): User? {
        val user = getUser()
        user?.let {
            userDatasource.delete(UserDTO.fromDomain(it))
        }
        return user
    }
}