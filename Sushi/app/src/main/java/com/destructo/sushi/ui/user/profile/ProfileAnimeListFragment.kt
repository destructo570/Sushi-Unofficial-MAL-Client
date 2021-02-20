package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.JikanUserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentProfileAnimeListBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val ARG_USERNAME = "username"

@AndroidEntryPoint
class ProfileAnimeListFragment : Fragment(), ListEndListener {

    private lateinit var binding: FragmentProfileAnimeListBinding
    private lateinit var animeListRecyclerView: RecyclerView
    private lateinit var animeListAdapter: JikanUserAnimeListAdapter
    private lateinit var progressBar: ProgressBar
    private val profileViewModel: ProfileViewModel by
    viewModels(ownerProducer = {requireParentFragment()})

    private var userName: String? = null

    companion object{
        fun newInstance(userName:String?): ProfileAnimeListFragment {
            val profileAnimeListFragment = ProfileAnimeListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, userName)
            profileAnimeListFragment.arguments = bundle
            return profileAnimeListFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName = arguments?.getString(ARG_USERNAME)
        userName?.let { profileViewModel.getUserAnimeList(it, "all") }
        profileViewModel.clearAnimeList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileAnimeListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        animeListRecyclerView = binding.profileAnimeListRecycler
        animeListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        animeListRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        progressBar = binding.animeListProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animeListAdapter = JikanUserAnimeListAdapter(MalIdListener {
            it?.let{ navigateToAnimeDetails(it) }
        })
        animeListAdapter.setListEndListener(this)
        animeListRecyclerView.adapter = animeListAdapter
        profileViewModel.userAnimeList.observe(viewLifecycleOwner){resource ->

            when(resource.status){
                Status.LOADING ->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: ${resource.message}")
                }

            }
        }

        profileViewModel.getAnimeList.observe(viewLifecycleOwner){
            animeListAdapter.submitList(it)
        }

    }



    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }

    override fun onEndReached(position: Int) {
        userName?.let { profileViewModel.getUserAnimeList(it, "all") }
    }
}