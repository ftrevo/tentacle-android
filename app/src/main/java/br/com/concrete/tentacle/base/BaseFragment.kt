package br.com.concrete.tentacle.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.HTTP_UPGRADE_REQUIRED
import br.com.concrete.tentacle.utils.Utils
import org.jetbrains.annotations.TestOnly

abstract class BaseFragment : Fragment() {

    protected var callback: CallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CallBack) callback = context
    }

    protected fun showError(errors: ErrorResponse?, title: String = "Erro") {
        var positiveButton = getString(R.string.ok)
        var negativeButton: String? = null

        if (errors != null) {
            context?.let { ctx ->
                errors.messageInt.map { error ->
                    errors.message.add(ctx.getString(error))
                }
            }
            val ers = errors.toString()
            var positive = DialogInterface.OnClickListener { _, _ -> }

            if (errors.statusCode == HTTP_UPGRADE_REQUIRED) {
                positiveButton = getString(R.string.update)
                negativeButton = getString(R.string.not_delete)
                positive = DialogInterface.OnClickListener { _, _ ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                errors.updateUrl
                            )
                        )
                    )
                }
            }

            activity?.let {
                DialogUtils.showDialog(
                    context = it,
                    title = if (title == "Erro") getString(R.string.something_happened) else title,
                    message = ers,
                    positiveText = positiveButton,
                    positiveListener = positive,
                    negativeText = negativeButton,
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