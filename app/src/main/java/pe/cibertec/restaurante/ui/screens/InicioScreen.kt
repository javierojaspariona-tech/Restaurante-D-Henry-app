package pe.cibertec.restaurante.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.cibertec.restaurante.R
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme

private val VerdeHenry = Color(0xFF214D32)
private val FondoHenry = Color(0xFFF8F8F8)
private val VerdeIconoHenry = Color(0xFFD0F7B0)
private val TextoSecundarioHenry = Color(0xFF858985)

/**
 * Pantalla principal de D'Henry.
 * Los callbacks permiten que MainScreen controle la navegación.
 */
@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    onProductosClick: () -> Unit,
    onOfertasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FondoHenry),
        contentPadding = PaddingValues(
            start = 21.dp,
            end = 21.dp,
            top = 25.dp,
            bottom = 26.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "D'Henry",
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeHenry
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Bienvenido, Daniel",
                    fontSize = 21.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF222222)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "¿Qué deseas gestionar hoy?",
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color = TextoSecundarioHenry
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeHenry),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 23.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Gestión del restaurante",
                        fontSize = 19.sp,
                        lineHeight = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Administra productos y ofertas de D'Henry desde un solo lugar.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }
            }
        }

        item {
            Text(
                text = "Accesos rápidos",
                fontSize = 19.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222222),
                modifier = Modifier.padding(top = 1.dp)
            )
        }

        item {
            AccesoRapidoCard(
                titulo = "Productos",
                descripcion = "Administra los productos del menú",
                icono = R.drawable.ic_products,
                onClick = onProductosClick
            )
        }

        item {
            AccesoRapidoCard(
                titulo = "Ofertas",
                descripcion = "Revisa y configura promociones",
                icono = R.drawable.ic_tag_price,
                onClick = onOfertasClick
            )
        }

        item {
            AccesoRapidoCard(
                titulo = "Mi perfil",
                descripcion = "Consulta los datos de tu cuenta",
                icono = R.drawable.ic_person,
                onClick = onPerfilClick
            )
        }
    }
}

@Composable
private fun AccesoRapidoCard(
    titulo: String,
    descripcion: String,
    icono: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VerdeIconoHenry),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icono),
                    contentDescription = titulo,
                    tint = VerdeHenry,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF252525)
                )
                Text(
                    text = descripcion,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextoSecundarioHenry
                )
            }
        }
    }
}
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "Inicio D'Henry"
)
@Composable
private fun InicioScreenPreview() {
    RestauranteAppTheme {
        InicioScreen(
            onProductosClick = {},
            onOfertasClick = {},
            onPerfilClick = {}
        )
    }
}