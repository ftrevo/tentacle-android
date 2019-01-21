package br.com.concrete.tentacle.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse

abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(activity is BaseActivity){
            (activity as BaseActivity).setToolbarTitle(getToolbarTitle())
        }
    }

    protected fun showError(errors: ErrorResponse?) {
        if (errors != null) {
            context?.let { ctx ->
                errors.messageInt.map { error ->
                    errors.message.add(ctx.getString(error))
                }
            }
            val ers = errors.toString()
            val alertDialog: AlertDialog? = activity?.let { fragment ->
                val builder = AlertDialog.Builder(fragment)
                builder.setTitle(R.string.error_dialog_title)
                builder.setMessage(ers)
                builder.apply {
                    setPositiveButton(
                        R.string.ok
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                }

                builder.create()
            }
            alertDialog?.show()
        }
    }

    abstract fun getToolbarTitle(): Int
}