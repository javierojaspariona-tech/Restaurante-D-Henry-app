package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.components.OfertaItem
import pe.cibertec.restaurante.ui.model.Oferta
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.theme.BordeCampo
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme
import pe.cibertec.restaurante.ui.util.aPrecioOrNull
import pe.cibertec.restaurante.ui.util.esPrecioParcialValido
import pe.cibertec.restaurante.ui.util.formatearPrecio
import java.util.Locale
import kotlin.math.roundToInt

// Mismos colores que usan Inicio y Productos.
private val VerdeOfertas = Color(0xFF214D32)
private val VerdeIconoOfertas = Color(0xFFD0F7B0)
private val FondoOfertas = Color(0xFFF8F8F8)
private val TextoOfertas = Color(0xFF222222)
private val TextoSecundarioOfertas = Color(0xFF858985)

private enum class FiltroOferta(val titulo: String) {
    TODAS("Todas"),
    ACTIVAS("Activas"),
    INACTIVAS("Inactivas")
}

@Composable
fun OffersScreen(
    modifier: Modifier = Modifier,
    ofertas: List<Oferta>,
    // Se conserva por compatibilidad: Ofertas es una pestaña principal,
    // igual que Productos, y no muestra flecha de retroceso.
    onBack: () -> Unit = {},
    onAgregarOferta: () -> Unit = {},
    onToggleActiva: (Oferta) -> Unit = {},
    onModificarPrecio: (Oferta, Double) -> Unit = { _, _ -> },
    onEliminar: (Oferta) -> Unit = {}
) {
    var filtro by remember {
        mutableStateOf(FiltroOferta.TODAS)
    }

    var expandidaId by remember {
        mutableStateOf<Int?>(null)
    }

    var ofertaAEditar by remember {
        mutableStateOf<Oferta?>(null)
    }

    var ofertaAEliminar by remember {
        mutableStateOf<Oferta?>(null)
    }

    val totalActivas = ofertas.count { it.activa }
    val totalInactivas = ofertas.size - totalActivas

    val ofertasFiltradas = ofertas.filter { oferta ->
        when (filtro) {
            FiltroOferta.TODAS -> true
            FiltroOferta.ACTIVAS -> oferta.activa
            FiltroOferta.INACTIVAS -> !oferta.activa
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoOfertas)
    ) {
        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 22.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VerdeIconoOfertas),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tag_price),
                    contentDescription = "Ofertas",
                    tint = VerdeOfertas,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ofertas",
                    fontSize = 25.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOfertas
                )

                Text(
                    text = "Revisa y configura promociones",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextoSecundarioOfertas
                )

                Text(
                    text = "Toca el estado para cambiarlo o pulsa la tarjeta para modificar/eliminar",
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = TextoSecundarioOfertas,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // FILTRO: TODAS / ACTIVAS / INACTIVAS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                ),
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
                    label = {
                        Text(text = "${opcion.titulo} ($cantidad)")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeOfertas,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // LISTA DE OFERTAS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (ofertasFiltradas.isEmpty()) {
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
                    onBoton = {
                        filtro = FiltroOferta.TODAS
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 18.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = ofertasFiltradas,
                        key = { oferta -> oferta.id }
                    ) { oferta ->
                        OfertaItem(
                            oferta = oferta,
                            expandida = expandidaId == oferta.id,
                            onClick = {
                                expandidaId = if (expandidaId == oferta.id) {
                                    null
                                } else {
                                    oferta.id
                                }
                            },
                            onToggleActiva = {
                                onToggleActiva(oferta)
                            },
                            onModificar = {
                                ofertaAEditar = oferta
                            },
                            onEliminar = {
                                ofertaAEliminar = oferta
                            }
                        )
                    }
                }
            }
        }

        // BOTÓN PARA AGREGAR OFERTA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = onAgregarOferta,
                containerColor = VerdeOfertas,
                contentColor = Color.White
            ) {
                Text(
                    text = "+",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }

    // MODIFICAR PRECIO DE OFERTA / DESCUENTO
    ofertaAEditar?.let { oferta ->
        ModificarPrecioDialog(
            oferta = oferta,
            onDismiss = {
                ofertaAEditar = null
            },
            onGuardar = { nuevoPrecio ->
                onModificarPrecio(oferta, nuevoPrecio)
                ofertaAEditar = null
                expandidaId = null
            }
        )
    }

    // CONFIRMACIÓN DE ELIMINACIÓN
    ofertaAEliminar?.let { oferta ->
        AlertDialog(
            onDismissRequest = {
                ofertaAEliminar = null
            },
            title = {
                Text("Eliminar oferta")
            },
            text = {
                Text("¿Deseas eliminar la oferta de ${oferta.producto.nombre}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar(oferta)
                        ofertaAEliminar = null
                        expandidaId = null
                    }
                ) {
                    Text("Sí, eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        ofertaAEliminar = null
                    }
                ) {
                    Text("Cancelar")
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
    onBoton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_tag_price),
                contentDescription = null,
                modifier = Modifier.size(46.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (textoBoton != null) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                onClick = onBoton
            ) {
                Text(
                    text = textoBoton,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
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
    return if (d % 1.0 == 0.0) {
        d.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", d)
    }
}

private fun esDescuentoParcialValido(input: String): Boolean {
    return Regex("^\\d{0,2}[.,]?\\d?$").matches(input)
}

@Composable
private fun ModificarPrecioDialog(
    oferta: Oferta,
    onDismiss: () -> Unit,
    onGuardar: (Double) -> Unit
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
        precioTexto.isBlank() -> "El precio de oferta es obligatorio"
        precioNumero == null -> "Ingrese un precio válido mayor a 0"
        precioNumero <= 0.0 -> "Ingrese un precio válido mayor a 0"
        precioNumero >= original ->
            "Debe ser menor al precio original (${formatearPrecio(original)})"
        else -> null
    }

    val precioValido = mensajeError == null && precioNumero != null

    // Mismos colores de campos que los formularios de producto y oferta.
    val colorCampos = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        unfocusedBorderColor = BordeCampo,
        focusedBorderColor = VerdeOfertas
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Modificar oferta")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${oferta.producto.nombre} · Precio original ${formatearPrecio(original)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundarioOfertas
                )

                // PRECIO DE OFERTA
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Precio de oferta (S/)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundarioOfertas
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
                        placeholder = {
                            Text("Ej. 18.00")
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_offer),
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos,
                        isError = mensajeError != null,
                        supportingText = {
                            if (mensajeError != null) {
                                Text(mensajeError)
                            }
                        }
                    )
                }

                // DESCUENTO
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Descuento (%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundarioOfertas
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = descuentoTexto,
                        onValueChange = { nuevo ->
                            if (esDescuentoParcialValido(nuevo)) {
                                descuentoTexto = nuevo

                                val d = nuevo.aPrecioOrNull()

                                precioTexto = if (d != null && d < 100.0) {
                                    String.format(
                                        Locale.US,
                                        "%.2f",
                                        original * (1 - d / 100)
                                    )
                                } else {
                                    ""
                                }
                            }
                        },
                        placeholder = {
                            Text("Ej. 20")
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_tag_price),
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = colorCampos
                    )
                }

                if (precioValido && precioNumero != null) {
                    Text(
                        text = "Ahorras ${formatearPrecio(original - precioNumero)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOfertas
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = precioValido,
                onClick = {
                    if (precioNumero != null) {
                        onGuardar((precioNumero * 100).roundToInt() / 100.0)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "Ofertas D'Henry"
)
@Composable
private fun OffersScreenPreview() {
    val p1 = Producto(1, "Lomo Saltado", "Platos principales", 22.0, "", "Activo")
    val p2 = Producto(2, "Ají de Gallina", "Platos principales", 18.0, "", "Activo")
    val p3 = Producto(3, "Hamburguesa", "Comidas rápidas", 15.0, "", "Activo")

    RestauranteAppTheme {
        OffersScreen(
            ofertas = listOf(
                Oferta(1, p1, 18.0, true),
                Oferta(2, p2, 14.0, true),
                Oferta(3, p3, 12.0, false)
            )
        )
    }
}