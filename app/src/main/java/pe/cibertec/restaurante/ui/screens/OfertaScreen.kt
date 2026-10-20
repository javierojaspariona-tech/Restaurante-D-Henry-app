package pe.cibertec.restaurante.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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

private enum class FiltroOferta(val titulo: String) {
    TODAS("Todas"),
    ACTIVAS("Activas"),
    INACTIVAS("Inactivas")
}

@Composable
fun OffersScreen(
    modifier: Modifier = Modifier,
    ofertas: List<Oferta>,
    onBack: () -> Unit = {},
    onAgregarOferta: () -> Unit = {},
    onToggleActiva: (Oferta) -> Unit = {},
    onModificarPrecio: (Oferta, Double) -> Unit = { _, _ -> },
    onEliminar: (Oferta) -> Unit = {},
) {
    var filtro by remember { mutableStateOf(FiltroOferta.TODAS) }
    var expandidaId by remember { mutableStateOf<Int?>(null) }
    var ofertaAEditar by remember { mutableStateOf<Oferta?>(null) }
    var ofertaAEliminar by remember { mutableStateOf<Oferta?>(null) }

    val totalActivas = ofertas.count { it.activa }
    val totalInactivas = ofertas.size - totalActivas

    val visibles = ofertas.filter {
        when (filtro) {
            FiltroOferta.TODAS -> true
            FiltroOferta.ACTIVAS -> it.activa
            FiltroOferta.INACTIVAS -> !it.activa
        }
    }

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
                text = "Ofertas",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ENCABEZADO SECCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_offer),
                contentDescription = null,
                tint = VerdeOscuro,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 12.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Productos en promoción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Gestiona los platos que estarán disponibles en la sección de ofertas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
        }

        // FILTRO: TODAS / ACTIVAS / INACTIVAS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FiltroOferta.entries.forEach { opcion ->
                val cantidad = when (opcion) {
                    FiltroOferta.TODAS -> ofertas.size
                    FiltroOferta.ACTIVAS -> totalActivas
                    FiltroOferta.INACTIVAS -> totalInactivas
                }
                FilterChip(
                    selected = filtro == opcion,
                    onClick = {
                        filtro = opcion
                        expandidaId = null
                    },
                    label = { Text(text = "${opcion.titulo} ($cantidad)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeOscuro,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (visibles.isEmpty()) {
                val sinOfertas = ofertas.isEmpty()

                EstadoVacioOfertas(
                    modifier = Modifier.align(Alignment.Center),
                    titulo = when {
                        sinOfertas -> "Aún no hay ofertas"
                        filtro == FiltroOferta.ACTIVAS -> "No hay ofertas activas"
                        else -> "No hay ofertas inactivas"
                    },
                    mensaje = when {
                        sinOfertas -> "Toca el botón + para crear la primera."
                        filtro == FiltroOferta.ACTIVAS -> "Activa una oferta tocando su estado en la lista."
                        else -> "Todas tus ofertas están activas."
                    },
                    textoBoton = if (sinOfertas) null else "Ver todas las ofertas",
                    onBoton = { filtro = FiltroOferta.TODAS }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibles, key = { it.id }) { oferta ->
                        OfertaItem(
                            oferta = oferta,
                            expandida = expandidaId == oferta.id,
                            onClick = {
                                expandidaId = if (expandidaId == oferta.id) null else oferta.id
                            },
                            onToggleActiva = { onToggleActiva(oferta) },
                            onModificar = { ofertaAEditar = oferta },
                            onEliminar = { ofertaAEliminar = oferta }
                        )
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = onAgregarOferta,
                shape = CircleShape,
                containerColor = VerdeOscuro,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Agregar oferta",
                )
            }
        }
    }

    // MODIFICAR PRECIO DE OFERTA / DESCUENTO
    ofertaAEditar?.let { oferta ->
        ModificarPrecioDialog(
            oferta = oferta,
            onDismiss = { ofertaAEditar = null },
            onGuardar = { nuevoPrecio ->
                onModificarPrecio(oferta, nuevoPrecio)
                ofertaAEditar = null
                expandidaId = null
            }
        )
    }

    // CONFIRMACIÓN ANTES DE ELIMINAR
    ofertaAEliminar?.let { oferta ->
        AlertDialog(
            onDismissRequest = { ofertaAEliminar = null },
            title = { Text(text = "Eliminar oferta") },
            text = {
                Text(
                    text = "¿Seguro que quieres eliminar la oferta de " +
                            "${oferta.producto.nombre}? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar(oferta)
                        ofertaAEliminar = null
                        expandidaId = null
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { ofertaAEliminar = null }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EstadoVacioOfertas(
    modifier: Modifier = Modifier,
    titulo: String,
    mensaje: String,
    textoBoton: String? = null,
    onBoton: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_offer),
                contentDescription = null,
                tint = VerdeOscuro,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        if (textoBoton != null) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, VerdeOscuro),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeOscuro),
                onClick = onBoton
            ) {
                Text(text = textoBoton, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun OfertaItem(
    oferta: Oferta,
    expandida: Boolean,
    onClick: () -> Unit,
    onToggleActiva: () -> Unit,
    onModificar: () -> Unit,
    onEliminar: () -> Unit,
) {
    val context = LocalContext.current
    val bitmap = remember(oferta.producto.imagenUri) {
        oferta.producto.imagenUri?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeCampo)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        bitmap != null -> Image(
                            bitmap = bitmap,
                            contentDescription = oferta.producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        oferta.producto.imagenRes != null -> Image(
                            painter = painterResource(id = oferta.producto.imagenRes),
                            contentDescription = oferta.producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        else -> Icon(
                            painter = painterResource(id = R.drawable.ic_food),
                            contentDescription = oferta.producto.nombre,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = oferta.producto.nombre,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = oferta.producto.categoria,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = formatearPrecio(oferta.precioOferta),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = VerdeOscuro
                        )
                        Text(
                            text = formatearPrecio(oferta.precioOriginal),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // BOTÓN DE ESTADO: al tocarlo cambia entre Activo e Inactivo
                val colorEstado = if (oferta.activa) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (oferta.activa) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                        )
                        .border(1.dp, colorEstado, RoundedCornerShape(8.dp))
                        .clickable { onToggleActiva() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (oferta.activa) "Activo" else "Inactivo",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorEstado
                    )
                }
            }

            // BOTONES (aparecen al tocar la oferta): Modificar + Eliminar
            if (expandida) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro),
                        onClick = onModificar
                    ) {
                        Text(text = "Modificar", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = onEliminar
                    ) {
                        Text(text = "Eliminar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Descuento (%) con un decimal, entre 0 y 100.
private fun descuentoDe(precio: Double, original: Double): Double {
    if (original <= 0.0) return 0.0
    val d = (1 - precio / original) * 100
    return ((d * 10).roundToInt() / 10.0).coerceIn(0.0, 100.0)
}

private fun formatearDescuento(d: Double): String {
    return if (d % 1.0 == 0.0) d.toInt().toString() else String.format(Locale.US, "%.1f", d)
}

private fun esDescuentoParcialValido(input: String): Boolean {
    return Regex("^\\d{0,2}[.,]?\\d?$").matches(input)
}

@Composable
private fun ModificarPrecioDialog(
    oferta: Oferta,
    onDismiss: () -> Unit,
    onGuardar: (Double) -> Unit,
) {
    val original = oferta.precioOriginal

    var precioTexto by remember {
        mutableStateOf(String.format(Locale.US, "%.2f", oferta.precioOferta))
    }
    var descuentoTexto by remember {
        mutableStateOf(formatearDescuento(descuentoDe(oferta.precioOferta, original)))
    }

    val precioNumero = precioTexto.aPrecioOrNull()
    val mensajeError: String? = when {
        precioTexto.isBlank() -> "Ingresa un precio"
        precioNumero == null -> "Ingresa un precio válido mayor a 0"
        precioNumero <= 0.0 -> "Ingresa un precio válido mayor a 0"
        precioNumero >= original -> "Debe ser menor al precio original (${formatearPrecio(original)})"
        else -> null
    }
    val precioValido = mensajeError == null && precioNumero != null

    val colorCampos = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VerdeOscuro,
        unfocusedBorderColor = BordeCampo
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Modificar oferta") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${oferta.producto.nombre} · Precio original ${formatearPrecio(original)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = precioTexto,
                    onValueChange = { nuevo ->
                        if (esPrecioParcialValido(nuevo)) {
                            precioTexto = nuevo
                            val p = nuevo.aPrecioOrNull()
                            descuentoTexto = if (p != null) {
                                formatearDescuento(descuentoDe(p, original))
                            } else {
                                ""
                            }
                        }
                    },
                    label = { Text(text = "Precio de oferta (S/)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colorCampos,
                    isError = mensajeError != null,
                    supportingText = {
                        if (mensajeError != null) Text(text = mensajeError)
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descuentoTexto,
                    onValueChange = { nuevo ->
                        if (esDescuentoParcialValido(nuevo)) {
                            descuentoTexto = nuevo
                            val d = nuevo.aPrecioOrNull()
                            precioTexto = if (d != null && d < 100.0) {
                                String.format(Locale.US, "%.2f", original * (1 - d / 100))
                            } else {
                                ""
                            }
                        }
                    },
                    label = { Text(text = "Descuento (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colorCampos
                )

                if (precioValido && precioNumero != null) {
                    Text(
                        text = "Ahorras ${formatearPrecio(original - precioNumero)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VerdeOscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = precioValido,
                onClick = {
                    if (precioNumero != null) {
                        onGuardar((precioNumero * 100).roundToInt() / 100.0)
                    }
                }
            ) {
                Text(text = "Guardar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun OffersScreenPreview() {
    val p1 = Producto(1, "Hamburguesa Especial", "Platos de fondo", 15.0, "", "Activo", disponible = true, imagenRes = R.drawable.img_comida1)
    val p2 = Producto(2, "Combo Familiar", "Platos de fondo", 55.0, "", "Activo", disponible = true, imagenRes = R.drawable.img_comida2)
    val p3 = Producto(3, "Ensalada Fresca", "Entradas", 12.0, "", "Activo", disponible = true, imagenRes = R.drawable.img_comida3)
    DVeranoTheme {
        OffersScreen(
            modifier = Modifier.fillMaxSize(),
            ofertas = listOf(
                Oferta(1, p1, 12.0, true),
                Oferta(2, p2, 45.0, true),
                Oferta(3, p3, 9.0, false),
            ),
            onBack = {},
            onAgregarOferta = {},
            onToggleActiva = {},
            onModificarPrecio = { _, _ -> },
            onEliminar = {},
        )
    }
}