package ir.ehsannarmani.mlkitsample.screens.components

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.SparseArray
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import ir.ehsannarmani.mlkitsample.analyzers.FaceAnalyzer


@Composable
fun Camera(
    analyzer: Analyzer
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFeature = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val previewView = remember {
        PreviewView(context)
    }
    val preview = remember {
        Preview.Builder().build()
    }
    val imageAnalysis = remember {
        ImageAnalysis.Builder().build()
    }
    val cameraSelector = remember {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
    }
    DisposableEffect(Unit) {
        val feature = cameraProviderFeature.get()
        feature.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
        onDispose {
            feature.unbindAll()
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                preview.setSurfaceProvider(previewView.surfaceProvider)
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    analyzer
                )
                previewView
            }
        )
    }

}

fun Rect.scale(
    parentWidth: Int,
    parentHeight: Int,
    imageWidth: Int,
    imageHeight: Int,
    resizeNumber: Int,
): RectF {
    val scaleFactorX = (parentWidth / (imageHeight.toFloat()))
    val scaleFactorY = (parentHeight / imageWidth.toFloat())
    return RectF(
        (left * scaleFactorX) - (resizeNumber),
        top * scaleFactorY,
        (right * scaleFactorX) + resizeNumber,
        bottom * scaleFactorY
    )
}

fun PointF.scale(
    parentWidth: Int,
    parentHeight: Int,
    imageWidth: Int,
    imageHeight: Int,
    resizeNumber: Int,
): PointF {
    val scaleFactorX = (parentWidth / (imageHeight.toFloat()))
    val scaleFactorY = (parentHeight / imageWidth.toFloat())
    return PointF(
        (x * scaleFactorX) + resizeNumber,
        y * scaleFactorY,
    )
}
