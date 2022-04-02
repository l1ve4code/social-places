package com.example.redmad.ui.profile_screen.fragment

import android.location.Address
import android.location.Geocoder
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
import com.example.redmad.databinding.FragmentProfilePostsBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.feed_screen.fragment.LikeInterface
import com.example.redmad.ui.profile_screen.adapter.ProfilePostsAdapter
import kotlinx.coroutines.launch
import java.util.*

class ProfilePosts : Fragment() {

    private lateinit var binding: FragmentProfilePostsBinding
    private lateinit var appViewModel: AppViewModel

    private lateinit var postsAdapter: ProfilePostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfilePostsBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        postsAdapter = ProfilePostsAdapter(
            object : LikeInterface {
                override fun dislikePost(id: String) {
                    lifecycleScope.launch {
                        appViewModel.dislikeFeed(id)
                    }
                }

                override fun getLocationString(lat: Double, lon: Double): List<Address> {
                    val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list: List<Address> = geocoder.getFromLocation(lat, lon, 1)

                    return list
                }

                override fun likePost(id: String) {
                    lifecycleScope.launch {
                        appViewModel.likeFeed(id)
                    }
                }
            }
        )

        loadPosts()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.recyclerProfilePosts.layoutManager = layoutManager
        binding.recyclerProfilePosts.adapter = postsAdapter

        return binding.root

    }

    private fun loadPosts(){
        lifecycleScope.launch {
            appViewModel.getMePosts().observe(viewLifecycleOwner, Observer {
                postsAdapter.postsList = it
            })
            postsAdapter.notifyDataSetChanged()
        }
    }

}