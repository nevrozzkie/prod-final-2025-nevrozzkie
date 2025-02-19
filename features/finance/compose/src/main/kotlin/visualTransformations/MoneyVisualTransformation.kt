package visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import formatLikeAmount
import kotlin.math.max

class MoneyOnlyLongVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = if(text.text.isEmpty()) "" else text.text.formatLikeAmount()
        val formattedTextWithCurrency = if(text.text.isEmpty()) "" else "$formattedText ₽"
        return TransformedText(
            text = AnnotatedString(formattedTextWithCurrency),
            offsetMapping = MoneyOffsetMapping(text.text, formattedTextWithCurrency)
        )
    }
}

// experimental way -> я разобрался урауарура
class MoneyOffsetMapping(
    private val text: String,
    private val formattedText: String,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return max(0,formattedText.length - 2)
    }

    override fun transformedToOriginal(offset: Int): Int {
        return max(0,text.length)
    }
}