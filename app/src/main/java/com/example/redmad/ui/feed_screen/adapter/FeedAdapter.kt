package com.example.redmad.ui.feed_screen.adapter

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

class FeedAdapter(private val likeInterface: LikeInterface) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    var feedList: List<FeedElementModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = FeedItemBinding.inflate(inflater, parent, false)

        return FeedAdapter.FeedViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        val item = feedList[position]

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
        return feedList.size
    }

    class FeedViewHolder (var binding: FeedItemBinding): RecyclerView.ViewHolder(binding.root)

}