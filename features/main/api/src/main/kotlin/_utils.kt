import android.graphics.Bitmap
import kotlinx.datetime.LocalDate

data class NewsItem(
    val id: String,
    val url: String,
    val title: String,
    val desc: String,
    val imageBitmap: Bitmap?,
    val isImageLoading: Boolean,
    val source: String,
    val geo: String?,
    val date: LocalDate
)

data class Ticker(
    val title: String,
    val price: Float,
    val currency: String,
    val rubles: Float,
    val percentageDelta: Float,
    val logoBitmap: Bitmap?
)