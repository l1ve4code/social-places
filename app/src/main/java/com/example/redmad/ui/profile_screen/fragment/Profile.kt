package com.example.redmad.ui.profile_screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.redmad.ProfileScreenInterface
import com.example.redmad.R
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentProfileBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.empty_screen.fragment.FriendEmpty
import com.example.redmad.ui.empty_screen.fragment.LikesEmpty
import com.example.redmad.ui.empty_screen.fragment.PostsEmpty
import kotlinx.coroutines.launch
import java.util.*

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var appViewModel: AppViewModel

    private val profileLikes = ProfileLikes()
    private val profileFriends = ProfileFriends()
    private val profilePosts = ProfilePosts()

    private val friendEmpty = FriendEmpty()
    private val likesEmpty = LikesEmpty()
    private val postsEmpty = PostsEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        loadUserData()

        setButtonActions()

        profileMenuFunctions()

        onLoad()

        return binding.root

    }

    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()
        dob.set(year, month, day)
        var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    private fun onLoad(){
        lifecycleScope.launch {
            appViewModel.getMePosts().observe(viewLifecycleOwner, Observer {
                if (it.size > 0) {
                    replaceFragment(profilePosts)
                } else {
                    replaceFragment(postsEmpty)
                }
            })
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            appViewModel.getMe().observe(viewLifecycleOwner, Observer {
                binding.userName.text = "@" + it.nickname
                binding.userFio.text = it.first_name + " " + it.last_name
                binding.userAge.text = getAge(it.birth_day.split("-")[0].toInt(), it.birth_day.split("-")[1].toInt(), it.birth_day.split("-")[2].toInt()) + " лет"
            })
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = getParentFragmentManager().beginTransaction()
            transaction.replace(binding.profileFragment.id, fragment)
            transaction.commit()
        }
    }

    private fun profileMenuFunctions() {
        binding.postsSelect.setOnClickListener {
            unsetButtonColors()
            lifecycleScope.launch {
                appViewModel.getMePosts().observe(viewLifecycleOwner, Observer {
                    if (it.size > 0) {
                        replaceFragment(profilePosts)
                    } else {
                        replaceFragment(postsEmpty)
                    }
                })
            }
            binding.postsSelect.setTextColor(resources.getColor(R.color.black))
        }
        binding.likeSelect.setOnClickListener {
            unsetButtonColors()
            lifecycleScope.launch {
                appViewModel.getLikedFeed().observe(viewLifecycleOwner, Observer {
                    if (it.size > 0) {
                        replaceFragment(profileLikes)
                    } else {
                        replaceFragment(likesEmpty)
                    }
                })
            }
            binding.likeSelect.setTextColor(resources.getColor(R.color.black))
        }
        binding.friendsSelect.setOnClickListener {
            unsetButtonColors()
            lifecycleScope.launch {
                appViewModel.getFriends().observe(viewLifecycleOwner, Observer {
                    if (it.size > 0) {
                        replaceFragment(profileFriends)
                    } else {
                        replaceFragment(friendEmpty)
                    }
                })
            }
            binding.friendsSelect.setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun unsetButtonColors() {
        binding.postsSelect.setTextColor(resources.getColor(R.color.disabled))
        binding.likeSelect.setTextColor(resources.getColor(R.color.disabled))
        binding.friendsSelect.setTextColor(resources.getColor(R.color.disabled))
    }

    private fun setButtonActions() {

        val profileScreenInterface = requireActivity() as ProfileScreenInterface

        binding.editProfileButton.setOnClickListener {
            profileScreenInterface.openEditProfileScreen()
        }

        binding.exitButton.setOnClickListener {
            profileScreenInterface.openStartScreen()
            lifecycleScope.launch {
                appViewModel.logout()
            }
        }

        binding.addFriendButton.setOnClickListener {
            profileScreenInterface.openSearchScreen()
        }

    }

}