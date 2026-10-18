package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    // Información ingresada por el usuario
    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // Controla si la contraseña se muestra o se oculta
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // Control de errores de los campos
    var fullNameError by remember {
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
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "D'Henrys",
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Completa tus datos para registrarte",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Campo: nombre completo
        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                fullNameError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Nombre completo")
            },
            placeholder = {
                Text(text = "Ingresa tu nombre")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_person
                    ),
                    contentDescription = "Nombre completo",
                    tint = TextSecondary
                )
            },
            singleLine = true,
            isError = fullNameError,
            supportingText = {
                if (fullNameError) {
                    Text(text = "Ingresa tu nombre completo")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                focusedLeadingIconColor = Primary,
                unfocusedLeadingIconColor = TextSecondary
            )
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Campo: correo electrónico
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
                    Text(text = "Ingresa un correo válido")
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
            modifier = Modifier.height(8.dp)
        )

        // Campo: contraseña
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
                Text(text = "Mínimo 6 caracteres")
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
                    Text(text = "Utiliza al menos 6 caracteres")
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
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                val nombreInvalido =
                    fullName.trim().length < 3

                val emailInvalido =
                    email.isBlank() || !email.contains("@")

                val passwordInvalido =
                    password.length < 6

                fullNameError = nombreInvalido
                emailError = emailInvalido
                passwordError = passwordInvalido

                if (
                    !nombreInvalido &&
                    !emailInvalido &&
                    !passwordInvalido
                ) {
                    onRegisterClick()
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
                text = "Crear cuenta",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = onLoginClick
        ) {
            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                color = Primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RestauranteAppTheme {
        RegisterScreen()
    }
}