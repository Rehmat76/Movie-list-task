package com.shoppingfoodcart.firstassigment.views.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.shoppingfoodcart.firstassigment.R
import com.shoppingfoodcart.firstassigment.databinding.ItemMoviesBinding
import com.shoppingfoodcart.firstassigment.interfaces.onItemClickListener
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.POSTER_PATH

class MoviesAdapter(
    private val listener: onItemClickListener,
    private var moviesList: List<Results>,
    var context: Context
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder private constructor(private val binding: ItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movieResultModel: Results,
            listener: onItemClickListener,
            position: Int,
            context: Context
        ) {

//            val currentPosition = position + 1

            if (movieResultModel.posterPath != null) {
                val imageUri = POSTER_PATH + movieResultModel.posterPath

                Glide.with(binding.imgMoviePoster.context)
                    .asBitmap()
                    .load(imageUri)
                    .into(object : SimpleTarget<Bitmap>() {

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            binding.imgMoviePoster.setImageBitmap(resource)

                        }
                    })
            }
            binding.lblMovieName.text = movieResultModel.title
            binding.lblReleaseDate.text = movieResultModel.releaseDate

            if (movieResultModel.isSelected){
                binding.imgFavorite.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.SRC_IN);
            }else{
                binding.imgFavorite.setColorFilter(ContextCompat.getColor(context, android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            binding.root.setOnClickListener {
                listener.onItemClick(movieResultModel)
            }

            binding.imgFavorite.setOnClickListener {
                movieResultModel.isSelected = !movieResultModel.isSelected

                listener.onItemClickFav(movieResultModel, position)
            }

        }

        companion object {

            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMoviesBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = moviesList[position]
        item.let { holder.bind(it, listener, position, context) }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setFilterData(moviesList: ArrayList<Results>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    fun getAllItems(): ArrayList<Results> {
        return moviesList as ArrayList<Results>
    }

}