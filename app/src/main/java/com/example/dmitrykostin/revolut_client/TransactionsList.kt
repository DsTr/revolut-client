package com.example.dmitrykostin.revolut_client

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.dmitrykostin.revolut_client.Revolut.Api

import kotlinx.android.synthetic.main.activity_transactions_list.*
import kotlinx.android.synthetic.main.content_transactions_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransactionsList : BaseActivityWithCoroutineScope() {
    companion object {
        val LOGIN_REQUEST = 1;
    }

    private val api by lazy {
        Api()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var authorizationHolder: AuthorizationHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        val dataSet = arrayOf("a", "b", "c", "gfgfg", "ggr3434gger", "fdfdf343");
        viewManager = LinearLayoutManager(this)
        viewAdapter = TransactionsListViewAdapter(dataSet)

        transaction_list.layoutManager = viewManager
        transaction_list.adapter = viewAdapter
        transaction_list.setHasFixedSize(true)

        val sharedPreferences = getSharedPreferences()
        val userId = sharedPreferences.getString(getString(R.string.saved_user_id_key), null)
        val accessToken = sharedPreferences.getString(getString(R.string.saved_token_key), null)

        if (userId != null && accessToken != null) {
            authorizationHolder = AuthorizationHolder(userId, accessToken)
            loadTransactions()
        }
        if (authorizationHolder == null) {
            startLoginActivity()
        }
    }

    private fun loadTransactions() = launch {
        if (authorizationHolder != null) {
            val (transactionList, err) = async(Dispatchers.Default) {
                api.getTransactions(authorizationHolder!!)
            }.await()
            transactionList
        }
    }

    private fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val token = data?.getStringExtra(LoginActivity.INTENT_KEY_TOKEN);
                val userId = data?.getStringExtra(LoginActivity.INTENT_KEY_USER_ID);

                if (null != token && userId != null) {
                    val sharedPref = getSharedPreferences()
                    with(sharedPref.edit()) {
                        putString(getString(R.string.saved_user_id_key), userId)
                        putString(getString(R.string.saved_token_key), token)
                        commit()
                    }
                    this.authorizationHolder = AuthorizationHolder(userId, token)
                }
            }
        }
    }
}
