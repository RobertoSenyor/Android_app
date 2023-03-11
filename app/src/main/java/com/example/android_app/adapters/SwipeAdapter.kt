package com.example.android_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.android_app.Entities.MatchingCard
import com.example.android_app.Entities.MatchingData
import com.example.android_app.R
import com.squareup.picasso.Picasso

class SwipeAdapter(context: Context, list: MutableList<Integer>) : BaseAdapter() {
    private val context: Context
    private val list: MutableList<Integer>
    private var size: Int = 16
    override fun getCount(): Int {
        return size
    }

    fun setSize(newSize: Int) {
        this.size = newSize
    }

    override fun getItem(position: Int): Integer {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(data: List<Integer>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val holder: DataViewHolder
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_koloda, parent, false)
            holder = DataViewHolder(view)
            view?.tag = holder
        } else {
            holder = view.tag as DataViewHolder
            //view = convertView
        }

        if (position < MatchingData.getInstance().matchingCards.size)
            holder.bindData(context, MatchingData.getInstance().matchingCards[position])

        return view
    }

    /**
     * Static view items holder
     */
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var userAvatar: ImageView = view.findViewById(R.id.user_avatar)
        private var userName: TextView = view.findViewById(R.id.username)
        private var aboutUser: TextView = view.findViewById(R.id.about)

        private var gameImage1: ImageView = view.findViewById(R.id.game_avatar_1)
        private var gameName1: TextView = view.findViewById(R.id.game_name_1)
        private var gameHours1: TextView = view.findViewById(R.id.game_hours_1)

        private var gameImage2: ImageView = view.findViewById(R.id.game_avatar_2)
        private var gameName2: TextView = view.findViewById(R.id.game_name_2)
        private var gameHours2: TextView = view.findViewById(R.id.game_hours_2)

        private var gameImage3: ImageView = view.findViewById(R.id.game_avatar_3)
        private var gameName3: TextView = view.findViewById(R.id.game_name_3)
        private var gameHours3: TextView = view.findViewById(R.id.game_hours_3)

        internal fun bindData(context: Context, data: MatchingCard) {
            val transforms = RequestOptions().transform(CenterCrop())
                    .transform(RoundedCorners(200))
            val transforms2 = RequestOptions().transform(CenterCrop())
                    .transform(RoundedCorners(50))
            Glide.with(context)
                    .load(data.avatarURL/*MatchingData.getInstance().avatarURLs*/)
                    .apply(transforms)
                    .into(userAvatar)
            userName.setText(data.username)
            aboutUser.setText(data.aboutUser)
            Glide.with(context)
                    .load(data.gameCards.get(0).coverURL)
                    .apply(transforms2)
                    .into(gameImage1)
            gameName1.setText(data.gameCards.get(0).gameName)
            gameHours1.setText(data.gameCards.get(0).minutes.toString())
            Glide.with(context)
                    .load(data.gameCards.get(1).coverURL)
                    .apply(transforms2)
                    .into(gameImage2)
            gameName2.setText(data.gameCards.get(1).gameName)
            gameHours2.setText(data.gameCards.get(1).minutes.toString())
            Glide.with(context)
                    .load(data.gameCards.get(2).coverURL)
                    .apply(transforms2)
                    .into(gameImage3)
            gameName3.setText(data.gameCards.get(2).gameName)
            gameHours3.setText(data.gameCards.get(2).minutes.toString())
            //Picasso.get().load("URL").into(userAvatar);
            /*Glide.with(context)
                    .load(data)
                    .apply(transforms)
                    .into(userAvatar)*/
        }

    }

    init {
        this.context = context
        this.list = list
    }
}