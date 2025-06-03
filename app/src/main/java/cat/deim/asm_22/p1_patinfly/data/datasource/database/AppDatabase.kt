package cat.deim.asm_22.p1_patinfly.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.SystemPricingPlanDTO
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.UserDTO
import cat.deim.asm_22.p1_patinfly.domain.models.converter.BikeTypeConverter

/**
 * Clase abstracta que define la base de datos de la aplicación utilizando Room.
 * Incluye las entidades [UserDTO], [BikeDTO] y [SystemPricingPlanDTO],
 * así como el conversor [BikeTypeConverter] para tipos personalizados.
 */
@Database(
    entities = [UserDTO::class, BikeDTO::class, SystemPricingPlanDTO::class],
    version = 6
)
@TypeConverters(BikeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al origen de datos de usuarios.
     *
     * @return Interfaz [UserDatasource] para operaciones con la tabla de usuarios.
     */
    abstract fun userDataSource(): UserDatasource

    /**
     * Proporciona acceso al origen de datos de bicicletas.
     *
     * @return Interfaz [BikeDatasource] para operaciones con la tabla de bicicletas.
     */
    abstract fun bikeDataSource(): BikeDatasource

    /**
     * Proporciona acceso al origen de datos del plan de precios del sistema.
     *
     * @return Interfaz [SystemPricingPlanDataSource] para operaciones con la tabla de planes de precios.
     */
    abstract fun systemPricingPlanDataSource(): SystemPricingPlanDataSource

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene una instancia única de [AppDatabase].
         * Si no existe, se crea una nueva instancia utilizando el contexto de la aplicación.
         * Se aplica `fallbackToDestructiveMigration` para reinicializar la base de datos en caso de cambios no compatibles en la estructura.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "patinfly_25_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
    }
}
