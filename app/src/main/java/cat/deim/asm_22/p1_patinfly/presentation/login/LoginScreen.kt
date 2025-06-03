package cat.deim.asm_22.p1_patinfly.presentation.login

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.presentation.MainActivity

/**
 * Pantalla de inicio de sesión que muestra un formulario para autenticar usuarios.
 *
 * Esta pantalla incluye:
 * - Cabecera con logo y nombre de la aplicación
 * - Campos para email y contraseña con validación visual
 * - Enlace para recuperación de contraseña
 * - Botón de acceso con validación de credenciales
 *
 * @param viewModel ViewModel que gestiona el estado y lógica de autenticación
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Layout principal con scroll
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()  // Se asegura de que ocupe todoo el tamaño
                .padding(21.dp)
                .verticalScroll(rememberScrollState()), // Permite el scroll
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabecera con logo y nombre de la aplicación
            LoginHeader()

            // Campo de email
            EmailField(
                email = uiState.email,
                isUserValid = uiState.isUserValid,
                onValueChange = viewModel::onEmailChanged
            )

            // Campo de contraseña
            PasswordField(
                password = uiState.password,
                isPasswordValid = uiState.isPasswordValid,
                onValueChange = viewModel::onPasswordChanged,
                onLogin = {
                    if (uiState.isUserValid && uiState.isPasswordValid && viewModel.login()) {
                        keyboardController?.hide()
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                }
            )

            // Enlace de olvido de contraseña
            ForgotPasswordLink()

            // Botón de login
            LoginButton(
                enabled = uiState.isUserValid && uiState.isPasswordValid,
                onClick = {
                    if (viewModel.login()) {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                }
            )

            // Spacer adicional para evitar que el contenido se sobrecargue al final
            Spacer(modifier = Modifier.height(100.dp))  // Ajusta el tamaño según sea necesario
        }
    }
}


/**
 * Cabecera de la pantalla de login con logo y nombre de la aplicación.
 */
@Composable
private fun LoginHeader() {
    Image(
        painter = painterResource(id = R.drawable.login_image),
        contentDescription = "Logo de la aplicación",
        modifier = Modifier
            .size(400.dp)
            .padding(bottom = 16.dp, top = 30.dp)
    )

    Text(
        text = "Patinfly",
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 30.dp)
    )
}

/**
 * Campo de entrada para el email con validación visual.
 *
 * @param email Valor actual del campo email
 * @param isUserValid Indica si el email es válido
 * @param onValueChange Callback cuando cambia el valor
 */
@Composable
private fun EmailField(
    email: String,
    isUserValid: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        OutlinedTextField(
            value = email,
            onValueChange = onValueChange,
            isError = email.isNotEmpty() && !isUserValid,
            label = {
                Text(
                    if (email.isNotEmpty() && !isUserValid) "Incorrect email"
                    else "Enter your email"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp), // Bordes redondeados de 16.dp
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (email.isNotEmpty()) {
                    if (isUserValid) Color.Green else Color.Red
                } else MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = if (email.isNotEmpty()) {
                    if (isUserValid) Color.Green else Color.Red
                } else MaterialTheme.colorScheme.primary
            )
        )
    }
}

/**
 * Campo de entrada para la contraseña con validación visual.
 *
 * @param password Valor actual del campo contraseña
 * @param isPasswordValid Indica si la contraseña es válida
 * @param onValueChange Callback cuando cambia el valor
 * @param onLogin Callback al pulsar "Done" en el teclado
 */
@Composable
private fun PasswordField(
    password: String,
    isPasswordValid: Boolean,
    onValueChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onValueChange,
            isError = password.isNotEmpty() && !isPasswordValid,
            label = {
                Text(
                    if (password.isNotEmpty() && !isPasswordValid) "Incorrect password"
                    else "Enter your password"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onLogin() }
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (password.isNotEmpty()) {
                    if (isPasswordValid) Color.Green else Color.Red
                } else MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = if (password.isNotEmpty()) {
                    if (isPasswordValid) Color.Green else Color.Red
                } else MaterialTheme.colorScheme.primary
            )
        )
    }
}

/**
 * Enlace para recuperación de contraseña.
 */
@Composable
private fun ForgotPasswordLink() {
    Text(
        text = "Forgot your password?",
        color = Color.Gray,
        modifier = Modifier
            .padding(top = 4.dp, bottom = 25.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
            .clickable { /* Implementar recuperación */ },
        fontWeight = FontWeight.Bold
    )
}

/**
 * Botón circular de login que se habilita solo con credenciales válidas.
 *
 * @param enabled Indica si el botón está habilitado
 * @param onClick Callback al pulsar el botón
 */
@Composable
private fun LoginButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    if (enabled) Color.Green.copy(alpha = 0.2f)
                    else Color.Gray.copy(alpha = 0.2f),
                    shape = CircleShape
                )
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(64.dp),
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Acceder a la aplicación",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}