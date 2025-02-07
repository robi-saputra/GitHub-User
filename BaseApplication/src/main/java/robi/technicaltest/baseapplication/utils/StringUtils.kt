package robi.technicaltest.baseapplication.utils

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

fun String?.parseNames(): Pair<String, String> {
    return if (this != null) {
        val nameParts = this.split(" ")
        val firstName = nameParts.firstOrNull() ?: ""
        val lastName = nameParts.drop(1).joinToString(" ")
        firstName to lastName
    } else {
        "" to ""
    }
}

fun CharSequence.spannableNormal(boldText: String): CharSequence {
    val spannable = SpannableString(this)
    val start = this.indexOf(boldText)
    if (start >= 0) {
        spannable.setSpan(
            StyleSpan(Typeface.NORMAL),
            start,
            start + boldText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

fun CharSequence.spannableBold(boldText: String): CharSequence {
    val spannable = SpannableString(this)
    val start = this.indexOf(boldText)
    if (start >= 0) {
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            start + boldText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

fun CharSequence.spannableColor(text: String, color: Int): CharSequence {
    val spannable = SpannableString(this)
    val start = this.indexOf(text)
    if (start >= 0) {
        spannable.setSpan(
            ForegroundColorSpan(color),
            start,
            start + text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

@SuppressLint("DefaultLocale")
fun Int.formatNumber(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fm", this / 1_000_000.0)
        this >= 1_000 -> String.format("%.1fk", this / 1_000.0)
        else -> this.toString()
    }
}