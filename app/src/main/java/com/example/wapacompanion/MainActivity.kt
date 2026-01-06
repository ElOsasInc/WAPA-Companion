package com.example.wapacompanion

//Screens
import com.example.wapacompanion.ui.screens.LoginScreen
import com.example.wapacompanion.ui.screens.InicioScreen
import com.example.wapacompanion.ui.screens.RegisterScreen
import com.example.wapacompanion.ui.screens.AgregarClaseScreen

//Librerias
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            LoginScreen(
                                loginExitoso = {
                                    navController.navigate("inicio")
                                },
                                onNavegarRegistro = {
                                    navController.navigate("registro")
                                }
                            )
                        }

                        composable("inicio") {
                            InicioScreen(
                                agregarClase = {
                                    navController.navigate("agregarClase")
                                },
                                logoutExitoso = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("agregarClase") {
                            AgregarClaseScreen(
                                claseAgregada = {
                                    navController.popBackStack()
                                }
                            )
                        }


                        composable("registro") {
                            RegisterScreen(
                                onNavegarLogin = {
                                    navController.navigate("login")
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}
