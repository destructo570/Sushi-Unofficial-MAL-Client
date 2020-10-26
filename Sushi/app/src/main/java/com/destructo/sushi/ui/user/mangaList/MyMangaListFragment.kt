package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMyAnimeListBinding
import com.destructo.sushi.databinding.FragmentMyMangaListBinding
import kotlinx.android.synthetic.main.activity_main.*

class MyMangaListFragment : Fragment() {

    private lateinit var binding: FragmentMyMangaListBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMangaListBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        toolbar = binding.toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }
}