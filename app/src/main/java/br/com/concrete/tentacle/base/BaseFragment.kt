package br.com.concrete.tentacle.base

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.utils.DialogUtils
import org.jetbrains.annotations.TestOnly

abstract class BaseFragment : Fragment() {

    protected var callback: CallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CallBack) callback = context
    }

    protected fun showError(errors: ErrorResponse?, title: String = "Erro") {
        if (errors != null) {
            context?.let { ctx ->
                errors.messageInt.map { error ->
                    errors.message.add(ctx.getString(error))
                }
            }
            val ers = errors.toString()

            activity?.let {
                DialogUtils.showDialog(
                    context = it,
                    title = if (title == "Erro") getString(R.string.something_happened) else title,
                    message = ers,
                    positiveText = getString(R.string.ok),
                    positiveListener = DialogInterface.OnClickListener { _, _ -> },
                    contentView = R.layout.custom_dialog_error
                )
            }
        }
    }

    abstract fun getToolbarTitle(): Int

    @TestOnly
    protected fun showMessageForTest(resMsg: Int) {
        context?.callSnackbar(view!!, getString(resMsg))
    }
}