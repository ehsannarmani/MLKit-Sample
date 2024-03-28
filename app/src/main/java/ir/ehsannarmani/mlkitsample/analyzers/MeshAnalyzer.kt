package ir.ehsannarmani.mlkitsample.analyzers

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions

@OptIn(ExperimentalGetImage::class)
class MeshAnalyzer(private val onDetect:(ImageProxy,List<FaceMesh>)->Unit):ImageAnalysis.Analyzer {
    val options = FaceMeshDetectorOptions.Builder()
        .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
        .build()

    val detector = FaceMeshDetection.getClient(options)
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