package pe.cibertec.restaurante.ui.model

data class Oferta(
    val id: Int,
    val producto: Producto,
    val precioOferta: Double,
    val activa: Boolean,
    val precioOriginal: Double = producto.precio
)
