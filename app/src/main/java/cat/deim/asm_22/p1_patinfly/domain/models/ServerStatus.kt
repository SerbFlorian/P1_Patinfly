package cat.deim.asm_22.p1_patinfly.domain.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.local.BikeLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.BikeAPIDataSource
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository

/**
 * Representa el estado del servidor con información de versión, build, actualización y nombre.
 *
 * @property version Versión del servidor
 * @property build Número o identificador de la build
 * @property update Fecha o descripción de la última actualización
 * @property name Nombre del servidor o del estado
 */
data class ServerStatus(
    val version: String,
    val build: String,
    val update: String,
    val name: String
) {
    companion object {
        /**
         * Ejecuta de forma asíncrona la obtención del estado del servidor usando LiveData.
         *
         * @param context Contexto de la aplicación necesario para acceder a las fuentes de datos y repositorios.
         * @return LiveData que emite un resultado exitoso con ServerStatus o un fallo en caso de error.
         */
        fun execute(context: Context): LiveData<Result<ServerStatus>> = liveData {
            try {
                // Obtener las fuentes de datos necesarias
                val dataSource = AppDatabase.getDatabase(context).bikeDataSource()
                val apiDataSource = BikeAPIDataSource.getInstance(context)
                val localDataSource = BikeLocalDataSource.getInstance(context)

                // Crear repositorio con las fuentes de datos y el contexto
                val repository = BikeRepository(
                    dataSource,
                    apiDataSource,
                    localDataSource,
                    context
                )

                // Obtener el estado del servidor desde el repositorio
                val status = repository.status()

                // Emitir el resultado exitoso
                emit(Result.success(status))
            } catch (e: Exception) {
                // Emitir el error en caso de excepción
                emit(Result.failure(e))
            }
        }
    }
}
