package com.example.poiskbiletov

import android.text.InputFilter
import android.text.Spanned


class CyrillicInputFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (source == null) return null
        val regex = Regex("[а-яА-ЯёЁ ]*")  // Кириллица и пробел
        return if (source.matches(regex)) {
            null
        } else {
            ""
        }
    }
}