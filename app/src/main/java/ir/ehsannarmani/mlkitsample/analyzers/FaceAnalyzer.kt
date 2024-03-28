package ir.ehsannarmani.mlkitsample.analyzers

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalGetImage::class)
class FaceAnalyzer(
    private val onDetect: (
        ImageProxy,
        List<Face>
    ) -> Unit
) : ImageAnalysis.Analyzer {
    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(options)
    override fun analyze(imageProxy: ImageProxy) {

        imageProxy.image?.let { img ->
            detector
                .process(InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees))
                .addOnSuccessListener {
                    onDetect(imageProxy,it)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}

