package com.example.wapacompanion

//Screens
import com.example.wapacompanion.ui.screens.LoginScreen
import com.example.wapacompanion.ui.screens.InicioScreen
import com.example.wapacompanion.ui.screens.RegisterScreen
import com.example.wapacompanion.ui.screens.AgregarClaseScreen
import com.example.wapacompanion.ui.screens.AsistenciaScreen
import com.example.wapacompanion.ui.theme.WAPACompanionTheme

//Librerias
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar permiso
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permiso
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }

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
                                },
                                verDetallesClase = {
                                    navController.navigate("asistencia")
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

                        composable("asistencia") {
                            AsistenciaScreen()
                        }
                    }

                }
            }
        }
    }
}
