package com.example.redmad.ui.profile_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentProfileFriendsBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.profile_screen.adapter.ProfileFriendsAdapter
import kotlinx.coroutines.launch

interface FriendInterface{
    fun deleteFriend(id: String)
}
class ProfileFriends : Fragment() {

    private lateinit var binding: FragmentProfileFriendsBinding
    private lateinit var appViewModel: AppViewModel

    private lateinit var friendsAdapter: ProfileFriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileFriendsBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        friendsAdapter = ProfileFriendsAdapter(
            object : FriendInterface{
                override fun deleteFriend(id: String) {
                    lifecycleScope.launch {
                        appViewModel.deleteFriend(id)
                    }
                    loadFriendsData()
                }
            }
        )

        loadFriendsData()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.recyclerProfileFriends.layoutManager = layoutManager
        binding.recyclerProfileFriends.adapter = friendsAdapter

        return binding.root

    }

    private fun loadFriendsData() {

        lifecycleScope.launch {
            appViewModel.getFriends().observe(viewLifecycleOwner, Observer {
                friendsAdapter.friendsList = it
            })
            friendsAdapter.notifyDataSetChanged()
        }

    }

}