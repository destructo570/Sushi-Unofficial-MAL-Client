package com.destructo.sushi.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.destructo.sushi.R

class SettingsListView
    @JvmOverloads
    constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
    : ConstraintLayout(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.settings_list_item_view, this)
        val title = findViewById<TextView>(R.id.setting_list_title)
        val description = findViewById<TextView>(R.id.setting_list_description)
        val icon = findViewById<ImageView>(R.id.setting_list_icon)

        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.SettingsListView, defStyleAttr ,0)

        title.text = attributeArray.getText(R.styleable.SettingsListView_setting_title)
        description.text = attributeArray.getText(R.styleable.SettingsListView_setting_description)
        icon.setImageDrawable(attributeArray.getDrawable(R.styleable.SettingsListView_setting_icon))
        attributeArray.recycle()
    }
}