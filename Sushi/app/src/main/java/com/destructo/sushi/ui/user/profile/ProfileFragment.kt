package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.view.*

private lateinit var toolbar: Toolbar
private lateinit var binding: FragmentProfileBinding

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
    }



    private fun setupToolbar() {
        toolbar.title = getString(R.string.title_user_profile)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }
}