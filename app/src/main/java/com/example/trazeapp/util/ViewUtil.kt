package com.example.trazeapp.util

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.Toast
import com.example.trazeapp.R
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress


fun Context.toast(message:String?) {
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Context.toast(messageResponse: Int) {
    Toast.makeText(this,messageResponse,Toast.LENGTH_LONG).show()
}
private fun Button.showLoading() {
    showProgress {
        buttonTextRes = R.string.loading
        progressColor = Color.WHITE
    }
}

