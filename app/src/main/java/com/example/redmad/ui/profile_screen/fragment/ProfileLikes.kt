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
import com.example.redmad.databinding.FragmentProfileLikesBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.feed_screen.fragment.LikeInterface
import com.example.redmad.ui.profile_screen.adapter.ProfileLikesAdapter
import kotlinx.coroutines.launch
import java.util.*

interface LikeInterface{
    fun likePost(id: String)
    fun dislikePost(id: String)
}
class ProfileLikes : Fragment() {

    private lateinit var binding: FragmentProfileLikesBinding
    private lateinit var appViewModel: AppViewModel

    private lateinit var likesAdapter: ProfileLikesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileLikesBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        likesAdapter = ProfileLikesAdapter(
            object : LikeInterface {
                override fun dislikePost(id: String) {
                    lifecycleScope.launch {
                        appViewModel.dislikeFeed(id)
                    }
                    loadlikesData()
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

        loadlikesData()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.recyclerProfileLikes.layoutManager = layoutManager
        binding.recyclerProfileLikes.adapter = likesAdapter

        return binding.root

    }

    private fun loadlikesData(){

        lifecycleScope.launch {
            appViewModel.getLikedFeed().observe(viewLifecycleOwner, Observer {
                likesAdapter.likesList = it
            })
            likesAdapter.notifyDataSetChanged()
        }

    }

}