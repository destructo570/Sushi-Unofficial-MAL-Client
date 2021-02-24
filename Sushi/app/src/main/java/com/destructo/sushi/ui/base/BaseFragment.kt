package com.destructo.sushi.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.destructo.sushi.R

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getAnimNavOptions(): NavOptions{
        return NavOptions
            .Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
    }

}