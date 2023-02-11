package com.shoppingfoodcart.firstassigment.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shoppingfoodcart.firstassigment.databinding.FragmentMoviesBinding
import com.shoppingfoodcart.firstassigment.interfaces.onItemClickListener
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.application.PreferencesUtil
import com.shoppingfoodcart.firstassigment.utils.application.showToast
import com.shoppingfoodcart.firstassigment.viewmodel.MoviesViewModel
import com.shoppingfoodcart.firstassigment.views.adapter.MoviesAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentMoviesBinding>(),
    onItemClickListener{

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by viewModel()
    private var movieListData = ArrayList<Results>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoviesAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean)
            : FragmentMoviesBinding {
        binding = FragmentMoviesBinding.inflate(inflater, container, b)
        return binding
    }

    override fun initViews() {
        recyclerView = binding.recyclerViewMovies
        val layoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        adapter = MoviesAdapter(this, movieListData, requireContext())
        recyclerView.adapter = adapter

        viewModel.getFavroiteList()

        binding.floatingActionButton.hide()
    }

    override fun onItemClickFav(item: Any, position : Int) {
        val movieObject = item as Results
        viewModel.deleteData(movieObject)
            movieListData.removeAt(position)
            adapter.notifyDataSetChanged()
        if (movieListData.size == 0)
            showToast("There is no Favorite item")
        removeForCache(movieObject)
    }

    private fun removeForCache(movieObject: Results) {
        val movieUpdateList = PreferencesUtil(requireContext()).loadData()
        for (item in movieUpdateList){
            if (item.id == movieObject.id){
                item.isSelected = false
            }
        }
        PreferencesUtil(requireContext()).saveData(movieUpdateList)
    }

    override fun onItemClick(item: Any) {
        val movieObject = item as Results
        val fragment = MovieDetailFragment()
        val bundle = Bundle()
        bundle.putSerializable("value", movieObject)
        fragment.arguments = bundle
        addFragment(fragment)
    }

    override fun attachViewModel() {
        with(viewModel) {
            movieListFav.observe(viewLifecycleOwner) {
                if (it.size > 0) {
                    movieListData.addAll(it)
                } else {
                    showToast("There is no Favorite item")
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

}