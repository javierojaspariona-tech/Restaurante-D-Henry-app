package pe.cibertec.restaurante.ui.oferta

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.cibertec.restaurante.data.local.OfertaDAO
import pe.cibertec.restaurante.ui.model.Oferta

class OfertaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val ofertaDAO = OfertaDAO(application)

    var ofertas by mutableStateOf<List<Oferta>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    fun guardarOferta(oferta: Oferta) {
        viewModelScope.launch {
            try {
                val esNueva = oferta.id == 0

                val guardada = withContext(Dispatchers.IO) {
                    if (esNueva) {
                        ofertaDAO.insertar(oferta) > 0L
                    } else {
                        ofertaDAO.actualizar(oferta) > 0
                    }
                }

                mensaje = when {
                    guardada && esNueva ->
                        "Oferta agregada correctamente"

                    guardada ->
                        "Oferta actualizada correctamente"

                    esNueva ->
                        "No se pudo agregar la oferta"

                    else ->
                        "No se pudo actualizar la oferta"
                }

                if (guardada) {
                    cargarOfertas()
                }
            } catch (e: Exception) {
                mensaje = "Error al guardar la oferta: ${e.message}"
            }
        }
    }

    fun cargarOfertas() {
        viewModelScope.launch {
            cargando = true

            try {
                ofertas = withContext(Dispatchers.IO) {
                    ofertaDAO.obtenerTodas()
                }
            } catch (e: Exception) {
                mensaje = "Error al cargar ofertas: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun eliminarOferta(oferta: Oferta) {
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    ofertaDAO.eliminar(oferta.id)
                }

                mensaje = if (resultado > 0) {
                    "Oferta eliminada correctamente"
                } else {
                    "No se pudo eliminar la oferta"
                }

                cargarOfertas()
            } catch (e: Exception) {
                mensaje = "Error al eliminar oferta: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        ofertaDAO.cerrar()
        super.onCleared()
    }
}