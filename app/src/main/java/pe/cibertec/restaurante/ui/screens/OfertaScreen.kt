package pe.cibertec.restaurante.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import pe.cibertec.restaurante.ui.util.formatearPrecio

@Composable
fun OffersScreen(
    modifier: Modifier = Modifier,
    ofertas: List<Oferta>,
    onBack: () -> Unit = {},
    onAgregarOferta: () -> Unit = {},
    onOfertaClick: (Oferta) -> Unit = {},
) {
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

        Box(modifier = Modifier.weight(1f)) {
            if (ofertas.isEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    text = "Aún no hay ofertas.\nToca + para crear la primera.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ofertas, key = { it.id }) { oferta ->
                        OfertaItem(
                            oferta = oferta,
                            onClick = { onOfertaClick(oferta) }
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
}

@Composable
private fun OfertaItem(
    oferta: Oferta,
    onClick: () -> Unit,
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

            // ETIQUETA ESTADO CON PATRÓN DE COLOR DE PRODUCTO ITEM
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (oferta.activa) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (oferta.activa) "Activo" else "Inactivo",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (oferta.activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
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
            onOfertaClick = {},
        )
    }
}
