package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
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
import com.destructo.sushi.enum.jikan.UserAnimeListStatus
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import com.destructo.sushi.util.makeLongToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val ARG_USERNAME = "username"
const val ARG_STATUS = "status"

@AndroidEntryPoint
class ProfileAnimeListFragment : Fragment(), ListEndListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentProfileAnimeListBinding
    private lateinit var animeListRecyclerView: RecyclerView
    private lateinit var animeListAdapter: JikanUserAnimeListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var statusSpinner: Spinner
    private val profileViewModel: ProfileViewModel by
    viewModels(ownerProducer = {requireParentFragment()})

    private var userName: String? = null
    private var currentStatus: String = UserAnimeListStatus.ALL.value

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
        if(savedInstanceState != null){
            savedInstanceState.getString(ARG_STATUS)?.let { currentStatus = it }
            savedInstanceState.getString(ARG_USERNAME)?.let { userName = it }
        }else{
            userName = arguments?.getString(ARG_USERNAME)
            profileViewModel.clearAnimeList()
            userName?.let { profileViewModel.getUserAnimeList(it, currentStatus) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_STATUS, currentStatus)
        outState.putString(ARG_USERNAME, userName)

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
        statusSpinner = binding.statusSpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.jikan_anime_status,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = adapter
            statusSpinner.onItemSelectedListener = this
        } }

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
                    progressBar.visibility = View.GONE
                    context?.makeLongToast(getString(R.string.error_user_list))
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
        userName?.let { profileViewModel.getUserAnimeList(it, currentStatus) }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (view!=null){
            when (parent?.getItemAtPosition(pos).toString()) {
                getString(R.string.all) -> {
                    onStatusChanged(UserAnimeListStatus.ALL.value)
                }
                getString(R.string.watching) -> {
                    onStatusChanged(UserAnimeListStatus.WATCHING.value)
                }
                getString(R.string.plan_to_watch) -> {
                    onStatusChanged(UserAnimeListStatus.PLAN_TO_WATCH.value)
                }
                getString(R.string.on_hold) -> {
                    onStatusChanged(UserAnimeListStatus.ON_HOLD.value)
                }
                getString(R.string.dropped) -> {
                    onStatusChanged(UserAnimeListStatus.DROPPED.value)
                }
                getString(R.string.completed) -> {
                    onStatusChanged(UserAnimeListStatus.COMPLETED.value)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun onStatusChanged(status: String){
        currentStatus = status
        profileViewModel.clearAnimeList()
        userName?.let { profileViewModel.getUserAnimeList(it, currentStatus) }
    }
}