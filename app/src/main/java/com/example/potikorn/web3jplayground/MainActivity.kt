package com.example.potikorn.web3jplayground

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val web3j: Web3j by lazy {
        Web3jFactory.build(HttpService("https://rinkeby.infura.io/v3/0281668706ef497bb4b6462de92a2128"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOk.setOnClickListener {
            when (etWalletAddress.text?.trim()?.isNotEmpty()) {
                true -> {
                    getWalletTransaction(etWalletAddress.text.toString())
                }
                else -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Please insert your wallet address",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        fabNewAccount.setOnClickListener {
            startActivity(Intent(this, NewAccountActivity::class.java))
        }
        web3j.ethAccounts().observable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e(this@MainActivity::class.java.simpleName, it.accounts.toString())
            }, {
                Log.e(this@MainActivity::class.java.simpleName, it.message)
            })
    }

    @SuppressLint("SetTextI18n")
    private fun getWalletTransaction(address: String) {
        etWalletAddress.setText("")
//        web3j.ethGetBalance(
//            address,
//            DefaultBlockParameterName.LATEST
//        )
//            .observable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ transaction ->
//                Log.e(
//                    this@MainActivity::class.java.simpleName,
//                    transaction.balance.toString()
//                )
//                Log.e(
//                    this@MainActivity::class.java.simpleName,
//                    transaction.result.toString()
//                )
//                tvDetail.text = "Balance amount: ${transaction.balance}"
//            }, { ex ->
//                ex.printStackTrace()
//                Log.e(this@MainActivity::class.java.simpleName, ex.message)
//            })
//        web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
//            .observable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ transaction ->
//                Log.e(
//                    this@MainActivity::class.java.simpleName,
//                    transaction.transactionCount.toString()
//                )
//            }, { ex ->
//                Log.e(this@MainActivity::class.java.simpleName, ex.message)
//            })
        val subscription =
            web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.EARLIEST)
                .filter { tx -> tx.from == address || tx.to == address }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tx ->
                    Log.e(this@MainActivity::class.java.simpleName, tx.valueRaw)
                }, {
                    Log.e(this@MainActivity::class.java.simpleName, it.message)
                })
        Thread.sleep(10000)
        subscription.unsubscribe()
    }
}
