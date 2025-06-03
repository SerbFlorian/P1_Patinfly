package cat.deim.asm_22.p1_patinfly.domain.repository

import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.ServerStatus

/**
 * Interfaz que define las operaciones para gestionar bicicletas en el repositorio.
 */
interface IBikeRepository {

    /**
     * Inserta una bicicleta en el repositorio.
     *
     * @param bike Objeto Bike que se desea insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    suspend fun setBike(bike: Bike): Boolean

    /**
     * Obtiene una bicicleta del repositorio.
     *
     * @return Bike obtenida o null si no existe ninguna bicicleta
     */
    suspend fun getBike(): Bike?

    /**
     * Obtiene una bicicleta por su identificador único.
     *
     * @param uuid Identificador único de la bicicleta
     * @return Bike con el uuid especificado o null si no existe
     */
    suspend fun getBike(uuid: String): Bike?

    /**
     * Actualiza una bicicleta existente en el repositorio.
     *
     * @param bike Objeto Bike con los datos actualizados
     * @return Bike actualizada o null si no se pudo actualizar
     */
    suspend fun updateBike(bike: Bike): Bike?

    /**
     * Elimina una bicicleta del repositorio.
     *
     * @return Bike eliminada o null si no había ninguna para eliminar
     */
    suspend fun deleteBike(): Bike?

    /**
     * Obtiene todas las bicicletas disponibles en el repositorio.
     *
     * @return Colección con todas las bicicletas
     */
    suspend fun getAll(): Collection<Bike>

    /**
     * Obtiene el estado actual del servidor.
     *
     * @return Estado del servidor como ServerStatus
     */
    fun status(): ServerStatus

    /**
     * Actualiza el estado de alquiler de una bicicleta.
     *
     * @param bike Objeto Bike cuya información de alquiler se va a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    suspend fun updateBikeRentStatus(bike: Bike): Boolean
}
