package pe.cibertec.restaurante.ui.util

import java.util.Locale

fun String.aPrecioOrNull(): Double? {
    return this.replace(',', '.').toDoubleOrNull()
}

fun esPrecioParcialValido(input: String): Boolean {
    if (input.isEmpty()) return true
    val regex = Regex("^\\d*[.,]?\\d{0,2}$")
    return regex.matches(input)
}

fun formatearPrecio(precio: Double): String {
    return "S/ " + String.format(Locale.US, "%.2f", precio)
}
