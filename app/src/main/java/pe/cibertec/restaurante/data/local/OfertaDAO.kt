package pe.cibertec.restaurante.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import pe.cibertec.restaurante.ui.model.Oferta
import pe.cibertec.restaurante.ui.model.Producto

class OfertaDAO(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertar(oferta: Oferta): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("producto_id", oferta.producto.id)
            put("precio_oferta", oferta.precioOferta)
            put("activa", if (oferta.activa) 1 else 0)
        }

        return db.insert("ofertas", null, values)
    }

    fun actualizar(oferta: Oferta): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("precio_oferta", oferta.precioOferta)
            put("activa", if (oferta.activa) 1 else 0)
        }

        return db.update(
            "ofertas",
            values,
            "id = ?",
            arrayOf(oferta.id.toString())
        )
    }

    fun obtenerTodas(): List<Oferta> {
        val ofertas = mutableListOf<Oferta>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                o.id AS oferta_id,
                o.precio_oferta,
                o.activa,
                p.id AS producto_id,
                p.nombre,
                p.categoria,
                p.precio,
                p.descripcion,
                p.estado,
                p.imagen_uri
            FROM ofertas o
            INNER JOIN productos p ON p.id = o.producto_id
            ORDER BY o.id ASC
            """.trimIndent(),
            null
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
                        it.getColumnIndexOrThrow("producto_id")
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

                val oferta = Oferta(
                    id = it.getInt(
                        it.getColumnIndexOrThrow("oferta_id")
                    ),
                    producto = producto,
                    precioOferta = it.getDouble(
                        it.getColumnIndexOrThrow("precio_oferta")
                    ),
                    activa = it.getInt(
                        it.getColumnIndexOrThrow("activa")
                    ) == 1
                )

                ofertas.add(oferta)
            }
        }

        return ofertas
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase

        return db.delete(
            "ofertas",
            "id = ?",
            arrayOf(id.toString())
        )
    }

    fun cerrar() {
        dbHelper.close()
    }
}