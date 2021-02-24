package com.destructo.sushi.ui.user.profile.mangalist

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
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.USERNAME_ARG
import com.destructo.sushi.adapter.JikanUserMangaListAdapter
import com.destructo.sushi.databinding.FragmentProfileUserMangaBinding
import com.destructo.sushi.enum.jikan.UserMangaListStatus
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileUserMangaFragment : BaseFragment(), ListEndListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentProfileUserMangaBinding
    private lateinit var mangaListRecyclerView: RecyclerView
    private lateinit var mangaListAdapter: JikanUserMangaListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var statusSpinner: Spinner
    private lateinit var toolbar: Toolbar
    private val profileViewModel: ProfileUserMangaViewModel by viewModels()
    private val args: ProfileUserMangaFragmentArgs by navArgs()

    private var userName: String? = null
    private var currentStatus: String = UserMangaListStatus.READING.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            savedInstanceState.getString(USERNAME_ARG)?.let { currentStatus = it }
            savedInstanceState.getString(USERNAME_ARG)?.let { userName = it }
        }else{
            userName = arguments?.getString(USERNAME_ARG)
            profileViewModel.clearMangaList()
            //userName?.let { profileViewModel.getUserMangaList(it, currentStatus) }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERNAME_ARG, currentStatus)
        outState.putString(USERNAME_ARG, userName)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileUserMangaBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        mangaListRecyclerView = binding.profileMangaListRecycler
        mangaListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        mangaListRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        progressBar = binding.mangaListProgressbar
        statusSpinner = binding.statusSpinner
        toolbar = binding.toolbar
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.jikan_manga_status,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = adapter
            statusSpinner.onItemSelectedListener = this
        } }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        mangaListAdapter = JikanUserMangaListAdapter(MalIdListener {
            it?.let{ navigateToMangaDetails(it) }
        })
        mangaListAdapter.setListEndListener(this)
        mangaListRecyclerView.adapter = mangaListAdapter
        profileViewModel.userMangaList.observe(viewLifecycleOwner){resource ->

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
                }

            }
        }

        profileViewModel.getMangaList.observe(viewLifecycleOwner){
            mangaListAdapter.submitList(it)
        }

    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateToMangaDetails(mangaMalId: Int) {
        this.findNavController().navigate(
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaMalId)),
            getAnimNavOptions()
        )
    }

    override fun onEndReached(position: Int) {
        userName?.let { profileViewModel.getUserMangaList(it, currentStatus) }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (view!=null){
            when (parent?.getItemAtPosition(pos).toString()) {
                getString(R.string.all) -> {
                    onStatusChanged(UserMangaListStatus.ALL.value)
                }
                getString(R.string.reading) -> {
                    onStatusChanged(UserMangaListStatus.READING.value)
                }
                getString(R.string.plan_to_read) -> {
                    onStatusChanged(UserMangaListStatus.PLAN_TO_READ.value)
                }
                getString(R.string.on_hold) -> {
                    onStatusChanged(UserMangaListStatus.ON_HOLD.value)
                }
                getString(R.string.dropped) -> {
                    onStatusChanged(UserMangaListStatus.DROPPED.value)
                }
                getString(R.string.completed) -> {
                    onStatusChanged(UserMangaListStatus.COMPLETED.value)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun onStatusChanged(status: String){
        currentStatus = status
        profileViewModel.clearMangaList()
        userName?.let { profileViewModel.getUserMangaList(it, currentStatus) }
    }
}