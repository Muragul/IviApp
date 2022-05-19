package com.example.iviapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.iviapp.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Shaqi_jrvej.jpg/1200px-Shaqi_jrvej.jpg"
const val IMAGE_URL_1 =
    "https://www.eea.europa.eu/themes/biodiversity/state-of-nature-in-the-eu/state-of-nature-2020-subtopic/image_print"
const val IMAGE_URL_2 =
    "https://media.cntraveller.com/photos/611bf0b8f6bd8f17556db5e4/1:1/w_2000,h_2000,c_limit/gettyimages-1146431497.jpg"

val images = arrayListOf(
    IMAGE_URL, IMAGE_URL_1, IMAGE_URL_2,
    IMAGE_URL, IMAGE_URL_1, IMAGE_URL_2,
    IMAGE_URL, IMAGE_URL_1, IMAGE_URL_2,
    IMAGE_URL, IMAGE_URL_1, IMAGE_URL_2
)

data class TotemItem(
    val totemTitle: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    override fun getTitle(): String = totemTitle
    override fun getSnippet(): String = totemTitle
}

class TotemMarker : ConstraintLayout {
    private lateinit var icon: ImageView
    private lateinit var dot: CardView
    private var imageUrl = ""

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, url: String) : super(context) {
        this.imageUrl = url
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
                    .load(imageUrl)
                    .circleCrop()
                    .submit()
                    .get()
            }
            icon.setImageDrawable(src)
        }
    }

}