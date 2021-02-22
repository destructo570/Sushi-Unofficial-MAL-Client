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
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.JikanUserMangaListAdapter
import com.destructo.sushi.databinding.FragmentProfileMangaListBinding
import com.destructo.sushi.enum.jikan.UserAnimeListStatus
import com.destructo.sushi.enum.jikan.UserMangaListStatus
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import timber.log.Timber

class ProfileMangaListFragment : Fragment(), ListEndListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentProfileMangaListBinding
    private lateinit var mangaListRecyclerView: RecyclerView
    private lateinit var mangaListAdapter: JikanUserMangaListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var statusSpinner: Spinner
    private val profileViewModel: ProfileViewModel by
    viewModels(ownerProducer = {requireParentFragment()})

    private var userName: String? = null
    private var currentStatus: String = UserAnimeListStatus.ALL.value

    companion object{
        fun newInstance(userName:String?): ProfileMangaListFragment {
            val profileMangaListFragment = ProfileMangaListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, userName)
            profileMangaListFragment.arguments = bundle
            return profileMangaListFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            savedInstanceState.getString(ARG_STATUS)?.let { currentStatus = it }
            savedInstanceState.getString(ARG_USERNAME)?.let { userName = it }
        }else{
            userName = arguments?.getString(ARG_USERNAME)
            profileViewModel.clearMangaList()
            userName?.let { profileViewModel.getUserMangaList(it, currentStatus) }
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

        binding = FragmentProfileMangaListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        mangaListRecyclerView = binding.profileMangaListRecycler
        mangaListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        mangaListRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        progressBar = binding.mangaListProgressbar
        statusSpinner = binding.statusSpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.jikan_manga_status,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = adapter
            statusSpinner.onItemSelectedListener = this
        } }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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



    private fun navigateToMangaDetails(mangaMalId: Int) {
        this.findNavController().navigate(
            R.id.mangaDetailsFragment, bundleOf(Pair(MANGA_ID_ARG, mangaMalId))
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