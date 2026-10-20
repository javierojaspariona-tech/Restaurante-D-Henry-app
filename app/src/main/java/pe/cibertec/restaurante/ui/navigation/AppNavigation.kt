package pe.cibertec.restaurante.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.cibertec.restaurante.ui.screens.LoginScreen
import pe.cibertec.restaurante.ui.screens.MainScreen
import pe.cibertec.restaurante.ui.screens.RegisterScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.cibertec.restaurante.ui.oferta.OfertaViewModel
import pe.cibertec.restaurante.ui.producto.ProductoViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Login.route,
        modifier = modifier
    ) {

        composable(
            route = AppDestinations.Login.route
        ) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(
                        AppDestinations.Inicio.route
                    ) {
                        popUpTo(
                            AppDestinations.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(
                        AppDestinations.Register.route
                    )
                }
            )
        }

        composable(
            route = AppDestinations.Register.route
        ) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(
                        AppDestinations.Inicio.route
                    ) {
                        popUpTo(
                            AppDestinations.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestinations.Inicio.route
        ) { entry ->
            val productoViewModel: ProductoViewModel =
                viewModel(viewModelStoreOwner = entry)

            val ofertaViewModel: OfertaViewModel =
                viewModel(viewModelStoreOwner = entry)

            MainScreen(
                productoViewModel = productoViewModel,
                ofertaViewModel = ofertaViewModel,
                onLogoutClick = {
                    navController.navigate(
                        AppDestinations.Login.route
                    ) {
                        popUpTo(
                            AppDestinations.Inicio.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}