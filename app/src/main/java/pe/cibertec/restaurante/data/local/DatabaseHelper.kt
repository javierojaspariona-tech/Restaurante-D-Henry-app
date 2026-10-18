package pe.cibertec.restaurante.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL,
                clave TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                categoria TEXT NOT NULL,
                precio REAL NOT NULL,
                descripcion TEXT NOT NULL,
                estado TEXT NOT NULL DEFAULT 'Activo',
                imagen_uri TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE ofertas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                producto_id INTEGER NOT NULL,
                precio_oferta REAL NOT NULL,
                activa INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (producto_id) REFERENCES productos(id)
            )
        """.trimIndent())

        // Datos iniciales (mismos productos de ProductoDemo.kt)
        db.execSQL("""
            INSERT INTO productos (nombre, categoria, precio, descripcion, estado) VALUES
            ('Lomo Saltado', 'Platos principales', 22.00, 'Lomo de res salteado con cebolla, tomate y papas fritas, acompañado de arroz.', 'Activo'),
            ('Ají de Gallina', 'Platos principales', 18.00, 'Gallina deshilachada en crema de ají amarillo, acompañada de arroz.', 'Activo'),
            ('Hamburguesa', 'Comidas rápidas', 15.00, 'Hamburguesa de carne con queso, lechuga y tomate, acompañada de papas.', 'Activo'),
            ('Ensalada César', 'Entradas', 12.00, 'Lechuga fresca con pollo, crutones y queso parmesano.', 'Inactivo'),
            ('Inca Kola 500ml', 'Bebidas', 5.00, 'Gaseosa Inca Kola de 500 ml.', 'Activo')
        """.trimIndent())
    }

    // ROLLBACK
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS ofertas")
        db.execSQL("DROP TABLE IF EXISTS productos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dhenrys.db"
        private const val DATABASE_VERSION = 2
    }
}