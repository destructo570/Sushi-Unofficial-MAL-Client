package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentUserMangaListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMangaDropped : Fragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var allMangaAdapter: UserMangaListAdapter
    private lateinit var allMangaRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userMangaViewModel.getUserMangaListDropped()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserMangaListBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        allMangaRecycler = binding.userMangaRecycler
        allMangaRecycler.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allMangaAdapter = UserMangaListAdapter()
        userMangaViewModel.userMangaListDropped.observe(viewLifecycleOwner) { userManga ->
            allMangaAdapter.submitList(userManga.data)
            allMangaRecycler.adapter = allMangaAdapter
        }
    }
}