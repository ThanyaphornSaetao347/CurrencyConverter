package com.example.currencyexchange

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import kotlin.time.times

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var spin1: Spinner
    private lateinit var spin2: Spinner

    private lateinit var ed1: EditText
    private lateinit var ed2: EditText

    var currencies = arrayOf<String?>("Thai Baht",
        "Indian Rupees",
        "US Dollar",
        "Japanese Yen",
        "Russian Ruble")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spin1 = findViewById(R.id.spin1)
        spin2 = findViewById(R.id.spin2)

        ed1 = findViewById(R.id.ed1)
        ed2 = findViewById(R.id.ed2)

        spin1.onItemSelectedListener = this
        spin2.onItemSelectedListener = this
        val ad1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            currencies)
        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            currencies)
        //set simple layout resource file for each item of spinner
        ad1.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        ad2.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        //set the arrayAdapter (ad) data on the spinner which binds data to spinner
        spin1.adapter = ad1
        spin2.adapter = ad2

        ed1.doOnTextChanged { _, _, _, _ ->
            if (ed1.isFocused){
                val amt = if (ed1.text.isEmpty()) 0.0 else ed1.text.toString().toDouble()
                val convertedCurrency = convertCurrency(amt,
                    spin1.selectedItem.toString(),
                    spin2.selectedItem.toString())
                ed2.setText(convertedCurrency.toString())
            }
        }

        ed2.doOnTextChanged { _, _, _, _ ->
            if (ed2.isFocused) {
                val amt = if (ed2.text.isEmpty()) 0.0 else ed2.text.toString().toDouble()
                val convertedCurrency = convertCurrency(
                    amt,
                    spin2.selectedItem.toString(),
                    spin1.selectedItem.toString()
                )
                ed1.setText(convertedCurrency.toString())
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id){
            R.id.spin1 -> {
                val amt = if (ed1.text.isEmpty()) 0.0 else ed1.text.toString().toDouble()
                val convertedCurrency = convertCurrency(
                    amt,
                    spin1.selectedItem.toString(),
                    spin2.selectedItem.toString())
                ed2.setText(convertedCurrency.toString())
            }
            R.id.spin2 -> {
                val amt = if (ed2.text.isEmpty()) 0.0 else ed2.text.toString().toDouble()
                val convertedCurrency = convertCurrency(
                    amt,
                    spin2.selectedItem.toString(),
                    spin1.selectedItem.toString())
                ed1.setText(convertedCurrency.toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun convertCurrency(amt: Double, firstCurrency: String, secondCurency: String): Double {
        val thaiBaht = convertOtherToThaiCurrency(amt, firstCurrency)
        return convertThaiToOtherCurrency(thaiBaht, secondCurency)
    }

    private fun convertThaiToOtherCurrency(thaiBaht: Double, secondCurency: String): Double {
        return thaiBaht * when (secondCurency){
            "Thai Baht" -> 1.0
            "Indian Rupees" -> 2.30
            "US Dollar" -> 0.028
            "Japanese Yen" -> 4.16
            "Russian Ruble" -> 2.54
            else -> 0.0
        }
    }

    private fun convertOtherToThaiCurrency(amt: Double, firstCurrency: String): Double {
        return amt * when (firstCurrency){
            "Thai Baht" -> 1.0
            "Indian Rupees" -> 0.43
            "US Dollar" -> 36.06
            "Japanese Yen" -> 0.24
            "Russian Ruble" -> 0.39
            else -> 0.0
        }
    }
}