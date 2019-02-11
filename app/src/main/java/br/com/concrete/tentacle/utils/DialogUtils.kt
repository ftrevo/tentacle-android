package br.com.concrete.tentacle.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

object DialogUtils {
    fun showDialog(
        context: Context,
        title: String? = null,
        message: String?,
        positiveText: String,
        positiveListener: DialogInterface.OnClickListener? = null,
        negativeText: String? = null,
        negativeListener: DialogInterface.OnClickListener? = null,
        dismissListener: DialogInterface.OnDismissListener? = null,
        contentView: Any? = null,
        neutralText: String? = null,
        neutralListener: DialogInterface.OnClickListener? = null
    ): AlertDialog {

        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)

        if (contentView == null) {
            builder.setMessage(message)
        }

        builder.setPositiveButton(positiveText, positiveListener)

        negativeText?.let {
            builder.setNegativeButton(it, negativeListener)
        }

        neutralText?.let {
            builder.setNeutralButton(neutralText, neutralListener)
        }

        dismissListener?.let {
            builder.setOnDismissListener(it)
        }

        val dialog: AlertDialog = builder.create()

        if (contentView != null) {

            if (contentView is View) {
                dialog.setContentView(contentView)
            } else {
                dialog.setContentView(contentView as Int)
            }
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
//        dialog.applyUnidasStyle()

        return dialog
    }
}