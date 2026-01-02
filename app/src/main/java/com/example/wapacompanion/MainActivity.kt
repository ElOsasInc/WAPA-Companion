package com.example.wapacompanion

//Screens
import com.example.wapacompanion.ui.screens.LoginScreen
import com.example.wapacompanion.ui.screens.InicioScreen

//Librerias
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wapacompanion.ui.theme.WAPACompanionTheme
import com.example.wapacompanion.viewmodel.ProfesorViewModel

class MainActivity : ComponentActivity() {
    private val profesorViewModel: ProfesorViewModel by viewModels()

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
                                profesorViewModel,
                                onNavegarDetalle = {
                                    //navController.navigate("inicio")
                                }
                            )
                        }

                        composable("inicio") {
                            InicioScreen()
                        }
                    }
                }
            }
        }
    }
}