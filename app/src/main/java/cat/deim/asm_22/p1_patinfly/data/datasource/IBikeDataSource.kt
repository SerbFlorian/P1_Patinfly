package cat.deim.asm_22.p1_patinfly.data.datasource

import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.BikeModel

/**
 * Interfaz que define las operaciones básicas de acceso a datos para las bicicletas.
 */
interface IBikeDataSource {

    /**
     * Inserta un nuevo modelo de bicicleta en la fuente de datos.
     *
     * @param bikeModel Modelo de bicicleta a insertar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insert(bikeModel: BikeModel): Boolean

    /**
     * Inserta o actualiza un modelo de bicicleta en la fuente de datos.
     * Si la bicicleta ya existe, se actualiza; si no, se inserta.
     *
     * @param bikeModel Modelo de bicicleta a insertar o actualizar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    fun insertOrUpdate(bikeModel: BikeModel): Boolean

    /**
     * Obtiene una bicicleta cualquiera de la fuente de datos.
     * La implementación concreta decide qué bicicleta devolver.
     *
     * @return Un modelo de bicicleta o null si no hay bicicletas disponibles
     */
    fun getBike(): BikeModel?

    /**
     * Obtiene una bicicleta a partir de su identificador único.
     *
     * @param uuid Identificador único de la bicicleta
     * @return El modelo de bicicleta correspondiente o null si no se encuentra
     */
    fun getBikeById(uuid: String): BikeModel?

    /**
     * Actualiza la información de una bicicleta en la fuente de datos.
     *
     * @param bikeModel Modelo de bicicleta actualizado
     * @return El modelo de bicicleta actualizado o null si no se pudo actualizar
     */
    fun update(bikeModel: BikeModel): BikeModel?

    /**
     * Elimina una bicicleta de la fuente de datos.
     * La implementación concreta decide qué bicicleta eliminar.
     *
     * @return El modelo de bicicleta eliminado o null si no se eliminó ninguna
     */
    fun delete(): BikeModel?

    /**
     * Obtiene todas las bicicletas almacenadas en la fuente de datos.
     *
     * @return Colección de modelos de bicicleta
     */
    fun getAll(): Collection<BikeModel>
}
