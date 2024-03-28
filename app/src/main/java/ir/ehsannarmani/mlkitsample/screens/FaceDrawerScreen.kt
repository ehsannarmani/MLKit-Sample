package ir.ehsannarmani.mlkitsample.screens

import android.graphics.PointF
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.face.Face
import ir.ehsannarmani.mlkitsample.analyzers.FaceAnalyzer
import ir.ehsannarmani.mlkitsample.screens.components.Camera
import ir.ehsannarmani.mlkitsample.screens.components.scale

@Composable
fun FaceDrawerScreen() {
    val detectedFaces = remember {
        mutableStateListOf<Face>()
    }
    val currentImageProxy = remember {
        mutableStateOf<ImageProxy?>(null)
    }
    Box(modifier=Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(1.dp).alpha(0f)){
            Camera(analyzer = FaceAnalyzer(
                onDetect = {image,faces->
                    currentImageProxy.value = image
                    detectedFaces.clear()
                    detectedFaces.addAll(faces)
                }
            ))
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }) {
            detectedFaces.map { it.allContours }.forEach {
                it.forEach {
                    it.points.forEach {
                        val scaled = it.scale()
                        drawCircle(
                            color = Color.Red,
                            center = Offset(scaled.x,scaled.y),
                            radius = 5f
                        )
                    }
                }
            }
        }
    }
}

fun PointF.scale():PointF{
    return PointF(
        x*2.5f,
        y*2.5f
    )
}