package cat.deim.asm_22.p1_patinfly.domain.usecase

import android.util.Log
import cat.deim.asm_22.p1_patinfly.domain.models.Credentials
import cat.deim.asm_22.p1_patinfly.domain.repository.IUserRepository
class LoginUsecase(private val userRepository: IUserRepository) {

    /**
     * Verifica si un usuario existe en el sistema.
     *
     * @param email Correo electrónico del usuario a verificar
     * @return `true` si el usuario existe, `false` si no existe o ocurre un error
     * @throws Exception Registra errores en el log pero no los propaga
     *
     */
    suspend fun checkUserExists(email: String): Boolean {
        return try {
            val user = userRepository.getUser(email)
            val inputNormalized = email.trim().lowercase()
            val storedNormalized = user?.email?.trim()?.lowercase()
            inputNormalized == storedNormalized
        } catch (error: Exception) {
            Log.e("LoginUseCase", "Error al verificar el usuario", error)
            false
        }
    }


    /**
     * Ejecuta el proceso completo de autenticación.
     *
     * Realiza las siguientes validaciones:
     *
     * Verifica que el usuario exista
     * Compara la contraseña proporcionada con la almacenada
     *
     *
     * @param credentials Credenciales del usuario (email y contraseña)
     * @return `true` si la autenticación es exitosa, `false` en caso contrario
     * @throws Exception Registra errores en el log pero no los propaga
     */
    suspend fun execute(credentials: Credentials): Boolean {
        return try {
            val user = userRepository.getUser(credentials.email)
            if (user == null) {
                Log.d("LoginUseCase", "Usuario no encontrado")
                return false
            }
            val isPasswordValid = verifyPassword(credentials.password, user.hashedPassword)
            if (!isPasswordValid) {
                Log.d("LoginUseCase", "Contraseña incorrecta")
                return false
            }
            Log.d("LoginUseCase", "Autenticación exitosa para el usuario: ${user.email}")
            true
        } catch (error: Exception) {
            Log.e("LoginUseCase", "Error en el proceso de autenticación", error)
            false
        }
    }

    /**
     * Compara una contraseña ingresada con una almacenada.
     *
     * Nota de seguridad: En una aplicación real, esto debería usar
     * funciones de hash seguras como BCrypt en lugar de comparación directa.
     *
     * @param inputPassword Contraseña proporcionada por el usuario
     * @param storedPassword Contraseña almacenada (generalmente hasheada)
     * @return `true` si las contraseñas coinciden, `false` en caso contrario
     */
    private fun verifyPassword(inputPassword: String, storedPassword: String): Boolean {
        Log.d("verifyPassword", "Contraseña ingresada: $inputPassword")
        Log.d("verifyPassword", "Contraseña almacenada: $storedPassword")
        val isValid = inputPassword == storedPassword
        Log.d("verifyPassword", "Contraseña válida: $isValid")
        return isValid
    }
}