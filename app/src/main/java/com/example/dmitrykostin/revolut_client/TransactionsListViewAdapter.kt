package com.example.dmitrykostin.revolut_client

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dmitrykostin.revolut_client.Revolut.response.Transaction
import java.text.DateFormat
import java.util.*

class TransactionsListViewAdapter(private val transactionsDataset: ArrayList<Transaction>) : RecyclerView.Adapter<TransactionsListViewAdapter.MyViewHolder>() {
    class MyViewHolder(val layout: ViewGroup) : RecyclerView.ViewHolder(layout) {
        val infoView = layout.findViewById<TextView>(R.id.info_text_view)
        val amountView = layout.findViewById<TextView>(R.id.amount)
        val dateView = layout.findViewById<TextView>(R.id.date_view)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TransactionsListViewAdapter.MyViewHolder {
        // create a new view
        val constraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false) as ViewGroup

        return MyViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactionsDataset[position]
        holder.infoView.text = transaction.description
        holder.amountView.text = java.text.NumberFormat.getCurrencyInstance().format(transaction.amount)
        if (transaction.startedDate != null) {
//            val date = Date(transaction.startedDate)
            holder.dateView.text = DateFormat.getDateTimeInstance().format(transaction.startedDate)
        } else {
            holder.dateView.text = ""
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = transactionsDataset.size
}