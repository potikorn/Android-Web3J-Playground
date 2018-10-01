//package twentyscoops.mvvm.kotlin.extensions
//
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DecodeFormat
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//
////fun ImageView.loadImageView(imageUrl: String) {
////    val requestOptions = RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)
////        .diskCacheStrategy(DiskCacheStrategy.ALL)
////        .error(android.R.color.darker_gray) // you can change another error view here
////        .placeholder(android.R.color.darker_gray) // you can change another place holder view here
////        .dontTransform()
////    Glide.with(context)
////        .applyDefaultRequestOptions(requestOptions)
////        .load(imageUrl)
////        .into(this)
////}