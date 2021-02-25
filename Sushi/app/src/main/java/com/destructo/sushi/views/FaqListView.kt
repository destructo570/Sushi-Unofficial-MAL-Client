package com.destructo.sushi.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.destructo.sushi.R

class FaqListView  @JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
)
    : LinearLayout(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.list_item_faq, this)
        this.orientation = VERTICAL
    }

    fun setValues(q: String, a: String){
        val question = findViewById<TextView>(R.id.question)
        question.text = q
        val answer = findViewById<TextView>(R.id.answer)
        answer.text = a

    }
}