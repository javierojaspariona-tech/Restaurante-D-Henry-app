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

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                val esNuevo = producto.id == 0
                val resultado = withContext(Dispatchers.IO) {
                    if (esNuevo) {
                        productoDAO.insertar(producto)
                    } else {
                        0L // Aquí irá la función de actualización (BD-3)
                    }
                }
                mensaje = if (resultado > 0L) {
                    "Producto agregado correctamente"
                } else {
                    "No se pudo agregar el producto"
                }
            } catch (e: Exception) {
                mensaje = "Error al guardar producto: ${e.message}"
            }
        }
    }
}