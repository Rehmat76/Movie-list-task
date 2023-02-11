package com.shoppingfoodcart.firstassigment.views.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.shoppingfoodcart.firstassigment.R
import com.shoppingfoodcart.firstassigment.utils.application.openActivityWithExist
import com.shoppingfoodcart.firstassigment.utils.general.DialogUtils
import com.shoppingfoodcart.firstassigment.utils.network.SessionManager

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    private var dialog: AlertDialog? = null
    private lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.MODE_NIGHT_YES
        binding = getViewBinding()
        setContentView(binding.root)
        dialog = DialogUtils.getProgressDialog(this)
        initViews()
    }

    abstract fun getViewBinding(): B

    abstract fun initViews()


    fun gotoMainActivity() {
        openActivityWithExist(MainActivity::class.java)
    }


    fun showProgressDialog(show: Boolean) {
        if (dialog != null && show) {
            if (!dialog!!.isShowing)
                dialog!!.apply {
                    show()
                }
        } else if (dialog != null && !show) {
            if (dialog!!.isShowing)
                dialog!!.dismiss()
        }
    }


    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}
