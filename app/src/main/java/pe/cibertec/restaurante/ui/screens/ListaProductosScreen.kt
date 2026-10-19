package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.cibertec.restaurante.ui.components.ProductoItem
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.model.productosDemo
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme

private val VerdeProductos = Color(0xFF214D32)
private val FondoProductos = Color(0xFFF8F8F8)
private val TextoProductos = Color(0xFF222222)
private val TextoSecundarioProductos = Color(0xFF858985)

@Composable
fun ListaProductosScreen(
    modifier: Modifier = Modifier,
    productos: List<Producto> = productosDemo,
    onProductoClick: (Producto) -> Unit = {},
    onAgregarClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var busqueda by remember {
        mutableStateOf("")
    }

    val consulta = busqueda.trim()

    val productosFiltrados = productos.filter { producto ->
        producto.nombre.contains(
            consulta,
            ignoreCase = true
        ) || producto.categoria.contains(
            consulta,
            ignoreCase = true
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoProductos)
    ) {
        // ENCABEZADO
        Text(
            text = "Productos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 22.dp,
                    bottom = 10.dp
                ),
            fontSize = 25.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextoProductos
        )

        // BUSCADOR
        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                ),
            placeholder = {
                Text(
                    text = "Buscar producto...",
                    color = TextoSecundarioProductos
                )
            },
            leadingIcon = {
                Text(
                    text = "⌕",
                    fontSize = 24.sp,
                    color = VerdeProductos
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = VerdeProductos,
                unfocusedBorderColor = Color(0xFFE2E5E1),
                cursorColor = VerdeProductos
            )
        )

        // LISTA DE PRODUCTOS
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (productosFiltrados.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (productos.isEmpty()) {
                                "Todavía no hay productos registrados."
                            } else {
                                "No se encontraron productos."
                            },
                            color = TextoSecundarioProductos,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(
                    items = productosFiltrados,
                    key = { producto -> producto.id }
                ) { producto ->
                    ProductoItem(
                        producto = producto,
                        onClick = {
                            onProductoClick(producto)
                        }
                    )
                }
            }
        }

        // BOTÓN PARA AGREGAR PRODUCTO
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
                onClick = onAgregarClick,
                containerColor = VerdeProductos,
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
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "Productos D'Henry"
)
@Composable
private fun ListaProductosScreenPreview() {
    RestauranteAppTheme {
        ListaProductosScreen()
    }
}