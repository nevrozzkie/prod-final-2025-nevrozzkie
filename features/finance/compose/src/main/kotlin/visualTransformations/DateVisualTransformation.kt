package visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import formatWithoutDotsToDate


// not in common utils cuz "alpha"
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val formattedText = formatWithoutDotsToDate(inputText)

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = DateOffsetMapping()
        )
    }


}

class DateOffsetMapping(
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return when {
            offset <= 2 -> offset // day
            offset <= 4 -> offset + 1 // .month (1 dot)
            else -> offset + 2 // .month.year (2 dots)
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return when {
            offset <= 2 -> offset
            offset <= 5 -> offset - 1
            else -> offset - 2
        }
    }
}