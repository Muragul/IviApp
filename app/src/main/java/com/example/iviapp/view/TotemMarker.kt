package com.example.iviapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.iviapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Shaqi_jrvej.jpg/1200px-Shaqi_jrvej.jpg"

class TotemMarker : ConstraintLayout {
    private lateinit var icon: ImageView
    private lateinit var dot: CardView
    private var imageUrl = ""

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.marker_custom, this)
        icon = findViewById(R.id.icon)
        dot = findViewById(R.id.dot)
        runBlocking {
            val src = withContext(Dispatchers.IO) {
                Glide.with(context.applicationContext)
                    .asDrawable()
                    .load(IMAGE_URL)
                    .circleCrop()
                    .submit()
                    .get()
            }
            icon.setImageDrawable(src)
        }
//        dot.visibility = View.GONE
    }

    companion object {
        fun newInstance(url: String, context: Context): TotemMarker {
            return TotemMarker(context).apply {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = params
//                imageUrl = url
            }
        }
    }

}