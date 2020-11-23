package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.*
import com.destructo.sushi.databinding.FragmentUserMangaListBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.listener.ListEndListener
import com.destructo.sushi.ui.manga.listener.MangaIdListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserMangaAll : Fragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userMangaAdapter: UserMangaListAdapter
    private lateinit var userMangaRecycler: RecyclerView
    private lateinit var userMangaProgress: ProgressBar
    private lateinit var userMangaPaginationProgress: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userMangaViewModel.getUserMangaList(null)
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
        userMangaRecycler.itemAnimator = null
        userMangaProgress = binding.userMangaListProgressbar
        userMangaPaginationProgress = binding.userMangaListPaginationProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userMangaAdapter = UserMangaListAdapter(AddChapterListener { manga ->
            val chapters = manga?.myMangaListStatus?.numChaptersRead
            val mangaId = manga?.id
            if (chapters != null && mangaId != null){
                userMangaViewModel.addChapterManga(mangaId.toString(),chapters+1)
            }
        }, MangaIdListener {
            it?.let{navigateToMangaDetails(it)}
        })
        userMangaAdapter.setListEndListener(object : ListEndListener {
            override fun onEndReached(position: Int) {
                userMangaViewModel.getNextPage(null)
            }

        })
        userMangaAdapter.stateRestorationPolicy = ALLOW
        userMangaRecycler.adapter = userMangaAdapter

        userMangaViewModel.userMangaListAll.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{
                    userMangaProgress.visibility = View.VISIBLE
                }
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

        userMangaViewModel.userMangaStatus.observe(viewLifecycleOwner){updateManga->
            userMangaViewModel.getUserMangaList(null)
        }
    }

    override fun onResume() {
        super.onResume()
        userMangaViewModel.getUserMangaList(null)
    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            MyMangaListFragmentDirections.actionMyMangaListFragmentToMangaDetailsFragment(mangaMalId)
        )
    }
}