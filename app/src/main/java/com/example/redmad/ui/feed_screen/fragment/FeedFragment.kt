package com.example.redmad.ui.feed_screen.fragment

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
import com.example.redmad.databinding.FragmentFeedBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.feed_screen.adapter.FeedAdapter
import kotlinx.coroutines.launch
import java.util.*

interface LikeInterface{
    fun likePost(id: String)
    fun dislikePost(id: String)
    fun getLocationString(lat: Double, lon: Double): List<Address>
}
class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var appViewModel: AppViewModel

    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        feedAdapter = FeedAdapter(
            object : LikeInterface{
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

        loadFeedElements()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.recyclerFeed.layoutManager = layoutManager
        binding.recyclerFeed.adapter = feedAdapter

        return binding.root

    }

    private fun loadFeedElements() {
        lifecycleScope.launch {
            appViewModel.getFeed().observe(viewLifecycleOwner, Observer {
                feedAdapter.feedList = it
            })
            feedAdapter.notifyDataSetChanged()
        }
    }

}