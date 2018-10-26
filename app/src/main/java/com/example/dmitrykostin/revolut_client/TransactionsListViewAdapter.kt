package com.example.dmitrykostin.revolut_client

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class TransactionsListViewAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<TransactionsListViewAdapter.MyViewHolder>() {
    class MyViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
        val infoView = layout.findViewById<TextView>(R.id.info_text_view)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TransactionsListViewAdapter.MyViewHolder {
        // create a new view
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false) as LinearLayout

        return MyViewHolder(linearLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.infoView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}