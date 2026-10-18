package pe.cibertec.restaurante.data.local

import android.content.ContentValues
import android.content.Context
import pe.cibertec.restaurante.ui.model.Producto

class ProductoDAO(
    context: Context
) {
    private val dbHelper = DatabaseHelper(context)

    fun insertar(producto: Producto): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("descripcion", producto.descripcion)
            put("estado", if (producto.disponible) "Activo" else "Inactivo")
            put("imagen_uri", producto.imagenUri?.toString())
        }
        return db.insert(
            "productos",
            null,
            values
        )
    }
}