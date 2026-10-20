package pe.cibertec.restaurante.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.model.CATEGORIAS
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.theme.BordeCampo
import pe.cibertec.restaurante.ui.theme.DVeranoTheme
import pe.cibertec.restaurante.ui.theme.FondoPantalla
import pe.cibertec.restaurante.ui.theme.TextoSecundario
import pe.cibertec.restaurante.ui.theme.VerdeOscuro
import pe.cibertec.restaurante.ui.util.aPrecioOrNull
import pe.cibertec.restaurante.ui.util.esPrecioParcialValido

@Composable
fun NewProductScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onGuardar: (Producto) -> Unit,
    productoEditar: Producto? = null
) {
    val context = LocalContext.current

    var imagenUri by remember(productoEditar?.id) {
        mutableStateOf<Uri?>(productoEditar?.imagenUri)
    }

    var nombre by remember(productoEditar?.id) {
        mutableStateOf(productoEditar?.nombre ?: "")
    }

    var categoria by remember(productoEditar?.id) {
        mutableStateOf(productoEditar?.categoria ?: "")
    }

    var categoriaExpandida by remember {
        mutableStateOf(false)
    }

    var precio by remember(productoEditar?.id) {
        mutableStateOf(
            productoEditar?.precio?.toString() ?: ""
        )
    }

    var descripcion by remember(productoEditar?.id) {
        mutableStateOf(productoEditar?.descripcion ?: "")
    }

    var disponible by remember(productoEditar?.id) {
        mutableStateOf(productoEditar?.disponible ?: true)
    }

    var errorNombre by remember {
        mutableStateOf(false)
    }

    var errorCategoria by remember {
        mutableStateOf(false)
    }

    var errorPrecio by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagenUri = uri
        }
    }

    val colorCampos = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        unfocusedBorderColor = BordeCampo,
        focusedBorderColor = VerdeOscuro
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = if (productoEditar == null) {
                    "Nuevo producto"
                } else {
                    "Editar producto"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // IMAGEN DEL PRODUCTO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFBDBDBD),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(16f, 12f)
                                )
                            ),
                            cornerRadius = CornerRadius(
                                12.dp.toPx()
                            )
                        )
                    }
                    .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri != null) {
                    val bitmap = remember(imagenUri) {
                        try {
                            context.contentResolver
                                .openInputStream(imagenUri!!)
                                ?.use { inputStream ->
                                    BitmapFactory.decodeStream(
                                        inputStream
                                    )?.asImageBitmap()
                                }
                        } catch (_: Exception) {
                            null
                        }
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Imagen del producto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_camera
                            ),
                            contentDescription = null,
                            tint = VerdeOscuro
                        )

                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Agregar imagen del producto",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = VerdeOscuro
                        )
                    }
                }
            }

            // NOMBRE
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Nombre del producto",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        if (it.isNotBlank()) {
                            errorNombre = false
                        }
                    },
                    placeholder = {
                        Text("Ej. Ceviche de pescado")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_food),
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colorCampos,
                    isError = errorNombre,
                    supportingText = {
                        if (errorNombre) {
                            Text("El nombre es obligatorio")
                        }
                    }
                )
            }

            // CATEGORÍA
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text("Selecciona una categoría")
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_down),
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos,
                        isError = errorCategoria,
                        supportingText = {
                            if (errorCategoria) {
                                Text("Selecciona una categoría")
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                categoriaExpandida = true
                            }
                    )

                    DropdownMenu(
                        expanded = categoriaExpandida,
                        onDismissRequest = {
                            categoriaExpandida = false
                        }
                    ) {
                        CATEGORIAS.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = item)
                                },
                                onClick = {
                                    categoria = item
                                    errorCategoria = false
                                    categoriaExpandida = false
                                }
                            )
                        }
                    }
                }
            }

            // PRECIO
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Precio (S/)",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = precio,
                    onValueChange = {
                        if (esPrecioParcialValido(it)) {
                            precio = it

                            val numero = it.aPrecioOrNull()

                            if (numero != null && numero > 0.0) {
                                errorPrecio = false
                            }
                        }
                    },
                    placeholder = {
                        Text("Ej. 25.00")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_card),
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colorCampos,
                    isError = errorPrecio,
                    supportingText = {
                        if (errorPrecio) {
                            Text(
                                text = if (precio.isBlank()) {
                                    "El precio es obligatorio"
                                } else {
                                    "Ingrese un precio válido mayor a 0"
                                }
                            )
                        }
                    }
                )
            }

            // DESCRIPCIÓN
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                    },
                    placeholder = {
                        Text("Describe los ingredientes, porciones...")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colorCampos
                )
            }

            // DISPONIBILIDAD
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Switch(
                    checked = disponible,
                    onCheckedChange = {
                        disponible = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = VerdeOscuro,
                        checkedThumbColor = Color.White
                    )
                )
            }
        }

        // GUARDAR
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeOscuro
            ),
            onClick = {
                val precioNumero = precio.aPrecioOrNull()

                errorNombre = nombre.isBlank()
                errorCategoria = categoria.isEmpty()
                errorPrecio = precioNumero == null ||
                        precioNumero <= 0.0

                if (
                    !errorNombre &&
                    !errorCategoria &&
                    !errorPrecio &&
                    precioNumero != null
                ) {
                    onGuardar(
                        Producto(
                            id = productoEditar?.id ?: 0,
                            nombre = nombre.trim(),
                            categoria = categoria,
                            precio = precioNumero,
                            descripcion = descripcion.trim(),
                            disponible = disponible,
                            imagenUri = imagenUri
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Revisa los campos marcados",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text(
                text = if (productoEditar == null) {
                    "Guardar producto"
                } else {
                    "Guardar cambios"
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewProductScreenPreview() {
    DVeranoTheme {
        NewProductScreen(
            modifier = Modifier.fillMaxSize(),
            onBack = {},
            onGuardar = {}
        )
    }
}