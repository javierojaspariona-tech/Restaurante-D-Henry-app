package pe.cibertec.restaurante.ui.screens

import android.graphics.BitmapFactory
import android.widget.Toast
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.model.Oferta
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.theme.BordeCampo
import pe.cibertec.restaurante.ui.theme.DVeranoTheme
import pe.cibertec.restaurante.ui.theme.FondoPantalla
import pe.cibertec.restaurante.ui.theme.TextoSecundario
import pe.cibertec.restaurante.ui.theme.VerdeOscuro
import pe.cibertec.restaurante.ui.util.aPrecioOrNull
import pe.cibertec.restaurante.ui.util.esPrecioParcialValido
import pe.cibertec.restaurante.ui.util.formatearPrecio
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun NewOfferScreen(
    modifier: Modifier = Modifier,
    productos: List<Producto>,
    onBack: () -> Unit,
    onIrANuevoProducto: () -> Unit,
    onGuardar: (Oferta) -> Unit,
) {
    val context = LocalContext.current

    var productoId by remember { mutableStateOf<Int?>(null) }
    var productoExpandido by remember { mutableStateOf(false) }
    var precioOferta by remember { mutableStateOf("") }
    var activa by remember { mutableStateOf(true) }
    var mostrarErrores by remember { mutableStateOf(false) }

    val producto = productos.firstOrNull { it.id == productoId }
    val precioNumero = precioOferta.aPrecioOrNull()

    val errorProducto = mostrarErrores && producto == null
    val mensajePrecio: String? = if (!mostrarErrores) null else when {
        precioOferta.isBlank() -> "El precio de oferta es obligatorio"
        precioNumero == null || precioNumero <= 0.0 -> "Ingrese un precio válido mayor a 0"
        producto != null && precioNumero >= producto.precio ->
            "Debe ser menor al precio original (${formatearPrecio(producto.precio)})"
        else -> null
    }

    val colorCampos = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        unfocusedBorderColor = BordeCampo,
        focusedBorderColor = VerdeOscuro,
        disabledContainerColor = Color.White,
        disabledBorderColor = BordeCampo,
        disabledTextColor = TextoSecundario,
        disabledLeadingIconColor = TextoSecundario,
        disabledPlaceholderColor = TextoSecundario,
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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Nueva oferta",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (productos.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No hay productos disponibles para crear una oferta. " +
                            "Registra un producto nuevo (cada producto tiene una sola oferta).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro),
                    onClick = onIrANuevoProducto
                ) {
                    Text(text = "Registrar producto", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Imagen del producto elegido
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
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f))
                                ),
                                cornerRadius = CornerRadius(12.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (producto != null) {
                        val bitmap = remember(producto.imagenUri) {
                            producto.imagenUri?.let { uri ->
                                try {
                                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                                    }
                                } catch (_: Exception) {
                                    null
                                }
                            }
                        }
                        when {
                            bitmap != null -> Image(
                                bitmap = bitmap,
                                contentDescription = producto.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            producto.imagenRes != null -> Image(
                                painter = painterResource(id = producto.imagenRes),
                                contentDescription = producto.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            else -> Icon(
                                painter = painterResource(R.drawable.ic_food),
                                contentDescription = producto.nombre,
                                tint = VerdeOscuro,
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(R.drawable.ic_food),
                                contentDescription = null,
                                tint = VerdeOscuro,
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "Selecciona un producto",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = VerdeOscuro
                            )
                        }
                    }
                }

                // Producto
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Producto",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = producto?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text(text = "Selecciona un producto") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_food),
                                    contentDescription = null,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_down),
                                    contentDescription = null,
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = colorCampos,
                            isError = errorProducto,
                            supportingText = {
                                if (errorProducto) Text(text = "Selecciona un producto")
                            },
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { productoExpandido = true }
                        )
                        DropdownMenu(
                            expanded = productoExpandido,
                            onDismissRequest = { productoExpandido = false }
                        ) {
                            productos.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = "${item.nombre} — ${formatearPrecio(item.precio)}") },
                                    onClick = {
                                        productoId = item.id
                                        productoExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Categoría (solo lectura, viene del producto)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Categoría",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = producto?.categoria ?: "",
                        onValueChange = {},
                        enabled = false,
                        placeholder = { Text(text = "Se completa al elegir el producto") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = null,
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos,
                    )
                }

                // Precio original (solo lectura, viene del producto)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Precio original (S/)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = producto?.let { "%.2f".format(Locale.US, it.precio) } ?: "",
                        onValueChange = {},
                        enabled = false,
                        placeholder = { Text(text = "Se completa al elegir el producto") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_card),
                                contentDescription = null,
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos,
                    )
                }

                // Precio de oferta
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Precio de oferta (S/)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = precioOferta,
                        onValueChange = { if (esPrecioParcialValido(it)) precioOferta = it },
                        placeholder = { Text(text = "Ej. 18.00") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_offer),
                                contentDescription = null,
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos,
                        isError = mensajePrecio != null,
                        supportingText = {
                            if (mensajePrecio != null) {
                                Text(text = mensajePrecio)
                            } else if (producto != null && precioNumero != null &&
                                precioNumero > 0.0 && precioNumero < producto.precio
                            ) {
                                val descuento = ((1 - precioNumero / producto.precio) * 100).roundToInt()
                                Text(text = "Descuento: $descuento%", color = VerdeOscuro)
                            }
                        },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Activa",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = activa,
                        onCheckedChange = { activa = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = VerdeOscuro,
                            checkedThumbColor = Color.White,
                        )
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro),
                onClick = {
                    mostrarErrores = true
                    if (producto != null && precioNumero != null &&
                        precioNumero > 0.0 && precioNumero < producto.precio
                    ) {
                        onGuardar(
                            Oferta(
                                id = 0,
                                producto = producto,
                                precioOferta = precioNumero,
                                activa = activa,
                            )
                        )
                    } else {
                        Toast.makeText(context, "Revisa los campos marcados", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "Guardar oferta",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewOfferScreenPreview() {
    DVeranoTheme {
        NewOfferScreen(
            modifier = Modifier.fillMaxSize(),
            productos = listOf(
                Producto(1, "Ceviche de pescado", "Entradas", 25.0, "", "Activo", disponible = true),
                Producto(2, "Hamburguesa Especial", "Comidas rápidas", 15.0, "", "Activo", disponible = true, imagenRes = R.drawable.img_comida1),
            ),
            onBack = {},
            onIrANuevoProducto = {},
            onGuardar = {},
        )
    }
}
