package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.UserMangaListAdapter
import com.destructo.sushi.databinding.FragmentUserMangaListBinding
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.listener.AddChapterListener
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserMangaDropped : Fragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userMangaAdapter: UserMangaListAdapter
    private lateinit var userMangaRecycler: RecyclerView
    private lateinit var userMangaProgress: ProgressBar
    private lateinit var userMangaPaginationProgress: ProgressBar
    private var calledOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userMangaViewModel.getUserMangaList(UserMangaStatus.DROPPED.value)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(!calledOnce) {
            calledOnce = true
            binding = FragmentUserMangaListBinding
                .inflate(inflater, container, false).apply {
                    lifecycleOwner = viewLifecycleOwner
                }

            userMangaRecycler = binding.userMangaRecycler
            userMangaRecycler.setHasFixedSize(true)
            userMangaRecycler.itemAnimator = null
            userMangaProgress = binding.userMangaListProgressbar
            userMangaPaginationProgress = binding.userMangaListPaginationProgressbar
            userMangaAdapter = UserMangaListAdapter(AddChapterListener { manga ->
                val chapters = manga?.myMangaListStatus?.numChaptersRead
                val mangaId = manga?.id
                if (chapters != null && mangaId != null){
                    userMangaViewModel.addChapterManga(mangaId.toString(),chapters+1, null)
                }
            }, MalIdListener {
                it?.let{navigateToMangaDetails(it)}
            }, false)
            userMangaAdapter.setListEndListener(object : ListEndListener {
                override fun onEndReached(position: Int) {
                    userMangaViewModel.getNextPage(UserMangaStatus.DROPPED.value)
                }

            })
            userMangaAdapter.stateRestorationPolicy = ALLOW
            userMangaRecycler.adapter = userMangaAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        userMangaViewModel.userMangaListDropped.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{
                    userMangaProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userMangaProgress.visibility = View.GONE
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)}
            }
        }

        userMangaViewModel.userMangaStatus.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    userMangaProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userMangaProgress.visibility = View.GONE
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userMangaViewModel.getUserMangaByStatus(UserMangaStatus.DROPPED.value)
            .observe(viewLifecycleOwner){
                userMangaAdapter.submitList(it)
            }

        userMangaViewModel.userMangaListDroppedNext.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    userMangaPaginationProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userMangaPaginationProgress.visibility = View.GONE
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }
    }



    private fun navigateToMangaDetails(mangaIdArg: Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment, bundleOf(Pair(MANGA_ID_ARG, mangaIdArg))
        )
    }

}