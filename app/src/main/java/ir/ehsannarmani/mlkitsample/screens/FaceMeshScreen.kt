package ir.ehsannarmani.mlkitsample.screens

import android.graphics.Paint
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.facemesh.FaceMesh
import ir.ehsannarmani.mlkitsample.analyzers.MeshAnalyzer
import ir.ehsannarmani.mlkitsample.screens.components.Camera

@Composable
fun FaceMeshScreen() {
    val detectedFaces = remember {
        mutableStateListOf<FaceMesh>()
    }
    val currentImageProxy = remember {
        mutableStateOf<ImageProxy?>(null)
    }
    Box(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier.size(1.dp).alpha(0f)){
            Camera(analyzer = MeshAnalyzer(onDetect = { image,faces->
                currentImageProxy.value = image
                detectedFaces.clear()
                detectedFaces.addAll(faces)
            }))
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }) {
            detectedFaces.forEach {
                it.allTriangles.forEachIndexed { triangleIndex, triangle ->
                    val connectedPoints = triangle.allPoints
                    connectedPoints.forEachIndexed { index, faceMeshPoint ->
                        val scaled = faceMeshPoint.position.scale()
                        drawCircle(
                            color = Color.White,
                            center = Offset(scaled.x,scaled.y),
                            radius = 4f
                        )
                        val nextPoint = connectedPoints
                            .getOrNull(index+1)
                            ?.position
                            ?.scale()
                        if (nextPoint != null){
                            drawLine(
                                color = Color.Red,
                                start = Offset(
                                    scaled.x,
                                    scaled.y
                                ),
                                end = Offset(
                                    nextPoint.x,
                                    nextPoint.y
                                ),
                                strokeWidth = 3f
                            )
                        }
                    }


                }
            }
        }
    }
}

fun PointF3D.scale():PointF3D{
    return PointF3D.from(x*2.8f,y*2.8f,z*2.8f)
}