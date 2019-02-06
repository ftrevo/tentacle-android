package br.com.concrete.tentacle.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.extensions.callSnackbar
import org.jetbrains.annotations.TestOnly

abstract class BaseFragment : Fragment() {

    protected var callback: CallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CallBack) callback = context
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

    @TestOnly
    protected fun showMessageForTest(resMsg: Int) {
        context?.callSnackbar(view!!, getString(resMsg))
    }
}