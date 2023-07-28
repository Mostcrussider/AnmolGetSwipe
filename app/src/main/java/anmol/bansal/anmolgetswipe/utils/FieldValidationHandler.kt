package anmol.bansal.anmolgetswipe.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.EditText

class FieldValidationHandler(
    private val productNameEditText: EditText,
    private val sellingPriceEditText: EditText,
    private val taxRateEditText: EditText,
    private val productTypeList: AutoCompleteTextView,
    private val addButton: View
) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            updateAddButtonState()
        }
    }

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            updateAddButtonState()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            updateAddButtonState()
        }
    }

    init {
        productNameEditText.addTextChangedListener(textWatcher)
        sellingPriceEditText.addTextChangedListener(textWatcher)
        taxRateEditText.addTextChangedListener(textWatcher)
        productTypeList.onItemSelectedListener = spinnerItemSelectedListener
        updateAddButtonState()
    }

    private fun updateAddButtonState() {
        val productName = productNameEditText.text.toString()
        val sellingPrice = sellingPriceEditText.text.toString()
        val taxRate = taxRateEditText.text.toString()
        val productType = productTypeList.text.toString()

        addButton.isEnabled =
            productName.isNotBlank() && sellingPrice.isNotBlank() && taxRate.isNotBlank() && productType.isNotBlank()
    }
}