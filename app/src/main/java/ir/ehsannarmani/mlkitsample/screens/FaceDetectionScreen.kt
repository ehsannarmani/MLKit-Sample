package ir.ehsannarmani.mlkitsample.screens

import android.graphics.Paint
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import ir.ehsannarmani.mlkitsample.analyzers.FaceAnalyzer
import ir.ehsannarmani.mlkitsample.screens.components.Camera
import ir.ehsannarmani.mlkitsample.screens.components.scale

@Composable
fun FaceDetectionScreen() {
    val density = LocalDensity.current

    val detectedFaces = remember {
        mutableStateListOf<Face>()
    }
    val currentImageProxy = remember {
        mutableStateOf<ImageProxy?>(null)
    }

    Box(modifier = Modifier.fillMaxSize()){
        Camera(
            analyzer = FaceAnalyzer(onDetect = { proxy, faces ->
                currentImageProxy.value = proxy
                detectedFaces.clear()
                detectedFaces.addAll(faces)
            })
        )
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            }) {
            detectedFaces.forEach { face ->
                val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
                leftEye?.let {
                    val scaled = it.position.scale(
                        size.width.toInt(),
                        size.height.toInt(),
                        currentImageProxy.value!!.width,
                        currentImageProxy.value!!.height,
                        face.boundingBox.width()
                    )
                    drawCircle(
                        color = Color.Red,
                        center = Offset(scaled.x, scaled.y),
                        radius = 10f
                    )
                }
                rightEye?.let {
                    val scaled = it.position.scale(
                        size.width.toInt(),
                        size.height.toInt(),
                        currentImageProxy.value!!.width,
                        currentImageProxy.value!!.height,
                        -face.boundingBox.width()
                    )
                    drawCircle(
                        color = Color.Green,
                        center = Offset(scaled.x, scaled.y),
                        radius = 10f
                    )
                }
            }
            drawIntoCanvas {
                it.nativeCanvas.also { canvas ->
                    detectedFaces.forEach {
                        val boxRect = it.boundingBox.scale(
                            canvas.width,
                            canvas.height,
                            currentImageProxy.value!!.width,
                            currentImageProxy.value!!.height,
                            it.boundingBox.width()
                        )

                        canvas.drawRect(
                            boxRect,
                            Paint().apply {
                                color = Color.Red.toArgb()
                                style = Paint.Style.STROKE
                                strokeWidth = 5f
                            },
                        )
                    }
                }
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }) {
            val width: Float
            val height: Float
            with(density) {
                width = maxWidth.toPx()
                height = maxHeight.toPx()
            }
            detectedFaces.forEach {
                val boxRect = it.boundingBox.scale(
                    width.toInt(),
                    height.toInt(),
                    currentImageProxy.value!!.width,
                    currentImageProxy.value!!.height,
                    it.boundingBox.width()
                )
                if (it.smilingProbability != null && it.leftEyeOpenProbability != null && it.rightEyeOpenProbability != null) {
                    Text(
                        text = "Smile: ${"%.2f".format(it.smilingProbability!!*100)}%",
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    boxRect.right.toInt()-500,
                                    boxRect.top.toInt() - 80
                                )
                            }
                            .graphicsLayer { rotationY = 180f },
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = "Left Eye Open: ${"%.2f".format(it.leftEyeOpenProbability!!*100)}%",
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    boxRect.right.toInt()-500,
                                    boxRect.top.toInt() - 130
                                )
                            }
                            .graphicsLayer { rotationY = 180f },
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = "Right Eye Open: ${"%.2f".format(it.rightEyeOpenProbability!!*100)}%",
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    boxRect.right.toInt()-500,
                                    boxRect.top.toInt() - 170
                                )
                            }
                            .graphicsLayer { rotationY = 180f },
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
        }
    }
}