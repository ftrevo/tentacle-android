package br.com.concrete.tentacle.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.concrete.tentacle.R

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

        if (contentView == null) {
            builder.setTitle(title)
        }

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
                val inflater = ((context) as AppCompatActivity).layoutInflater
                val view = inflater.inflate(contentView as Int, null)

                val titleMessage = view.findViewById<TextView>(R.id.titleMessage)
                titleMessage.text = title

                val messageBody = view.findViewById<TextView>(R.id.message)
                messageBody.text = message

                dialog.setView(view)
            }
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val color = ContextCompat.getColor(context, R.color.colorAccent)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color)

        return dialog
    }
}