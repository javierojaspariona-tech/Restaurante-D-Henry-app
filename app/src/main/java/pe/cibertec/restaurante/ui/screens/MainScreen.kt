package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.model.Oferta
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.model.productosDemo
import pe.cibertec.restaurante.ui.producto.ProductoViewModel

data class NavigationItem(
    val title: String,
    val icon: Int
)

@Composable
fun MainScreen(
    productoViewModel: ProductoViewModel,
    onLogoutClick: () -> Unit = {}
) {
    var selectedItem by remember {
        mutableStateOf(0)
    }

    val productos = productoViewModel.productos

    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos()
    }

    var ofertas by remember {
        mutableStateOf(
            listOf(
                Oferta(
                    id = 1,
                    producto = productosDemo[0],
                    precioOferta = 18.00,
                    activa = true
                ),
                Oferta(
                    id = 2,
                    producto = productosDemo[1],
                    precioOferta = 14.00,
                    activa = true
                )
            )
        )
    }

    var productoSeleccionado by remember {
        mutableStateOf<Producto?>(null)
    }

    var modoNuevoProducto by remember {
        mutableStateOf(false)
    }

    var modoNuevaOferta by remember {
        mutableStateOf(false)
    }

    // Barra inferior según el diseño del PDF.
    val navigationItems = listOf(
        NavigationItem(
            title = "Inicio",
            icon = R.drawable.ic_restaurant
        ),
        NavigationItem(
            title = "Productos",
            icon = R.drawable.ic_products
        ),
        NavigationItem(
            title = "Ofertas",
            icon = R.drawable.ic_offer
        ),
        NavigationItem(
            title = "Perfil",
            icon = R.drawable.ic_person
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,

                        onClick = {
                            selectedItem = index
                            productoSeleccionado = null
                            modoNuevoProducto = false
                            modoNuevaOferta = false
                        },

                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.title
                            )
                        },

                        label = {
                            Text(text = item.title)
                        },

                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor =
                                MaterialTheme.colorScheme.primary,
                            indicatorColor =
                                MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        when (selectedItem) {

            // 0. INICIO
            0 -> {

            }

            // 1. PRODUCTOS
            1 -> {
                if (modoNuevoProducto) {

                    NewProductScreen(
                        modifier = Modifier.padding(innerPadding),

                        onBack = {
                            modoNuevoProducto = false
                        },

                        onGuardar = { nuevoProducto ->
                            productoViewModel.guardarProducto(
                                nuevoProducto
                            )

                            modoNuevoProducto = false
                        }
                    )

                } else if (productoSeleccionado == null) {

                    ListaProductosScreen(
                        modifier = Modifier.padding(innerPadding),

                        productos = productos,

                        onProductoClick = { producto ->
                            productoSeleccionado = producto
                        },

                        onAgregarClick = {
                            modoNuevoProducto = true
                        },

                        onBack = {
                            selectedItem = 0
                        }
                    )

                } else {

                    DetalleProductoScreen(
                        modifier = Modifier.padding(innerPadding),

                        producto = productoSeleccionado!!,

                        onBack = {
                            productoSeleccionado = null
                        },

                        onEditar = {
                            // Pendiente de conectar con la edición.
                        },

                        onEliminar = {
                            productoViewModel.eliminarProducto(
                                productoSeleccionado!!
                            )

                            productoSeleccionado = null
                        }
                    )
                }
            }

            // 2. OFERTAS
            2 -> {
                if (modoNuevaOferta) {

                    NewOfferScreen(
                        modifier = Modifier.padding(innerPadding),

                        // Solo productos que todavía no tienen una oferta
                        productos = productos.filter { producto ->
                            ofertas.none { it.producto.id == producto.id }
                        },

                        onBack = {
                            modoNuevaOferta = false
                        },

                        onIrANuevoProducto = {
                            modoNuevaOferta = false
                            selectedItem = 1
                            modoNuevoProducto = true
                        },

                        onGuardar = { nuevaOferta ->
                            // Seguridad: un producto solo puede tener una oferta
                            val yaTieneOferta = ofertas.any {
                                it.producto.id == nuevaOferta.producto.id
                            }

                            if (!yaTieneOferta) {
                                val nuevoId =
                                    (ofertas.maxOfOrNull { it.id } ?: 0) + 1

                                val ofertaFinal = nuevaOferta.copy(
                                    id = nuevoId
                                )

                                ofertas = ofertas + ofertaFinal
                            }

                            modoNuevaOferta = false
                        }
                    )

                } else {

                    OffersScreen(
                        modifier = Modifier.padding(innerPadding),

                        ofertas = ofertas,

                        onBack = {
                            selectedItem = 0
                        },

                        onAgregarOferta = {
                            modoNuevaOferta = true
                        },

                        onToggleActiva = { oferta ->
                            ofertas = ofertas.map {
                                if (it.id == oferta.id) {
                                    it.copy(activa = !it.activa)
                                } else {
                                    it
                                }
                            }
                        },

                        onModificarPrecio = { oferta, nuevoPrecio ->
                            ofertas = ofertas.map {
                                if (it.id == oferta.id) {
                                    it.copy(precioOferta = nuevoPrecio)
                                } else {
                                    it
                                }
                            }
                        },

                        onEliminar = { oferta ->
                            ofertas = ofertas.filter { it.id != oferta.id }
                        }
                    )
                }
            }

            // 3. PERFIL
            3 -> {
                PerfilScreen(
                    modifier = Modifier.padding(innerPadding),
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun TemporaryScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}