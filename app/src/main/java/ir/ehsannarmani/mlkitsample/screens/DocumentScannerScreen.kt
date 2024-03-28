package ir.ehsannarmani.mlkitsample.screens

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import ir.ehsannarmani.mlkitsample.MainActivity

@Composable
fun DocumentScannerScreen() {
    val activity = LocalContext.current as MainActivity
    val options = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .build()
    }
    val scanner = remember {
        GmsDocumentScanning.getClient(options)
    }
    val scannedDocuments = remember {
        mutableStateListOf<Uri>()
    }
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == RESULT_OK){
                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                result?.pages?.let { pages->
                    scannedDocuments.clear()
                    scannedDocuments.addAll(pages.map { it.imageUri })
                }
                result?.pdf?.let { pdf->

                }
            }
        }
    )
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(onClick = {
            scanner.getStartScanIntent(activity)
                .addOnSuccessListener {
                    scannerLauncher.launch(IntentSenderRequest.Builder(it).build())
                }
        }) {
            Text(text = "Scan")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(scannedDocuments){
                    AsyncImage(
                        modifier=Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                        model = it,
                        contentDescription = null
                    )
                }
            }
        }
    }
}