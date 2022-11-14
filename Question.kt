package com.example.myapp

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)
{
    private var isAnswered = false

    public fun answered()
    {
        isAnswered = true
    }

    public fun getAnswered() : Boolean
    {
        return this.isAnswered
    }
}
