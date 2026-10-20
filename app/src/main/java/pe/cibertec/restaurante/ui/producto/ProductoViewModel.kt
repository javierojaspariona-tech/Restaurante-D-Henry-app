package pe.cibertec.restaurante.ui.producto

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.cibertec.restaurante.data.local.ProductoDAO
import pe.cibertec.restaurante.ui.model.Producto

class ProductoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val productoDAO = ProductoDAO(application)

    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                val esNuevo = producto.id == 0

                val guardado = withContext(Dispatchers.IO) {
                    if (esNuevo) {
                        productoDAO.insertar(producto) > 0L
                    } else {
                        productoDAO.actualizar(producto) > 0
                    }
                }

                mensaje = when {
                    guardado && esNuevo ->
                        "Producto agregado correctamente"

                    guardado ->
                        "Producto actualizado correctamente"

                    esNuevo ->
                        "No se pudo agregar el producto"

                    else ->
                        "No se pudo actualizar el producto"
                }

                if (guardado) {
                    cargarProductos()
                }
            } catch (e: Exception) {
                mensaje = "Error al guardar el producto: ${e.message}"
            }
        }
    }

    fun cargarProductos() {
        viewModelScope.launch {
            cargando = true

            try {
                productos = withContext(Dispatchers.IO) {
                    productoDAO.obtenerTodos()
                }
            } catch (e: Exception) {
                mensaje = "Error al cargar productos: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    productoDAO.eliminar(producto.id)
                }

                mensaje = if (resultado > 0) {
                    "Producto eliminado correctamente"
                } else {
                    "No se pudo eliminar el producto"
                }

                cargarProductos()
            } catch (e: Exception) {
                mensaje = "Error al eliminar producto: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        productoDAO.cerrar()
        super.onCleared()
    }
}