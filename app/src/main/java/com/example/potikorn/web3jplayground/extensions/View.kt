package com.example.potikorn.web3jplayground.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.getBitmapFromView(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val c = Canvas(bitmap)
    this.layout(this.left, this.top, this.right, this.bottom)
    this.draw(c)
    return bitmap
}

fun View.swapSelectedState() {
    when (this.isSelected) {
        true -> this.isSelected = false
        else -> this.isSelected = true
    }
}

fun View.show() = let {
    this.visibility = View.VISIBLE
}

fun View.invisible() = let {
    this.visibility = View.INVISIBLE
}

fun View.hide() = let {
    this.visibility = View.GONE
}