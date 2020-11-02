package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentUserMangaListBinding
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserMangaReading : Fragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userMangaAdapter: UserMangaListAdapter
    private lateinit var userMangaRecycler: RecyclerView
    private lateinit var userMangaProgress: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userMangaViewModel.getUserMangaList(UserMangaStatus.READING.value)
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

        userMangaRecycler = binding.userMangaRecycler
        userMangaRecycler.setHasFixedSize(true)
        userMangaProgress = binding.userMangaListProgressbar


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userMangaAdapter = UserMangaListAdapter(AddChapterListener { manga ->
            val chapters = manga?.myListStatus?.numChaptersRead
            val mangaId = manga?.id
            if (chapters != null && mangaId != null){
                userMangaViewModel.addChapterManga(mangaId.toString(),chapters+1)
            }
        })
        userMangaRecycler.adapter = userMangaAdapter

        userMangaViewModel.userMangaListReading.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{userMangaProgress.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userMangaProgress.visibility = View.GONE
                    resource.data?.let{
                        userMangaAdapter.submitList(it.data)
                    }
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)}
            }
        }
    }
}