package com.shoppingfoodcart.firstassigment.views.fragments

import android.R
import android.content.Context
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shoppingfoodcart.firstassigment.databinding.FragmentSearchMovieBinding
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


class SearchMovieFragment : BaseFragment<FragmentSearchMovieBinding>(),
    onItemClickListener, InternetCheckReceiver.ConnectivityReceiverListener {

    private lateinit var binding: FragmentSearchMovieBinding
    private val viewModel: MoviesViewModel by viewModel()
    private var movieListData = ArrayList<Results>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MoviesAdapter
    private var currentPageNo = 1
    private var isLastPage = false
    private lateinit var searchedValue : ArrayList<String>

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSearchMovieBinding {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, b)
        return binding
    }

    override fun initViews() {
        recyclerView = binding.recyclerSearchMovies
        layoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        adapter = MoviesAdapter(this, movieListData, requireContext())
        recyclerView.adapter = adapter

        setAutoCompleteList()
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener)

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!binding.etSearch.text.toString().trim().isNullOrEmpty()) {
//                    Log.e("search ", "search ${binding.etSearch.text}")
//                    adapter.setFilterData(ArrayList<Results>())
                    movieListData.clear()
                    currentPageNo = 1
                    viewModel.getSearchMoviesList(binding.etSearch.text.toString(), currentPageNo)
                }
                return@OnEditorActionListener true
            }
            false
        })
        //viewModel.getMoviesList(currentPageNo)
    }

    private fun setAutoCompleteList() {
        searchedValue = PreferencesUtil(requireContext()).loadSearchData()
//        Log.e("searchedValue ","searchedValue List $searchedValue")
        if (searchedValue.size > 0){
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                R.layout.simple_dropdown_item_1line, searchedValue
            )
            binding.etSearch.setAdapter(adapter)
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
                            viewModel.getSearchMoviesList(binding.etSearch.text.toString(), it)
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
            searchMoviesList.observe(viewLifecycleOwner) {
//                Log.e("response ", "response totalPages ${it.totalPages}")
                isLastPage = currentPageNo > it.totalPages!!
                currentPageNo = it.page!! + 1
                if (it.results.size > 0) {
//                    Log.e("response ", "response result size ${it.results.size}")
                    for (item in it.results) {
//                        Log.e("response ","response movieObject $item")
                        checkListItemSaveOrNot(item)
                    }
                    saveLocalSearch()
                }
            }
        }
    }

    private fun saveLocalSearch() {
        val searching = binding.etSearch.text.toString()
//        var isFound = false
        if (searchedValue.size == 0){
            searchedValue.add(searching)
        }else{
            for (item in searchedValue){
                if (item == searching) {
                    searchedValue.remove(searching)
//                    isFound = true
                    break
                }
            }
//            if (!isFound)
                searchedValue.add(searching)
            if (searchedValue.size > 10)
                searchedValue.removeAt(searchedValue.size -1)
//            Log.e("searchedValue ","searchedValue $searchedValue")
            PreferencesUtil(requireContext()).saveSearchData(searchedValue)
            setAutoCompleteList()
        }
    }

    private fun checkListItemSaveOrNot(movieObject: Results) {
        val movieUpdateList = PreferencesUtil(requireContext()).loadData()
        for (item in movieUpdateList) {
            if (item.id == movieObject.id) {
                movieObject.isSelected = item.isSelected
            }
            movieListData.add(movieObject)
        }
        adapter.setFilterData(movieListData)
    }

    override fun onItemClickFav(item: Any, position: Int) {
        val movieObject = item as Results
        if (item.isSelected) {
            viewModel.insertData(movieObject)
        } else {
            viewModel.deleteData(movieObject)
        }
        adapter.notifyItemChanged(position)
        checkListItem(item)
    }

    private fun checkListItem(movieObject: Results) {
        val movieUpdateList = PreferencesUtil(requireContext()).loadData()
        for (item in movieUpdateList) {
            if (item.id == movieObject.id) {
                item.isSelected = movieObject.isSelected
            }
            movieListData.add(movieObject)
        }
        adapter.notifyDataSetChanged()
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected)
            viewModel.getMoviesList(currentPageNo)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FirstAssignment.mInstance?.setConnectivityListener(this);

    }

}