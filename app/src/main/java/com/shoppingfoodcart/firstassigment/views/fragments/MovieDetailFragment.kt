package com.shoppingfoodcart.firstassigment.views.fragments

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.shoppingfoodcart.firstassigment.R
import com.shoppingfoodcart.firstassigment.databinding.FragmentMovieDetailBinding
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.application.PreferencesUtil
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants
import com.shoppingfoodcart.firstassigment.viewmodel.MoviesViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {

//    private lateinit var binding: FragmentMovieDetailBinding
    private val viewModel: MoviesViewModel by viewModel()
    private var container: ViewGroup? = null
    private var inflater: LayoutInflater? = null
    private var myValue : Results? = null
    private var textView: TextView? = null
    private var lblMovieName: TextView? = null
    private var lblReleaseDate: TextView? = null
    private var imgMoviePoster: ImageView? = null
    private var imgFavorite: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Instantiate our container and inflater handles.
        this.container = container
        this.inflater = inflater

        // Display the desired layout and return the view.
        return initializeUserInterface();
    }
    /*override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentMovieDetailBinding {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, b)
        return binding
    }*/

    private fun removeForCache(movieObject: Results) {
        val movieUpdateList = PreferencesUtil(requireContext()).loadData()
        for (item in movieUpdateList){
            if (item.id == movieObject.id){
                item.isSelected = movieObject.isSelected
            }
        }
        PreferencesUtil(requireContext()).saveData(movieUpdateList)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        // Create the new layout.
        val view: View = initializeUserInterface()

        // Display the new layout on the screen.
        container?.addView(view);
        super.onConfigurationChanged(newConfig)
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showToast("LandScape")
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            showToast("Portrait")
        }*/
    }

    private fun initValues() {
        if (myValue != null) {
            val imageUri = AppConstants.POSTER_PATH + myValue!!.posterPath
            Glide.with(imgMoviePoster!!.context)
                .asBitmap()
                .load(imageUri)
                .into(object : SimpleTarget<Bitmap>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        imgMoviePoster!!.setImageBitmap(resource)

                    }
                })
            lblMovieName!!.text = myValue!!.title
            lblReleaseDate!!.text = myValue!!.releaseDate
            if (myValue!!.isSelected) {
                imgFavorite!!.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_dark
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );
            } else {
                imgFavorite!!.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );
            }
            imgFavorite!!.setOnClickListener {
                myValue!!.isSelected = !myValue!!.isSelected
                if (myValue!!.isSelected) {
                    imgFavorite!!.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    );
                    viewModel.insertData(myValue!!)
                } else {
                    imgFavorite!!.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    );
                    viewModel.deleteData(myValue!!)
                }
                removeForCache(myValue!!)
            }
        }
    }

    private fun initializeUserInterface(): View {
        var view: View

        // If there is already a layout inflated, remove it.
        if (container != null) {
            container!!.removeAllViewsInLayout()
        }

        // Get the screen orientation.
        val orientation = requireContext().resources.configuration.orientation
        // Inflate the appropriate layout based on the screen orientation.
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = inflater!!.inflate(R.layout.fragment_movie_detail, container, false);
        }else { // orientation == Configuration.ORIENTATION_LANDSCAPE
            view = inflater!!.inflate(R.layout.fragment_movie_detail_land, container, false);
        }
        lblMovieName = view.findViewById(R.id.lblMovieName)
        lblReleaseDate = view.findViewById(R.id.lblReleaseDate)
        imgMoviePoster = view.findViewById(R.id.imgMoviePoster)
        imgFavorite = view.findViewById(R.id.imgFavorite)
        val bundle = this.arguments
        if (bundle != null) {
            myValue  = bundle.getSerializable("value") as Results
        }
            initValues()
        return view
    }

    override fun onDestroyView() {
        if (container != null) {
            container!!.removeAllViewsInLayout()
        }
        super.onDestroyView()
    }
}