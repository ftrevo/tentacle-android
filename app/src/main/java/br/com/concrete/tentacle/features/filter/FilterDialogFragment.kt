package br.com.concrete.tentacle.features.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.filter.FilterItem
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.extensions.loadImage
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.fragment_filter.filterButtonView
import kotlinx.android.synthetic.main.fragment_filter.filterClearButtonView
import kotlinx.android.synthetic.main.fragment_filter.filterCloseButton
import kotlinx.android.synthetic.main.fragment_filter.filterContent
import kotlinx.android.synthetic.main.item_filter_checkbox.view.subItemFilter
import kotlinx.android.synthetic.main.item_filter_checkbox.view.subitemFilterCheckBox
import kotlinx.android.synthetic.main.item_filter_checkbox.view.subitemFilterTextView
import kotlinx.android.synthetic.main.item_filter_title.view.itemFilterImageView
import kotlinx.android.synthetic.main.item_filter_title.view.itemFilterTextView
import org.koin.android.viewmodel.ext.android.viewModel

const val TAG = "FilterDialogFragment"
private const val TYPE_DRAWABLE = "drawable"
private const val FILTER_BUNDLE_ARGS = "filter_items"

class FilterDialogFragment : DialogFragment() {

    private val viewModelFilter: FilterViewModel by viewModel()

    private lateinit var filterList: List<FilterItem>
    private lateinit var callback: OnFilterListener

    private val filtersSelected = ArrayList<SubItem>()

    companion object {
        fun showDialog(fragment: Fragment?, elements: ArrayList<SubItem>, filterPath: String) {
            fragment?.fragmentManager?.let {
                val dialog = FilterDialogFragment()
                val args = Bundle()
                args.putParcelableArrayList(FILTER_BUNDLE_ARGS, elements)
                args.putString("FILTER_PATH", filterPath)

                dialog.arguments = args
                dialog.setTargetFragment(fragment, 0)
                dialog.show(it, TAG)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            callback = targetFragment as OnFilterListener
        } catch (e: TypeCastException) {
            LogWrapper.print(e)
        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.StyleDialogFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString("FILTER_PATH")?.let {filterPath ->
            initObservers()
            initListeners()
            viewModelFilter.getFilterItems(filterPath)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(null)
    }

    private fun initObservers() {
        viewModelFilter.viewStateFilters.observe(this, Observer {
            when (it.status) {
                ViewStateModel.Status.SUCCESS -> {
                    filterList = it.model!!
                    createViewsOnSuccess()
                }
                ViewStateModel.Status.ERROR -> {
                    dismiss()
                }
            }
        })
        lifecycle.addObserver(viewModelFilter)
    }

    private fun createViewsOnSuccess() {
        arguments?.getParcelableArrayList<SubItem>(FILTER_BUNDLE_ARGS)?.let {
            filtersSelected.addAll(it)
            setClearButtonVisibility()
        }

        filterList.forEach { item ->
            val itemView = LayoutInflater
                .from(activity)
                .inflate(R.layout.item_filter_title, filterContent, false)

            itemView.itemFilterTextView.text = item.title

            activity?.let {
                val drawableRes = resources.getIdentifier(item.icon, TYPE_DRAWABLE, it.packageName)
                itemView.itemFilterImageView
                    .loadImage(drawableRes)
            }
            filterContent.addView(itemView)

            item.subItems.forEach { subitem ->
                subitem.queryType = item.title

                val subItemView = LayoutInflater
                    .from(activity)
                    .inflate(R.layout.item_filter_checkbox, filterContent, false)

                subItemView.subitemFilterTextView.text = subitem.name

                // Populate view
                filtersSelected.firstOrNull { it.queryParameter == subitem.queryParameter }?.let {
                    subitem.isChecked = it.isChecked
                }
                subItemView.subitemFilterCheckBox.isChecked = subitem.isChecked

                subItemView.subItemFilter.setOnClickListener {
                    subitem.isChecked = !subitem.isChecked
                    subItemView.subitemFilterCheckBox.isChecked = subitem.isChecked

                    onCheckItem(subitem)
                }

                filterContent.addView(subItemView)
            }
        }
    }

    private fun initListeners() {
        filterButtonView.setOnClickListener {
            onDismiss()
        }

        filterClearButtonView.setOnClickListener {
            filtersSelected.clear()
            onDismiss()
        }

        filterCloseButton.setOnClickListener {
            dismiss()
        }
    }

    private fun onDismiss() {
        callback.onFilterListener(filtersSelected)
        dismiss()
    }

    private fun onCheckItem(subItem: SubItem) {
        if (subItem.isChecked) {
            filtersSelected.add(subItem)
        } else {
            val preSelectedItem = filtersSelected.first { listItem ->
                listItem.queryParameter == subItem.queryParameter
            }
            filtersSelected.remove(preSelectedItem)
        }

        setClearButtonVisibility()
    }

    private fun setClearButtonVisibility() {
        filterClearButtonView.visibility = if (filtersSelected.isNotEmpty()) VISIBLE else GONE
    }

    interface OnFilterListener {
        fun onFilterListener(filters: List<SubItem>)
    }
}
