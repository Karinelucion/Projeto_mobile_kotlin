package br.edu.utpr.controlefluxocaixa

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private var isEditing = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isEditing) return

        isEditing = true

        try {
            val brazilLocale = Locale("pt", "BR")

            val originalString = s.toString()
            val cleanString = originalString.replace(Regex("[^\\d]"), "")

            val parsed = cleanString.toDoubleOrNull() ?: 0.0
            val formatted = NumberFormat.getCurrencyInstance(brazilLocale).format(parsed / 100)

            editText.setText(formatted)
            editText.setSelection(formatted.length)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        isEditing = false
    }
}
