package br.com.concrete.tentacle.features.library.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.filter.FilterItem
import br.com.concrete.tentacle.data.models.library.filter.SubItem
import br.com.concrete.tentacle.extensions.loadImage
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.fragment_filter.filterButtonView
import kotlinx.android.synthetic.main.fragment_filter.filterContent
import kotlinx.android.synthetic.main.item_filter_checkbox.view.subitemFilterCheckBox
import kotlinx.android.synthetic.main.item_filter_checkbox.view.subitemFilterTextView
import kotlinx.android.synthetic.main.item_filter_title.view.itemFilterImageView
import kotlinx.android.synthetic.main.item_filter_title.view.itemFilterTextView
import org.koin.android.viewmodel.ext.android.viewModel

const val TAG = "FilterDialogFragment"
private const val TYPE_DRAWABLE = "drawable"

class FilterDialogFragment : DialogFragment() {

    private val viewModelFilter: FilterViewModel by viewModel()

    private lateinit var filterList: List<FilterItem>
    private lateinit var callback: OnFilterListener

    private val filtersSelected = ArrayList<SubItem>()

    companion object {
        fun showDialog(fragment: Fragment?, elements: ArrayList<SubItem>) {
            fragment?.fragmentManager?.let {
                val dialog = FilterDialogFragment()
                val args = Bundle()
                args.putParcelableArrayList("filter_items", elements)

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
        initObservers()

        initListeners()
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
        arguments?.getParcelableArrayList<SubItem>("filter_items")?.let {
            filtersSelected.addAll(it)
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

            item.subitems.forEach { subitem ->
                val subItemView = LayoutInflater
                    .from(activity)
                    .inflate(R.layout.item_filter_checkbox, filterContent, false)

                subItemView.subitemFilterTextView.text = subitem.name

                // Populate view
                filtersSelected.firstOrNull { it.key == subitem.key }?.let {
                    subitem.isChecked = it.isChecked
                }
                subItemView.subitemFilterCheckBox.isChecked = subitem.isChecked

                subItemView.setOnClickListener {
                    subitem.isChecked = !subitem.isChecked
                    subItemView.subitemFilterCheckBox.isChecked = subitem.isChecked
                }

                filterContent.addView(subItemView)
            }
        }
    }

    private fun initListeners() {
        filterButtonView.setOnClickListener {
            filtersSelected.clear()
            filterList.forEach { item ->
                filtersSelected.addAll(item.subitems.filter { subItem ->
                    subItem.isChecked
                })
            }
            callback.onFilterListener(filtersSelected)
            dismiss()
        }
    }

    interface OnFilterListener {
        fun onFilterListener(filters: List<SubItem>)
    }
}
