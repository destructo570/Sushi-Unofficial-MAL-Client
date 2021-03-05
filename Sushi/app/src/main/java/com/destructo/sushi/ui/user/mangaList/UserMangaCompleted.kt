package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.destructo.sushi.listener.AddChapterListenerUA
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.database.UserMangaEntity
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMangaCompleted : BaseFragment() {

    private lateinit var binding: FragmentUserMangaListBinding
    private val userMangaViewModel: UserMangaViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userMangaAdapter: UserMangaListAdapter
    private lateinit var userMangaRecycler: RecyclerView

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userMangaAdapter = UserMangaListAdapter(AddChapterListenerUA { manga ->
            val chapters = manga?.myMangaListStatus?.numChaptersRead
            val mangaId = manga?.malId
            if (chapters != null && mangaId != null) {
                userMangaViewModel.addChapterManga(mangaId.toString(), chapters + 1, null)
            }
        }, MalIdListener {
            it?.let { navigateToMangaDetails(it) }
        }, false)
        userMangaAdapter.setListEndListener(object : ListEndListener {
            override fun onEndReached(position: Int) {
                userMangaViewModel.getNextPage()
            }

        })
        userMangaRecycler.adapter = userMangaAdapter

        userMangaViewModel.userMangaList.observe(viewLifecycleOwner) {
            val droppedList = mutableListOf<UserMangaEntity>()
            for (manga in it) {
                if (manga.myMangaListStatus?.status == UserMangaStatus.COMPLETED.value) {
                    droppedList.add(manga)
                }
            }
            userMangaAdapter.submitList(droppedList)
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