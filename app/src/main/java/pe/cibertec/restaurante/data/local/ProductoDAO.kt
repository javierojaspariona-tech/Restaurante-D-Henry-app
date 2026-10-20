package pe.cibertec.restaurante.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import pe.cibertec.restaurante.ui.model.Producto

class ProductoDAO(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertar(producto: Producto): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("descripcion", producto.descripcion)
            put(
                "estado",
                if (producto.disponible) "Activo" else "Inactivo"
            )
            put("imagen_uri", producto.imagenUri?.toString())
        }

        return db.insert("productos", null, values)
    }

    fun actualizar(producto: Producto): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("descripcion", producto.descripcion)
            put(
                "estado",
                if (producto.disponible) "Activo" else "Inactivo"
            )
            put("imagen_uri", producto.imagenUri?.toString())
        }

        return db.update(
            "productos",
            values,
            "id = ?",
            arrayOf(producto.id.toString())
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

        cursor.use {
            while (it.moveToNext()) {
                val estado = it.getString(
                    it.getColumnIndexOrThrow("estado")
                )

                val imagen = it.getString(
                    it.getColumnIndexOrThrow("imagen_uri")
                )

                val producto = Producto(
                    id = it.getInt(
                        it.getColumnIndexOrThrow("id")
                    ),
                    nombre = it.getString(
                        it.getColumnIndexOrThrow("nombre")
                    ),
                    categoria = it.getString(
                        it.getColumnIndexOrThrow("categoria")
                    ),
                    precio = it.getDouble(
                        it.getColumnIndexOrThrow("precio")
                    ),
                    descripcion = it.getString(
                        it.getColumnIndexOrThrow("descripcion")
                    ),
                    estado = estado,
                    disponible = estado == "Activo",
                    imagenUri = imagen?.let { uri ->
                        Uri.parse(uri)
                    }
                )

                productos.add(producto)
            }
        }

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