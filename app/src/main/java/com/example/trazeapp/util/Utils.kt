package com.example.trazeapp.util


import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.delay


suspend fun <T> retry(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelay
    // i repeat niya hantod sa times nga variable
    repeat(times - 1) {
        try {
            return block() // i return niya what ever object nga imoha ipasa
        } catch (e: Exception) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        // i increase niya ang delay via factor kay i times man
        // para ipadugay niya kadyot ang error
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt


}


fun Fragment.buildGetContentRequest(function: (Uri) -> Unit): ActivityResultLauncher<String> {
    return this.registerForActivityResult(ActivityResultContracts.GetContent()) {
        function(it)
    }
}

fun Fragment.buildTakePhotoRequest(function: (Boolean) -> Unit): ActivityResultLauncher<Uri?> {
    return this.registerForActivityResult(ActivityResultContracts.TakePicture()) {
        function(it)
    }
}

fun Fragment.startActivityForResult(function: (ActivityResult) -> Unit): ActivityResultLauncher<Intent?> {
    return this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        function(it)
    }
}


fun Fragment.multiplePermission(function: (MutableMap<String, Boolean>) -> Unit): ActivityResultLauncher<Array<String>> {
    return this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        function(it)
    }
}












