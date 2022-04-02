package com.example.redmad

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.redmad.data.model.*
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.ActivityMainBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.addpost_screen.fragment.AddPost
import com.example.redmad.ui.done_screen.fragment.DoneScreen
import com.example.redmad.ui.editprofile_screen.fragment.EditProfile
import com.example.redmad.ui.empty_screen.fragment.EmptyFragment
import com.example.redmad.ui.feed_screen.fragment.FeedFragment
import com.example.redmad.ui.profile_screen.fragment.Profile
import com.example.redmad.ui.search_screen.fragment.SearchFragment
import com.example.redmad.ui.signin_screen.fragment.SignInScreen
import com.example.redmad.ui.signup_screen.fragment.SignUpFirst
import com.example.redmad.ui.signup_screen.fragment.SignUpSecond
import com.example.redmad.ui.start_screen.fragment.StartScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody


interface StartScreenInterface{
    fun openSignInScreen()
    fun openSignUpScreen()
}
interface SignInScreenInterface{
    fun closeScreen()
    fun openSignUpScreen()
    fun openDoneScreen()
}
interface SignUpScreenInterface{
    fun closeScreen()
    fun openSignInScreen()
    fun openSignUpFirstScreen()
    fun openSignUpSecondScreen()
    fun openDoneScreen()
}
interface DoneScreenInterface{
    fun openEmptyScreen()
    fun openFeedScreen()
}
interface ProfileScreenInterface{
    fun openEditProfileScreen()
    fun openStartScreen()
    fun openSearchScreen()
}
interface ProfileEditScreenInterface{
    fun closeProfileEditScreen()
}
interface AddPostScreenInterface{
    fun closeAddPostScreen()
}
interface SearchScreenInterface{
    fun closeSearchScreen()
}
interface EmptyFragmentInterface{
    fun openSearchScreen()
}
interface EmptyFriendFragmentInterface{
    fun openSearchScreen()
}
interface EmptyLikeFragmentInterface{
    fun closeEmptyLikeScreen()
}
interface EmptyPostFragmentInterface{
    fun openAddScreen()
}
class MainActivity : AppCompatActivity(),
    StartScreenInterface,
    SignInScreenInterface,
    SignUpScreenInterface,
    DoneScreenInterface,
    ProfileScreenInterface,
    ProfileEditScreenInterface,
    AddPostScreenInterface,
    SearchScreenInterface,
    EmptyFragmentInterface,
    EmptyFriendFragmentInterface,
    EmptyLikeFragmentInterface,
    EmptyPostFragmentInterface {

    private lateinit var binding: ActivityMainBinding

    private lateinit var appViewModel: AppViewModel

    private val startScreen = StartScreen()
    private val signInScreen = SignInScreen()
    private val signUpFirstScreen = SignUpFirst()
    private val signUpSecondScreen = SignUpSecond()
    private val doneScreen = DoneScreen()
    private val emptyScreen = EmptyFragment()
    private val addPostScreen = AddPost()
    private val profileScreen = Profile()
    private val profileEditScreen = EditProfile()
    private val searchScreen = SearchFragment()
    private val feedScreen = FeedFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        replaceFragment(startScreen)

        navBarFunctions()

        binding.bottomNavigation.visibility = View.GONE

        viewModelTest()

        setContentView(binding.root)
    }

    private fun viewModelTest(){

//        val repository = (application as MainApplication).appRepository
//
//        appViewModel = ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)
//
//        CoroutineScope(Dispatchers.IO).launch {
//
//            appViewModel.login(SignModel("steven.marelly@gmail.com", "TestTest228"))
//
//            appViewModel.logout()
//
//            appViewModel.addFriend(FriendModel("4f5b0527-a559-4d97-8002-44d542dc9789"))
//            appViewModel.deleteFriend("4f5b0527-a559-4d97-8002-44d542dc9789")
//
//            appViewModel.getMe()
//
//            appViewModel.getMePosts()
//
//            appViewModel.addMePost("ЧотаТЕСТ", 5.33, 4.33)
//
//            appViewModel.getFeed()
//
//            appViewModel.dislikeFeed("5e20b607-cb70-4210-b90d-c19daa6aeb0f")
//
//            appViewModel.getLikedFeed()
//
//            appViewModel.search("test")
//
//        }


    }

    private fun navBarFunctions(){
        binding.bottomNavigation.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(feedScreen)
                R.id.ic_add -> {
                    replaceFragment(addPostScreen)
                    binding.bottomNavigation.visibility = View.GONE
                }
                R.id.ic_profile -> replaceFragment(profileScreen)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.mainActivityFragment.id, fragment)
            transaction.commit()
        }
    }

    override fun openSignInScreen() {
        replaceFragment(signInScreen)
    }

    override fun openSignUpFirstScreen() {
        replaceFragment(signUpFirstScreen)
    }

    override fun openSignUpSecondScreen() {
        replaceFragment(signUpSecondScreen)
    }

    override fun closeScreen() {
        replaceFragment(startScreen)
    }

    override fun openSignUpScreen() {
        replaceFragment(signUpFirstScreen)
    }

    override fun openDoneScreen() {
        replaceFragment(doneScreen)
    }

    override fun openEmptyScreen() {
        replaceFragment(emptyScreen)
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun openFeedScreen() {
        replaceFragment(feedScreen)
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun openEditProfileScreen() {
        replaceFragment(profileEditScreen)
        binding.bottomNavigation.visibility = View.GONE
    }

    override fun openStartScreen() {
        binding.bottomNavigation.setSelectedItemId(R.id.ic_home)
        binding.bottomNavigation.visibility = View.GONE
        replaceFragment(startScreen)
    }

    override fun openSearchScreen() {
        replaceFragment(searchScreen)
        binding.bottomNavigation.visibility = View.GONE
    }

    override fun closeAddPostScreen() {
        replaceFragment(feedScreen)
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.bottomNavigation.setSelectedItemId(R.id.ic_home)
    }

    override fun closeProfileEditScreen() {
        replaceFragment(profileScreen)
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun closeSearchScreen() {
        binding.bottomNavigation.setSelectedItemId(R.id.ic_profile)
        binding.bottomNavigation.visibility = View.VISIBLE
        replaceFragment(profileScreen)
    }

    override fun openAddScreen() {
        replaceFragment(addPostScreen)
        binding.bottomNavigation.visibility = View.GONE
    }

    override fun closeEmptyLikeScreen() {
        binding.bottomNavigation.setSelectedItemId(R.id.ic_home)
        replaceFragment(feedScreen)
    }

}