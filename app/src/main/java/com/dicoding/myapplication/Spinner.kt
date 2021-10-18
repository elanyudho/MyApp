package com.dicoding.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dicoding.myapplication.databinding.ActivitySpinnerBinding

class Spinner : AppCompatActivity() {

    lateinit var binding:ActivitySpinnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = arrayOf("Option 1", "Option 2", "Option 3")

        binding.spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        binding.spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.result.text = options[position]
            }

            @SuppressLint("SetTextI18n")
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.result.text = "Select Option"
            }

        }
    }
}