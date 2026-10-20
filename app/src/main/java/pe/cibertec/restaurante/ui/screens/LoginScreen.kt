package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.theme.Border
import pe.cibertec.restaurante.ui.theme.Primary
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme
import pe.cibertec.restaurante.ui.theme.TextPrimary
import pe.cibertec.restaurante.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf(false)
    }

    var passwordError by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Logo de D'HENRY
        Image(
            painter = painterResource(
                id = R.drawable.ic_launcher_foreground
            ),
            contentDescription = "Logo de D'Henry",
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Ingresa tus datos para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // Campo de correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Correo electrónico")
            },
            placeholder = {
                Text(text = "ejemplo@correo.com")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_email
                    ),
                    contentDescription = "Correo electrónico",
                    tint = TextSecondary
                )
            },
            singleLine = true,
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text(
                        text = "Ingresa un correo válido"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                focusedLeadingIconColor = Primary,
                unfocusedLeadingIconColor = TextSecondary
            )
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contraseña")
            },
            placeholder = {
                Text(text = "Ingresa tu contraseña")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_lock
                    ),
                    contentDescription = "Contraseña",
                    tint = TextSecondary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) {
                                R.drawable.ic_visibility
                            } else {
                                R.drawable.ic_visibility_off
                            }
                        ),
                        contentDescription = if (passwordVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        tint = TextPrimary
                    )
                }
            },
            singleLine = true,
            isError = passwordError,
            supportingText = {
                if (passwordError) {
                    Text(
                        text = "La contraseña es obligatoria"
                    )
                }
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                focusedLeadingIconColor = Primary,
                unfocusedLeadingIconColor = TextSecondary
            )
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Botón para iniciar sesión
        Button(
            onClick = {
                val emailInvalido =
                    email.isBlank() || !email.contains("@")

                val passwordInvalido =
                    password.isBlank()

                emailError = emailInvalido
                passwordError = passwordInvalido

                if (!emailInvalido && !passwordInvalido) {
                    onLoginClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Text(
                text = "Iniciar sesión",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Acceso al registro
        TextButton(
            onClick = onRegisterClick
        ) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = Primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RestauranteAppTheme {
        LoginScreen()
    }
}