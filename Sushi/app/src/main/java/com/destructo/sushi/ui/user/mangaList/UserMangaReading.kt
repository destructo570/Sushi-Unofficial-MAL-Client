package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.UserMangaListAdapter
import com.destructo.sushi.databinding.FragmentUserMangaListBinding
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.listener.AddChapterListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserMangaReading : BaseFragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userMangaAdapter: UserMangaListAdapter
    private lateinit var userMangaRecycler: RecyclerView
    private lateinit var userMangaProgress: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserMangaListBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        userMangaRecycler = binding.userMangaRecycler
        userMangaRecycler.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
        userMangaRecycler.setHasFixedSize(true)
        userMangaProgress = binding.userMangaListProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userMangaAdapter = UserMangaListAdapter(AddChapterListener { manga ->
            val chapters = manga?.myMangaListStatus?.numChaptersRead
            val totalChapters = manga?.numChapters
            val mangaId = manga?.malId

            mangaId?.let { userMangaViewModel.clearMangaDetail(it) }
            if (chapters != null && mangaId != null && totalChapters != null && chapters.plus(1) == totalChapters) {
                userMangaViewModel.addChapterManga(
                    mangaId.toString(),
                    chapters + 1,
                    UserMangaStatus.COMPLETED.value
                )
            } else if (chapters != null && mangaId != null) {
                userMangaViewModel.addChapterManga(mangaId.toString(), chapters + 1, null)
            }
        }, MalIdListener { it?.let { navigateToMangaDetails(it) } }, true)

        userMangaRecycler.adapter = userMangaAdapter

        userMangaViewModel.userMangaList.observe(viewLifecycleOwner) {
                userMangaViewModel.getMangaListByStatus(UserMangaStatus.READING.value)
        }

        userMangaViewModel.userMangaReading.observe(viewLifecycleOwner) {
            userMangaAdapter.submitList(it.data)
        }


        userMangaViewModel.userMangaStatus.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    userMangaProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    userMangaProgress.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }


    }

    private fun navigateToMangaDetails(mangaIdArg: Int) {
        this.findNavController().navigate(
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaIdArg)),
            getAnimNavOptions()
        )
    }

}