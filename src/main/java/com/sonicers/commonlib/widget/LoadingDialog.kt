package com.sonicers.commonlib.widget

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.sonicers.commonlib.R

class LoadingDialog private constructor(internal var activity: Activity) {

    internal var isShowing = false
    internal var loadingView: LoadingView? = null
    internal var alertDialog: AlertDialog? = null
    var context: Activity? = null

    init {
        context = activity
    }

    fun show(): LoadingDialog {
        if (!isShowing && alertDialog != null) {
            isShowing = true
            try {
                alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            alertDialog!!.show()
            if (loadingView != null)
                loadingView!!.start()
        }
        return this
    }

    fun hide(): LoadingDialog {
        isShowing = false
        if (loadingView != null)
            loadingView!!.stop()
        if (alertDialog != null)
            alertDialog!!.dismiss()
        return this
    }

    companion object {

        operator fun get(activity: Activity): LoadingDialog {
            val dialog = LoadingDialog(activity)
            val builder = AlertDialog.Builder(dialog.activity)
            val v = LayoutInflater.from(dialog.activity).inflate(R.layout.layout_loading_view, null)
            dialog.loadingView = v.findViewById(R.id.loading_view)
            builder.setView(v)
            builder.setCancelable(false)
            dialog.alertDialog = builder.create()
            return dialog
        }
    }
}