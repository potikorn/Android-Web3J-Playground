package com.example.potikorn.web3jplayground

import android.content.Context
import android.content.SharedPreferences

interface Wallet {

    fun setWalletInfo(walletInfo: String)
    fun getWalletInfo(): String?
}

class WalletSharePref(context: Context): Wallet {

    val pref: SharedPreferences = context.getSharedPreferences(context.packageName + "walletinfo", 0)

    private val PREF_WALLET_INFO = "PREF_WALLET_INFO"

    override fun setWalletInfo(walletInfo: String) {
        pref.edit().putString(PREF_WALLET_INFO, walletInfo).apply()
    }

    override fun getWalletInfo(): String? = pref.getString(PREF_WALLET_INFO, "")
}