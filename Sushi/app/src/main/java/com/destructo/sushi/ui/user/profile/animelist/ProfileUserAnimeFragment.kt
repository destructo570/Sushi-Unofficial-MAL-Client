package com.destructo.sushi.ui.user.profile.animelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.STATUS_ARG
import com.destructo.sushi.USERNAME_ARG
import com.destructo.sushi.adapter.JikanUserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentProfileUserAnimeBinding
import com.destructo.sushi.enum.jikan.UserAnimeListStatus
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import com.destructo.sushi.util.makeLongToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileUserAnimeFragment : BaseFragment(), ListEndListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentProfileUserAnimeBinding
    private lateinit var animeListRecyclerView: RecyclerView
    private lateinit var animeListAdapter: JikanUserAnimeListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var statusSpinner: Spinner
    private lateinit var toolbar: Toolbar
    private val profileViewModel: ProfileUserAnimeViewModel by viewModels()
    private val args: ProfileUserAnimeFragmentArgs by navArgs()

    private var userName: String? = null
    private var currentStatus: String = UserAnimeListStatus.ALL.value


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            savedInstanceState.getString(STATUS_ARG)?.let { currentStatus = it }
            savedInstanceState.getString(USERNAME_ARG)?.let { userName = it }
        }else{
            profileViewModel.clearAnimeList()
            userName = args.username
            //userName?.let { profileViewModel.getUserAnimeList(it, currentStatus) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATUS_ARG, currentStatus)
        outState.putString(USERNAME_ARG, userName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileUserAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        animeListRecyclerView = binding.profileAnimeListRecycler
        animeListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        animeListRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        progressBar = binding.animeListProgressbar
        statusSpinner = binding.statusSpinner
        toolbar = binding.toolbar
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.jikan_anime_status,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = adapter
            statusSpinner.onItemSelectedListener = this
        } }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
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
                    progressBar.visibility = View.GONE
                    Timber.e("Error: ${resource.message}")
                    resource.message?.let { profileViewModel.showError(getString(R.string.error_user_list)) }
                }

            }
        }

        profileViewModel.getAnimeList.observe(viewLifecycleOwner){
            animeListAdapter.submitList(it)
        }

        profileViewModel.showErrorToast.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { msg -> context?.makeLongToast(msg) }
        }



    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
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