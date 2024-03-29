package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.R
import com.destructo.sushi.USERNAME_ARG
import com.destructo.sushi.adapter.JikanUserFriendAdapter
import com.destructo.sushi.databinding.FragmentProfileFriendsBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFriendsFragment : BaseFragment(), ListEndListener {

    private lateinit var binding: FragmentProfileFriendsBinding
    private lateinit var friendListRecyclerView: RecyclerView
    private lateinit var friendListAdapter: JikanUserFriendAdapter
    private lateinit var progressBar: ProgressBar
    private val profileViewModel: ProfileViewModel by
    viewModels(ownerProducer = {requireParentFragment()})

    private var userName: String? = null

    companion object{
        fun newInstance(userName:String?): ProfileFriendsFragment {
            val profileFriendListFragment = ProfileFriendsFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME_ARG, userName)
            profileFriendListFragment.arguments = bundle
            return profileFriendListFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            savedInstanceState.getString(USERNAME_ARG)?.let { userName = it }
        }else{
            userName = arguments?.getString(USERNAME_ARG)
            userName?.let {
                profileViewModel.clearFriendList(it)
                profileViewModel.getUserFriendList(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERNAME_ARG, userName)
    }

    override fun onDestroy() {
        userName?.let { profileViewModel.clearFriendList(it) }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileFriendsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        friendListRecyclerView = binding.profileFriendListRecycler
        friendListRecyclerView.layoutManager = LinearLayoutManager(context)
        friendListRecyclerView.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
        progressBar = binding.friendListProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        friendListAdapter = JikanUserFriendAdapter(MalUrlListener {
            it?.let{ navigateToUserProfile(it) }
        })
        friendListAdapter.setListEndListener(this)
        friendListRecyclerView.adapter = friendListAdapter
        profileViewModel.userFriendList.observe(viewLifecycleOwner){resource ->

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

        profileViewModel.getUserFriendListByUsername(userName!!).observe(viewLifecycleOwner){
            friendListAdapter.submitList(it)
        }
    }

    private fun navigateToUserProfile(username: String) {
        this.findNavController().navigate(
            R.id.profileFragment,
            bundleOf(Pair(USERNAME_ARG, username)),
            getAnimNavOptions()
        )
    }

    override fun onEndReached(position: Int) {
        userName?.let { profileViewModel.getUserFriendList(it) }
    }
}