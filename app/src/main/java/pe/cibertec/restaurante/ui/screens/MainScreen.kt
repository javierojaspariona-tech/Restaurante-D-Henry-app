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
import pe.cibertec.restaurante.ui.model.Producto
import pe.cibertec.restaurante.ui.oferta.OfertaViewModel
import pe.cibertec.restaurante.ui.producto.ProductoViewModel

data class NavigationItem(
    val title: String,
    val icon: Int
)

@Composable
fun MainScreen(
    productoViewModel: ProductoViewModel,
    ofertaViewModel: OfertaViewModel,
    onLogoutClick: () -> Unit = {}
) {
    var selectedItem by remember {
        mutableStateOf(0)
    }

    val productos = productoViewModel.productos

    // Las ofertas ahora se guardan en la base de datos, igual que los productos.
    val ofertas = ofertaViewModel.ofertas

    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos()
        ofertaViewModel.cargarOfertas()
    }

    // Al entrar a Ofertas se recargan para reflejar cambios en los productos (precio editado o producto eliminado).
    LaunchedEffect(selectedItem) {
        if (selectedItem == 2) {
            ofertaViewModel.cargarOfertas()
        }
    }

    // Producto que se está consultando.
    var productoSeleccionado by remember {
        mutableStateOf<Producto?>(null)
    }

    // Producto que se está editando.
    var productoEditando by remember {
        mutableStateOf<Producto?>(null)
    }

    var modoNuevoProducto by remember {
        mutableStateOf(false)
    }

    var modoNuevaOferta by remember {
        mutableStateOf(false)
    }

    // Navegación inferior del diseño acordado.
    val navigationItems = listOf(
        NavigationItem(
            title = "Inicio",
            icon = R.drawable.ic_home
        ),
        NavigationItem(
            title = "Productos",
            icon = R.drawable.ic_products
        ),
        NavigationItem(
            title = "Ofertas",
            icon = R.drawable.ic_tag_price
        ),
        NavigationItem(
            title = "Perfil",
            icon = R.drawable.ic_person
        )
    )

    Scaffold(
        bottomBar = {
            val mostrarBarraNavegacion =
                !(selectedItem == 1 &&
                        productoSeleccionado != null &&
                        !modoNuevoProducto)

            if (mostrarBarraNavegacion) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItem == index,

                            onClick = {
                                selectedItem = index
                                productoSeleccionado = null
                                productoEditando = null
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
        }
    ) { innerPadding ->

        when (selectedItem) {

            // INICIO
            0 -> {
                InicioScreen(
                    modifier = Modifier.padding(innerPadding),

                    onProductosClick = {
                        selectedItem = 1
                        productoSeleccionado = null
                        productoEditando = null
                        modoNuevoProducto = false
                        modoNuevaOferta = false
                    },

                    onOfertasClick = {
                        selectedItem = 2
                        productoSeleccionado = null
                        productoEditando = null
                        modoNuevoProducto = false
                        modoNuevaOferta = false
                    },

                    onPerfilClick = {
                        selectedItem = 3
                        productoSeleccionado = null
                        productoEditando = null
                        modoNuevoProducto = false
                        modoNuevaOferta = false
                    }
                )
            }

            // PRODUCTOS
            1 -> {
                if (modoNuevoProducto) {

                    NewProductScreen(
                        modifier = Modifier.padding(innerPadding),

                        onBack = {
                            // Si cancela la edición,
                            // regresa al detalle anterior.
                            modoNuevoProducto = false
                            productoEditando = null
                        },

                        onGuardar = { productoGuardado ->
                            val estabaEditando =
                                productoEditando != null

                            // El ViewModel decide si inserta
                            // o actualiza según el ID.
                            productoViewModel.guardarProducto(
                                productoGuardado
                            )

                            // Al editar, vuelve al detalle actualizado.
                            // Al crear, vuelve al listado.
                            productoSeleccionado =
                                if (estabaEditando) {
                                    productoGuardado
                                } else {
                                    null
                                }

                            modoNuevoProducto = false
                            productoEditando = null
                        },

                        productoEditar = productoEditando
                    )

                } else if (productoSeleccionado == null) {

                    ListaProductosScreen(
                        modifier = Modifier.padding(innerPadding),

                        productos = productos,

                        onProductoClick = { producto ->
                            productoSeleccionado = producto
                            productoEditando = null
                        },

                        onAgregarClick = {
                            productoSeleccionado = null
                            productoEditando = null
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
                            // Vuelve al listado.
                            productoSeleccionado = null
                            productoEditando = null
                        },

                        onEditar = {
                            // Conserva el producto seleccionado
                            // para poder volver a su detalle.
                            productoEditando = productoSeleccionado
                            modoNuevoProducto = true
                        },

                        onEliminar = {
                            val productoAEliminar =
                                productoSeleccionado

                            if (productoAEliminar != null) {
                                productoViewModel.eliminarProducto(
                                    productoAEliminar
                                )
                            }

                            productoSeleccionado = null
                            productoEditando = null
                        }
                    )
                }
            }

            // OFERTAS
            2 -> {
                if (modoNuevaOferta) {

                    NewOfferScreen(
                        modifier = Modifier.padding(innerPadding),

                        // Evita ofrecer un producto que ya tiene oferta.
                        productos = productos.filter { producto ->
                            ofertas.none {
                                it.producto.id == producto.id
                            }
                        },

                        onBack = {
                            modoNuevaOferta = false
                        },

                        onIrANuevoProducto = {
                            modoNuevaOferta = false
                            selectedItem = 1
                            productoSeleccionado = null
                            productoEditando = null
                            modoNuevoProducto = true
                        },

                        onGuardar = { nuevaOferta ->

                            // El ViewModel inserta la oferta (ID 0).
                            // La base de datos también impide repetir
                            // una oferta para el mismo producto.
                            val yaTieneOferta = ofertas.any {
                                it.producto.id == nuevaOferta.producto.id
                            }

                            if (!yaTieneOferta) {
                                ofertaViewModel.guardarOferta(nuevaOferta)
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

                        // Activar o desactivar una oferta.
                        onToggleActiva = { oferta ->
                            ofertaViewModel.guardarOferta(
                                oferta.copy(activa = !oferta.activa)
                            )
                        },

                        // Actualizar el precio de una oferta.
                        onModificarPrecio = { oferta, nuevoPrecio ->
                            ofertaViewModel.guardarOferta(
                                oferta.copy(precioOferta = nuevoPrecio)
                            )
                        },

                        // Eliminar una oferta.
                        onEliminar = { oferta ->
                            ofertaViewModel.eliminarOferta(oferta)
                        }
                    )
                }
            }

            // PERFIL
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