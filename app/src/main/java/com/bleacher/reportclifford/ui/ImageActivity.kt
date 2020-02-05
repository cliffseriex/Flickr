package com.bleacher.reportclifford.ui

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.*
import com.bleacher.reportclifford.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class ImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen_image)


        title = intent.getStringExtra(IMAGE_TITLE_EXTRA)

        Picasso.with(this)
                .load(intent.getStringExtra(IMAGE_URL_EXTRA))
                .fit().centerInside()
                .into(imageView)
        supportActionBar?.show()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

    }



    companion object {
        val IMAGE_URL_EXTRA = "ImageUrl"
        val IMAGE_TITLE_EXTRA = "ImageTitle"
        val IMAGE_DESCRIPTION_EXTRA = "ImageDescription"

        private val UI_ANIMATION_DELAY = 300
    }
}
