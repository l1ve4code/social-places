package com.example.redmad.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.example.redmad.MainActivity
import com.example.redmad.R

class LoadingDialog(mainActivity: FragmentActivity) {

    private val activity: Activity = mainActivity
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog(){
        val builder = AlertDialog.Builder(activity)

        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_loader, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}