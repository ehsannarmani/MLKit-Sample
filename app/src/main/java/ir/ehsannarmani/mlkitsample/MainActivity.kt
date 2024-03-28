package ir.ehsannarmani.mlkitsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.ehsannarmani.mlkitsample.screens.DocumentScannerScreen
import ir.ehsannarmani.mlkitsample.screens.FaceDetectionScreen
import ir.ehsannarmani.mlkitsample.screens.FaceDrawerScreen
import ir.ehsannarmani.mlkitsample.screens.FaceMeshScreen
import ir.ehsannarmani.mlkitsample.screens.HomeScreen
import ir.ehsannarmani.mlkitsample.ui.theme.MLKitSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home.route,
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                        },
                        exitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                        },
                        popEnterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                        },
                        popExitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                        }
                    ){
                        composable(Routes.Home.route){
                            HomeScreen(navController = navController)
                        }
                        composable(Routes.FaceDetection.route){
                            FaceDetectionScreen()
                        }
                        composable(Routes.FaceDrawer.route){
                            FaceDrawerScreen()
                        }
                        composable(Routes.FaceMesh.route){
                            FaceMeshScreen()
                        }
                        composable(Routes.DocumentScanner.route){
                            DocumentScannerScreen()
                        }
                    }
                }
            }
        }
    }
}

