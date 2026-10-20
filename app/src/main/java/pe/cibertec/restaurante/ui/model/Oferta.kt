package pe.cibertec.restaurante.ui.model

import kotlin.math.roundToInt

data class Oferta(
    val id: Int,
    val producto: Producto,
    val precioOferta: Double,
    val activa: Boolean,
    val precioOriginal: Double = producto.precio
) {
    fun porcentajeDescuento(): Int {
        val base = if (producto.precio > 0) producto.precio else precioOriginal
        if (base <= 0 || precioOferta >= base) return 0
        return (((base - precioOferta) / base) * 100).roundToInt()
    }
}
