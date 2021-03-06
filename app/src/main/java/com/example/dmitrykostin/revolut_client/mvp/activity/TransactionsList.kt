package com.example.dmitrykostin.revolut_client.mvp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dmitrykostin.revolut_client.R
import com.example.dmitrykostin.revolut_client.credentials.SharedPreferencesCredentialsStorage
import com.example.dmitrykostin.revolut_client.mvp.adapters.TransactionsListViewAdapter
import com.example.dmitrykostin.revolut_client.mvp.model.RevolutTransactionsListModel
import com.example.dmitrykostin.revolut_client.mvp.presenter.RevolutTransactionsListPresenter
import com.example.dmitrykostin.revolut_client.mvp.presenter.TransactionListPresenter
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import kotlinx.android.synthetic.main.activity_transactions_list.*
import kotlinx.android.synthetic.main.content_transactions_list.*

class TransactionsList : BaseActivity(), TransactionsListView {
    private lateinit var viewAdapter: TransactionsListViewAdapter
    private val transactionListDataset: ArrayList<Transaction> = ArrayList(0)

    private lateinit var transactionsListPresenter: TransactionListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        prepareTransactionListView()
        preparePresenter()
    }

    private fun prepareTransactionListView() {
        viewAdapter = TransactionsListViewAdapter(transactionListDataset)
        viewAdapter.loadMoreButtonClickCb = { transactionsListPresenter.loadMoreClick() }
        transaction_list.layoutManager = LinearLayoutManager(this)
        transaction_list.adapter = viewAdapter
        transaction_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        transaction_list.setHasFixedSize(true)
    }

    private fun preparePresenter() {
        transactionsListPresenter = if (null != lastCustomNonConfigurationInstance) {
            lastCustomNonConfigurationInstance as TransactionListPresenter
        } else {
            createConcreteTransactionListRepresenter()
        }
        transactionsListPresenter.attachView(this)
        transactionsListPresenter.viewLoaded()
    }

    private fun createConcreteTransactionListRepresenter(): TransactionListPresenter {
        val sharedPreferencesCredentialsKeeper = SharedPreferencesCredentialsStorage(getSharedPreferences())
        return RevolutTransactionsListPresenter(sharedPreferencesCredentialsKeeper, RevolutTransactionsListModel())
    }

    override fun displayNewDataset(newTransactionsList: List<Transaction>) {
        viewAdapter.transactionsDataset = newTransactionsList
        viewAdapter.notifyDataSetChanged()
    }

    override fun openAuthorizationDialog(reasonToRequest: TransactionListPresenter.ReasonToLoginUser) {
        if (reasonToRequest == TransactionListPresenter.ReasonToLoginUser.EXPIRED_TOKEN) {
            Toast.makeText(baseContext, "Wrong credentials, authorize again", Toast.LENGTH_SHORT).show()
        }
        transactionListDataset.clear()
        viewAdapter.notifyDataSetChanged()
        startLoginActivity()
    }

    override fun displayNetworkFailure() {
        Toast.makeText(baseContext, "Network failure, try again later", Toast.LENGTH_LONG).show()
    }

    override fun doSwitchLoaderState(loadingState: Boolean) {
        viewAdapter.loadingState = loadingState
        viewAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_logout) {
            transactionsListPresenter.logOutClick()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(
            intent,
            LOGIN_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val userId = data?.getStringExtra(LoginActivity.INTENT_KEY_USER_ID)
                val token = data?.getStringExtra(LoginActivity.INTENT_KEY_TOKEN)

                if (null != token && userId != null) {
                    transactionsListPresenter.newCredentialsRetrievedFromUser(userId, token)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!this.isChangingConfigurations) {
            transactionsListPresenter.destroy()
        }
        transactionsListPresenter.detachView()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return transactionsListPresenter
    }

    companion object {
        const val LOGIN_REQUEST = 1
    }
}
