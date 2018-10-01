package com.example.potikorn.web3jplayground.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: CharSequence?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(resourceId: Int) {
    showToast(getString(resourceId))
}