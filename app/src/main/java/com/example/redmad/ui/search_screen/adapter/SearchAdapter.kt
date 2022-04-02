package com.example.redmad.ui.search_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redmad.data.model.UserModel
import com.example.redmad.databinding.FriendItemBinding
import com.example.redmad.databinding.LkFriendItemBinding
import com.example.redmad.ui.profile_screen.adapter.ProfileFriendsAdapter
import com.example.redmad.ui.search_screen.fragment.AdapterInterface

class SearchAdapter(private val adapterInterface: AdapterInterface) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var userList: List<UserModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = FriendItemBinding.inflate(inflater, parent, false)

        return SearchAdapter.SearchViewHolder(binding)

    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = userList[position]

        with(holder.binding){
            nameInput.text = item.first_name + " " + item.last_name
            usernameInput.text = "@" + item.nickname

            addToFriends.setOnClickListener {
                adapterInterface.addFriend(item.id)
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class SearchViewHolder (var binding: FriendItemBinding): RecyclerView.ViewHolder(binding.root)
}