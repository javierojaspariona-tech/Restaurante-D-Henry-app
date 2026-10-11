package pe.cibertec.restaurante.ui.model

import android.net.Uri
import java.util.Locale

val CATEGORIAS = listOf(
    "Platos principales",
    "Entradas",
    "Comidas rápidas",
    "Bebidas",
    "Postres"
)

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val descripcion: String,
    val estado: String = "Activo",
    val disponible: Boolean = (estado == "Activo"),
    val imagenRes: Int? = null,
    val imagenUri: Uri? = null
) {

    fun precioFormateado(): String {
        return "S/ " + String.format(Locale.US, "%.2f", precio)
    }

    fun estaActivo(): Boolean {
        return estado == "Activo" || disponible
    }
}
