package ir.ehsannarmani.mlkitsample.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.ehsannarmani.mlkitsample.Routes

@Composable
fun HomeScreen(navController: NavController) {
    val routes = Routes.entries.filter { it != Routes.Home }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {

        }
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    LazyColumn(modifier= Modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(routes){
            RouteItem(route = it, onClick = {
                navController.navigate(it.route)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteItem(route: Routes,onClick:()->Unit) {
    Card(modifier=Modifier.fillMaxWidth(), onClick = onClick) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.CenterStart){
            Text(text = route.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}