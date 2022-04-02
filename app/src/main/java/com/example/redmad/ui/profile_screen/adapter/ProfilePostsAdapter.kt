package com.example.redmad.ui.profile_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.redmad.R
import com.example.redmad.data.model.FeedElementModel
import com.example.redmad.databinding.FeedItemBinding
import com.example.redmad.ui.feed_screen.fragment.LikeInterface
import java.lang.Exception

class ProfilePostsAdapter(private val likeInterface: LikeInterface) : RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostsViewHolder>() {

    var postsList: List<FeedElementModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostsViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = FeedItemBinding.inflate(inflater, parent, false)

        return ProfilePostsAdapter.ProfilePostsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ProfilePostsViewHolder, position: Int) {

        val item = postsList[position]

        with(holder.binding){

            if(item.liked){
                likeButton.setImageResource(R.drawable.like_icon_enabled)
                likeButton.tag = "LIKE"
            }else{
                likeButton.setImageResource(R.drawable.like_icon)
                likeButton.tag = "NOT LIKED"
            }

            Glide.with(image.context)
                .load(item.image_url)
                .placeholder(R.drawable.feed_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(image)

            title.text = item.text.replace("\"", "")
            userName.text = "@" + item.author.nickname

            try {
                locName.text = likeInterface.getLocationString(item.lat, item.lon).get(0).countryName + ", " + likeInterface.getLocationString(item.lat, item.lon).get(0).locality
                if(likeInterface.getLocationString(item.lat, item.lon).get(0).locality == null){
                    locName.text = likeInterface.getLocationString(item.lat, item.lon).get(0).countryName + ", " + likeInterface.getLocationString(item.lat, item.lon).get(0).adminArea
                }
            }catch (e: Exception){
            }

            likeButton.setOnClickListener {
                if(likeButton.tag == "LIKE"){
                    likeButton.setImageResource(R.drawable.like_icon)
                    likeInterface.dislikePost(item.id)
                    likeButton.tag = "NOT LIKED"
                }else{
                    likeButton.setImageResource(R.drawable.like_icon_enabled)
                    likeInterface.likePost(item.id)
                    likeButton.tag = "LIKE"
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    class ProfilePostsViewHolder (var binding: FeedItemBinding): RecyclerView.ViewHolder(binding.root)
}