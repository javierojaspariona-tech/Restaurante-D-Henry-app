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
                val resultado = withContext(Dispatchers.IO) {
                    if (esNuevo) {
                        productoDAO.insertar(producto)
                    } else {
                        0L // Aquí irá la función de actualización (BD-3c)
                    }
                }
                mensaje = if (resultado > 0L) {
                    "Producto agregado correctamente"
                } else {
                    "No se pudo agregar el producto"
                }
                cargarProductos()
            } catch (e: Exception) {
                mensaje = "Error al guardar producto: ${e.message}"
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