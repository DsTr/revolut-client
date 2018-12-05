package com.example.dmitrykostin.revolut_client

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import java.text.DateFormat
import java.util.*

class TransactionsListViewAdapter(var transactionsDataset: List<Transaction>) : RecyclerView.Adapter<TransactionsListViewAdapter.BasicLayoutViewHolder>() {
    companion object {
        val VIEW_TYPE_TRANSACTION = 1
        val VIEW_TYPE_LOADER = 2
    }

    var loadingState = false

    // Base ViewHodel class and for loader
    abstract class BasicLayoutViewHolder(layout: ViewGroup) : RecyclerView.ViewHolder(layout) {
        open fun updateData(transaction: Transaction?, isLoadingState: Boolean) {}
    }

    open class LoaderViewHolder(layout: ViewGroup) : BasicLayoutViewHolder(layout) {
        val loaderView = layout.findViewById<View>(R.id.loader_list_item_loader)

        override fun updateData(transaction: Transaction?, isLoadingState: Boolean) {
            loaderView.visibility = if (isLoadingState) View.VISIBLE else View.GONE
        }
    }

    class TransactionViewHolder(layout: ViewGroup) : BasicLayoutViewHolder(layout) {
        val infoView = layout.findViewById<TextView>(R.id.info_text_view)
        val amountView = layout.findViewById<TextView>(R.id.amount)
        val dateView = layout.findViewById<TextView>(R.id.date_view)
        val additionalInfoView = layout.findViewById<TextView>(R.id.additional_info)

        override fun updateData(transaction: Transaction?, isLoadingState: Boolean) {
            infoView.text = transaction?.description
            amountView.text = java.text.NumberFormat.getCurrencyInstance().format(transaction?.amount)
            if (transaction?.startedDate != null) {
                dateView.text = DateFormat.getDateTimeInstance().format(transaction.startedDate)
            } else {
                dateView.text = ""
            }
            val additionalInfoBuilder = StringBuilder()
            val mcc = transaction?.merchant?.mcc
            if (null != mcc) {
                additionalInfoBuilder.append("mcc: ")
                additionalInfoBuilder.append(mcc)
            }
            val additionalInfoText = additionalInfoBuilder.toString()
            if (additionalInfoText.isEmpty()) {
                additionalInfoView.visibility = View.GONE
            } else {
                additionalInfoView.text = additionalInfoText
                additionalInfoView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TransactionsListViewAdapter.BasicLayoutViewHolder {
        if (viewType == VIEW_TYPE_TRANSACTION) {
            val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_item, parent, false) as ViewGroup
            return TransactionViewHolder(layout)
        }
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.loader_list_item, parent, false) as ViewGroup
        return LoaderViewHolder(layout)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < transactionsDataset.size) VIEW_TYPE_TRANSACTION else VIEW_TYPE_LOADER
    }

    override fun onBindViewHolder(holder: BasicLayoutViewHolder, position: Int) {
        holder.updateData(transactionsDataset.getOrNull(position), loadingState)
    }

    override fun getItemCount() = transactionsDataset.size + 1
}