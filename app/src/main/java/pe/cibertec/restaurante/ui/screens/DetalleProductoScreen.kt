package pe.cibertec.restaurante.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.model.productosDemo
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme

@Composable
fun DetalleProductoScreen(
    modifier: Modifier = Modifier,
    producto: Producto,
    onBack: () -> Unit = {},
    onEditar: () -> Unit = {},
    onEliminar: () -> Unit = {}
) {
    var mostrarDialogo by remember(producto.id) {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    // Intenta cargar la imagen seleccionada para este producto.
    val imagenBitmap = remember(producto.imagenUri) {
        try {
            producto.imagenUri?.let { uri ->
                context.contentResolver
                    .openInputStream(uri)
                    ?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                            ?.asImageBitmap()
                    }
            }
        } catch (_: Exception) {
            null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ENCABEZADO Y REGRESO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
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
                text = "Detalle de producto",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // INFORMACIÓN DEL PRODUCTO
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // IMAGEN
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imagenBitmap != null) {

                    Image(
                        bitmap = imagenBitmap,
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                } else {

                    Text(
                        text = "🍽",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            // NOMBRE
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )

            // CATEGORÍA
            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // ESTADO
            Text(
                text = producto.estado,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (producto.estaActivo()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier.padding(top = 2.dp)
            )

            // PRECIO
            Text(
                text = producto.precioFormateado(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // DESCRIPCIÓN
            Text(
                text = "DESCRIPCIÓN",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = producto.descripcion.ifBlank {
                    "Sin descripción registrada."
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // EDITAR
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onEditar
            ) {
                Text("Editar producto")
            }

            // ELIMINAR
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    mostrarDialogo = true
                }
            ) {
                Text("Eliminar producto")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }

    // CONFIRMACIÓN DE ELIMINACIÓN
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
            },

            title = {
                Text("Eliminar producto")
            },

            text = {
                Text(
                    "¿Deseas eliminar ${producto.nombre}?"
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogo = false
                        onEliminar()
                    }
                ) {
                    Text("Sí, eliminar")
                }
            },

            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogo = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "Detalle producto D'Henry"
)
@Composable
private fun DetalleProductoScreenPreview() {
    RestauranteAppTheme {
        DetalleProductoScreen(
            producto = productosDemo[0]
        )
    }
}