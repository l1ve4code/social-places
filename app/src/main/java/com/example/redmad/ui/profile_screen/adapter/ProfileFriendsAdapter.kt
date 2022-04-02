package com.example.redmad.ui.profile_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redmad.data.model.UserModel
import com.example.redmad.databinding.LkFriendItemBinding
import com.example.redmad.ui.profile_screen.fragment.FriendInterface
import com.example.redmad.ui.search_screen.fragment.AdapterInterface

class ProfileFriendsAdapter(private val friendInterface: FriendInterface) : RecyclerView.Adapter<ProfileFriendsAdapter.ProfileFriendsViewHolder>() {

    var friendsList: List<UserModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFriendsViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = LkFriendItemBinding.inflate(inflater, parent, false)

        return ProfileFriendsAdapter.ProfileFriendsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ProfileFriendsViewHolder, position: Int) {

        val item = friendsList[position]

        with(holder.binding){
            nameInput.text = item.first_name + " " + item.last_name
            usernameInput.text = "@" + item.nickname

            addToFriends.setOnClickListener {
                friendInterface.deleteFriend(item.id)
            }

        }

    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    class ProfileFriendsViewHolder (var binding: LkFriendItemBinding): RecyclerView.ViewHolder(binding.root)
}