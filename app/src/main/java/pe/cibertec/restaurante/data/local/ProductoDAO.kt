package pe.cibertec.restaurante.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
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

    fun obtenerTodos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "productos",
            null,
            null,
            null,
            null,
            null,
            "id ASC"
        )
        while (cursor.moveToNext()) {
            val estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
            val imagen: String? = cursor.getString(cursor.getColumnIndexOrThrow("imagen_uri"))

            val producto = Producto(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                estado = estado,
                disponible = estado == "Activo",
                imagenUri = imagen?.let { Uri.parse(it) }
            )
            productos.add(producto)
        }
        cursor.close()
        return productos
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            "productos",
            "id = ?",
            arrayOf(id.toString())
        )
    }

    fun cerrar() {
        dbHelper.close()
    }
}