package com.shoppingfoodcart.firstassigment.views.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoppingfoodcart.firstassigment.databinding.FragmentMoviesBinding
import com.shoppingfoodcart.firstassigment.interfaces.onItemClickListener
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.receivers.InternetCheckReceiver
import com.shoppingfoodcart.firstassigment.utils.application.FirstAssignment
import com.shoppingfoodcart.firstassigment.utils.application.PreferencesUtil
import com.shoppingfoodcart.firstassigment.utils.application.showToast
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants
import com.shoppingfoodcart.firstassigment.viewmodel.MoviesViewModel
import com.shoppingfoodcart.firstassigment.views.adapter.MoviesAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<FragmentMoviesBinding>(),
    onItemClickListener, InternetCheckReceiver.ConnectivityReceiverListener {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by viewModel()
    private var movieListData = ArrayList<Results>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MoviesAdapter
    private var currentPageNo = 1
    private var isLastPage = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean)
            : FragmentMoviesBinding {
        binding = FragmentMoviesBinding.inflate(inflater, container, b)
        return binding
    }

    override fun initViews() {
        recyclerView = binding.recyclerViewMovies
        layoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        adapter = MoviesAdapter(this, movieListData, requireContext())
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener)


        currentPageNo = PreferencesUtil(requireContext()).getInt("pageNumber")

        binding.floatingActionButton.setOnClickListener {
            addFragment(SearchMovieFragment())
        }
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= MifareUltralight.PAGE_SIZE
            ) {
                if (!isLastPage) {
                    currentPageNo = if (movieListData.size == 0) {
                        AppConstants.STARTING_PAGE_NUMBER
                    } else {
                        currentPageNo
                    }
                    currentPageNo.let {
                        if (isOnline(requireContext())) {
                            viewModel.getMoviesList(it)
                        } else {
                            showToast("No Internet Available")
                        }
                    }
                }
            }
        }
    }

    override fun attachViewModel() {
        with(viewModel) {
            movieList.observe(viewLifecycleOwner) {
                isLastPage = currentPageNo > it.totalPages!!
                currentPageNo = it.page!! + 1
                PreferencesUtil(requireContext()).saveInt("pageNumber", currentPageNo)
                if (it.results.size > 0){
                    movieListData.addAll(it.results)
                }
                PreferencesUtil(requireContext()).saveData(movieListData)
                adapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onItemClickFav(item: Any, position: Int) {
        val movieObject = item as Results
        if (item.isSelected){
            viewModel.insertData(movieObject)
        }else {
            viewModel.deleteData(movieObject)
        }
        adapter.notifyItemChanged(position)
        PreferencesUtil(requireContext()).saveData(adapter.getAllItems())
    }

    override fun onItemClick(item: Any) {
        val movieObject = item as Results
        val fragment = MovieDetailFragment()
        val bundle = Bundle()
        bundle.putSerializable("value", movieObject)
        fragment.arguments = bundle
        addFragment(fragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FirstAssignment.mInstance?.setConnectivityListener(this);

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected)
            viewModel.getMoviesList(currentPageNo)
    }

    override fun onResume() {
        super.onResume()
        if (currentPageNo > 1){
            movieListData = PreferencesUtil(requireContext()).loadData()
            adapter.setFilterData(movieListData)
        }else{
            currentPageNo = 1
            viewModel.getMoviesList(currentPageNo)
        }
    }
}