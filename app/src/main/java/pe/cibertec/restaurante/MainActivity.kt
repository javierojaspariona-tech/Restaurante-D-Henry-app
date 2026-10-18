package pe.cibertec.restaurante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import pe.cibertec.restaurante.data.local.DatabaseHelper
import pe.cibertec.restaurante.ui.navigation.AppNavigation
import pe.cibertec.restaurante.ui.theme.Background
import pe.cibertec.restaurante.ui.theme.RestauranteAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        setContent {
            RestauranteAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    val navController = rememberNavController()

                    AppNavigation(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController
                    )
                }
            }
        }
    }
}