package cat.deim.asm_22.p1_patinfly.data.datasource.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

object AssetsProvider {
    /**
     * Carga un archivo JSON desde la carpeta `res/raw/` y lo devuelve como un String.
     *
     * @param context Contexto de la aplicación.
     * @param fileName Nombre del archivo JSON sin la extensión.
     * @return String con el contenido del archivo JSON o null si no se ha podido cargar.
     */
    @SuppressLint("DiscouragedApi")
    fun getJsonDataFromRawAsset(context: Context, fileName: String): String? {
        return try {
            val resourceId = context.resources.getIdentifier(fileName, "raw", context.packageName)

            if (resourceId == 0) {
                Log.e("AssetsProvider", "Archivo no encontrado: $fileName en res/raw/")
                return null
            }

            Log.d("AssetsProvider", "Cargando archivo JSON: $fileName con ID: $resourceId")

            context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("AssetsProvider", "Error al leer el archivo JSON: ${e.message}")
            null
        }
    }
}
