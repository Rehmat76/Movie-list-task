package com.shoppingfoodcart.firstassigment.views.activity

import androidx.lifecycle.Observer
import com.shoppingfoodcart.firstassigment.R
import com.shoppingfoodcart.firstassigment.databinding.ActivityMainBinding
import com.shoppingfoodcart.firstassigment.viewmodel.MoviesViewModel
import com.shoppingfoodcart.firstassigment.views.fragments.FavoriteFragment
import com.shoppingfoodcart.firstassigment.views.fragments.HomeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var binding: ActivityMainBinding

    var needBackMove: Boolean = false
    var clearbackStack: Boolean = false

    val viewModel: MoviesViewModel by viewModel()

    override fun getViewBinding(): ActivityMainBinding {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding
    }

    override fun initViews() {
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.favorite -> replaceFragment(FavoriteFragment())
                else ->
                    replaceFragment(HomeFragment())
            }
            true
        }

        setupViewModel()
    }


    /*  override fun onCreateOptionsMenu(menu: Menu): Boolean {
      menuInflater.inflate(R.menu.main, menu)
      return true
  }*/




    private fun exit() {
        finish()
    }


    private fun setupViewModel() {
        with(viewModel!!)
        {

            snackbarMessage.observe(this@MainActivity, Observer {
                it.getContentIfNotHandled()?.let {
//                    showAlertDialog(it)
                }
            })

            progressBar.observe(this@MainActivity, Observer {
                it.getContentIfNotHandled()?.let {
                    showProgressDialog(it)
                }
            })
        }
    }

    override fun onBackPressed() {
        if (clearbackStack) {
            exit()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
    }
}