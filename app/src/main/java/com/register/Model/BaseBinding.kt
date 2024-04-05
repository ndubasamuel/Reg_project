package com.register.Model

import android.widget.TextView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter


class BaseBinding() {
    @BindingAdapter("android:text")
    fun setText(view: TextView, int: Int) {
        view.text = int.toString()
    }


    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: TextView): CharArray {
        val fText = view.text.toString()

        return fText.toCharArray()
    }



}