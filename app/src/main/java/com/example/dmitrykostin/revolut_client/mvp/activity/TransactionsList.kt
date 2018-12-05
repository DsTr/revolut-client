package com.example.dmitrykostin.revolut_client.mvp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction

import kotlinx.android.synthetic.main.activity_transactions_list.*
import kotlinx.android.synthetic.main.content_transactions_list.*
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import com.example.dmitrykostin.revolut_client.R
import com.example.dmitrykostin.revolut_client.TransactionsListViewAdapter
import com.example.dmitrykostin.revolut_client.mvp.model.RevolutTransactionsListModel
import com.example.dmitrykostin.revolut_client.mvp.representer.RevolutTransactionsListRepresenter
import com.example.dmitrykostin.revolut_client.util.SharedPreferencesCredentialsKeeper
import android.view.Menu
import android.view.MenuItem
import com.example.dmitrykostin.revolut_client.mvp.representer.TransactionListRepresenterInterface

class TransactionsList : BaseActivity(), TransactionsListActivityInterface {
    companion object {
        val LOGIN_REQUEST = 1;
    }

    private lateinit var viewAdapter: TransactionsListViewAdapter
    private val transactionListDataset: ArrayList<Transaction> = ArrayList(0)

    private lateinit var transactionsListRepresenter: TransactionListRepresenterInterface;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        prepareTransactionListView()
        prepareRepresenter()
    }

    private fun prepareTransactionListView() {
        viewAdapter = TransactionsListViewAdapter(transactionListDataset)
        transaction_list.layoutManager = LinearLayoutManager(this)
        transaction_list.adapter = viewAdapter
        transaction_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        transaction_list.setHasFixedSize(true)
    }

    private fun prepareRepresenter() {
        if (null != lastCustomNonConfigurationInstance) {
            transactionsListRepresenter = lastCustomNonConfigurationInstance as TransactionListRepresenterInterface
        } else {
            transactionsListRepresenter = createConcreteTransactionListRepresenter()
        }
        transactionsListRepresenter.attachView(this)
        transactionsListRepresenter.firstLoad()
    }

    private fun createConcreteTransactionListRepresenter() : TransactionListRepresenterInterface {
        val sharedPreferencesCredentialsKeeper = SharedPreferencesCredentialsKeeper(getSharedPreferences())
        return RevolutTransactionsListRepresenter(sharedPreferencesCredentialsKeeper, RevolutTransactionsListModel())
    }

    override fun gotNewDatasetToDisplay(newTransactionsList: List<Transaction>) {
        viewAdapter.transactionsDataset = newTransactionsList
        viewAdapter.notifyDataSetChanged()
    }

    override fun doNewUserCredentialsRequest(reasonToRequest: TransactionListRepresenterInterface.ReasonToLoginUser) {
        if (reasonToRequest == TransactionListRepresenterInterface.ReasonToLoginUser.EXPIRED_TOKEN) {
            Toast.makeText(baseContext, "Wrong credentials, authorize again", Toast.LENGTH_SHORT).show();
        }
        transactionListDataset.clear()
        viewAdapter.notifyDataSetChanged()
        startLoginActivity()
    }

    override fun gotNetworkFailure() {
        Toast.makeText(baseContext, "Network failure, try again later", Toast.LENGTH_LONG).show();
    }

    override fun doSwitchLoaderState(loadingState: Boolean) {
        viewAdapter.loadingState = loadingState
        viewAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        return if (id == R.id.action_logout) {
            transactionsListRepresenter.logOutUser()
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
                val userId = data?.getStringExtra(LoginActivity.INTENT_KEY_USER_ID);
                val token = data?.getStringExtra(LoginActivity.INTENT_KEY_TOKEN);

                if (null != token && userId != null) {
                    transactionsListRepresenter.newCredentialsRetrievedFromUser(userId, token)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!this.isChangingConfigurations) {
            transactionsListRepresenter.destroy()
        }
        transactionsListRepresenter.detachView()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return transactionsListRepresenter
    }
}
