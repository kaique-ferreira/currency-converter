package com.aruana.currencyprice.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {

    if (imageUrl == null) {
        return
    }

    GlideToVectorYou
            .init()
            .with(view.context)
            .requestBuilder
            .load(imageUrl)
            .into(view)
}

