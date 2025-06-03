package cat.deim.asm_22.patinfly.data.datasource.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import cat.deim.asm_22.p1_patinfly.data.datasource.local.AssetsProvider
import cat.deim.asm_22.p1_patinfly.data.datasource.IUserDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.UserModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.util.*

/**
 * Fuente de datos local que maneja las operaciones relacionadas con los usuarios.
 * Esta clase es responsable de leer y almacenar la información de los usuarios desde un archivo JSON,
 * permitiendo la inserción, actualización, recuperación y eliminación de datos de usuario.
 */
class UserLocalDataSource private constructor() : IUserDataSource {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserLocalDataSource? = null

        private const val TAG = "UserLocalDataSource"

        /**
         * Obtiene una instancia de [UserLocalDataSource].
         * Si no existe una instancia, se crea una nueva.
         * @param context Contexto de la aplicación para acceder a los recursos.
         * @return Instancia de [UserLocalDataSource].
         */
        fun getInstance(context: Context): UserLocalDataSource =
            instance ?: synchronized(this) {
                instance ?: UserLocalDataSource().also {
                    instance = it
                    it.context = context
                    it.loadUserData()
                }
            }
    }

    private var context: Context? = null
    private val usersUUIDMap: MutableMap<UUID, UserModel> = HashMap()
    private val usersMailMap: MutableMap<String, UserModel> = HashMap()

    /**
     * Carga los datos de los usuarios desde un archivo JSON almacenado en los recursos raw.
     * El archivo debe estar ubicado en la carpeta raw y debe contener la información de un usuario.
     */
    private fun loadUserData() {
        context?.let { context ->
            try {
                val jsonData = AssetsProvider.getJsonDataFromRawAsset(context, "user")
                Log.d(TAG, "JSON leído: $jsonData")
                jsonData?.let { it ->
                    val user = parseJson(it)
                    Log.d(TAG, "Usuario parseado: $user")
                    user?.let { save(it) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar datos de usuario: ${e.message}")
            }
        }
    }

    /**
     * Parsea el JSON recibido en un objeto [UserModel].
     * @param json JSON a parsear.
     * @return Objeto [UserModel] o null si el JSON es inválido.
     */
    private fun parseJson(json: String): UserModel? {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create()
        return try {
            gson.fromJson(json, UserModel::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Error al parsear JSON: ${e.message}")
            null
        }
    }

    /**
     * Guarda un usuario en los mapas de usuarios, uno indexado por su UUID y otro por su correo electrónico.
     * @param user Usuario a guardar.
     */
    private fun save(user: UserModel) {
        usersUUIDMap[user.uuid] = user
        usersMailMap[user.email] = user
    }

    /**
     * Inserta un nuevo usuario en la fuente de datos local.
     * Si el usuario ya existe (por UUID), no se realiza la inserción.
     * @param userModel Usuario a insertar.
     * @return True si el usuario fue insertado, false si ya existía.
     */
    override fun insert(userModel: UserModel): Boolean {
        if (usersUUIDMap.containsKey(userModel.uuid)) return false
        save(userModel)
        return true
    }

    /**
     * Inserta o actualiza un usuario en la fuente de datos local.
     * Si el usuario ya existe, se actualiza.
     * @param userModel Usuario a insertar o actualizar.
     * @return True si la operación se realizó con éxito.
     */
    override fun insertOrUpdate(userModel: UserModel): Boolean {
        save(userModel)
        return true
    }

    /**
     * Obtiene el primer usuario almacenado en la fuente de datos local.
     * @return El primer usuario almacenado, o null si no hay usuarios.
     */
    override fun getUser(): UserModel? {
        return usersUUIDMap.values.firstOrNull()
    }

    /**
     * Obtiene un usuario por su UUID.
     * @param uuid UUID del usuario a buscar.
     * @return El usuario encontrado, o null si no se encuentra.
     */
    override fun getUserById(uuid: String): UserModel? {
        return try {
            usersUUIDMap[UUID.fromString(uuid)]
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "UUID inválido: $uuid")
            null
        }
    }

    /**
     * Obtiene todos los usuarios almacenados en la fuente de datos local.
     * @return Una lista de todos los usuarios.
     */
    override fun getAllUsers(): List<UserModel> {
        return usersUUIDMap.values.toList()
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     * La búsqueda no distingue mayúsculas/minúsculas y elimina espacios en blanco.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return El usuario encontrado, o null si no se encuentra.
     */
    override fun getUser(email: String): UserModel? {
        val normalizedEmail = email.trim().lowercase()
        return usersMailMap[normalizedEmail]
    }

    /**
     * Actualiza los datos de un usuario existente en la fuente de datos local.
     * @param userModel Usuario a actualizar.
     * @return El usuario actualizado, o null si no existe.
     */
    override fun update(userModel: UserModel): UserModel? {
        if (!usersUUIDMap.containsKey(userModel.uuid)) return null
        save(userModel)
        return userModel
    }

    /**
     * Elimina el primer usuario almacenado en la fuente de datos local.
     * @return El usuario eliminado, o null si no hay usuarios.
     */
    override fun delete(): UserModel? {
        val user = getUser() ?: return null
        usersUUIDMap.remove(user.uuid)
        usersMailMap.remove(user.email)
        return user
    }
}
