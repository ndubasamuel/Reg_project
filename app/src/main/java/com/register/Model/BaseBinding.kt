package com.register.Model

import android.widget.TextView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.register.HomeScreen
import io.reactivex.Observable


class BaseBinding() {
    val firstName = "Welcome"
    @BindingAdapter("firstName")
    fun setText(view: TextView, string: String) {
        view.text = string
    }


    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: TextView): CharArray {
        val fText = view.text.toString()

        return fText.toCharArray()
    }



}