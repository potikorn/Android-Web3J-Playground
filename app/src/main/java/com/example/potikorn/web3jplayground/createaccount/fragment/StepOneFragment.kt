package com.example.potikorn.web3jplayground.createaccount.fragment

import android.app.WallpaperInfo
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.potikorn.web3jplayground.R
import com.example.potikorn.web3jplayground.WalletSharePref
import com.example.potikorn.web3jplayground.createaccount.adapter.CreateAccountViewPager
import com.example.potikorn.web3jplayground.extensions.inflate
import kotlinx.android.synthetic.main.fragment_step_one.*
import org.web3j.crypto.WalletUtils
import java.io.File
import java.lang.Exception

class StepOneFragment: Fragment() {

    private var callback: CreateAccountViewPager.ViewPagerNavigationInterface? = null
    private val pref: WalletSharePref by lazy { this.context?.let { WalletSharePref(it) }!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_step_one)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNext.setOnClickListener {
            generateNewWallet(etPassword.text?.trim().toString())
        }
    }

    fun setCallBackInterac(callback: CreateAccountViewPager.ViewPagerNavigationInterface?) {
        this.callback = callback
    }

    private fun generateNewWallet(password: String) {
        val etherPath =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/etherfile")

        if (!etherPath.exists())
            etherPath.mkdir()

        if (etherPath.listFiles().isEmpty()) {
            val generateBip39Wallet = WalletUtils.generateBip39Wallet(
                password,
                etherPath
            )
            Log.e(this@StepOneFragment::class.java.simpleName, generateBip39Wallet.filename)
            Log.e(this@StepOneFragment::class.java.simpleName, generateBip39Wallet.mnemonic)
            val fileNameAndMnemonic =
                mapOf<String, String>(generateBip39Wallet.filename to generateBip39Wallet.mnemonic)
            pref.setWalletInfo(fileNameAndMnemonic.toString())
            callback?.onNextPage()
        } else {
            try {
                val wallet = WalletUtils.loadCredentials(password, etherPath.listFiles().first())
                Log.e(this@StepOneFragment::class.java.simpleName, wallet.address)
                Log.e(this@StepOneFragment::class.java.simpleName, wallet.ecKeyPair.privateKey.toString(16))
                Log.e(this@StepOneFragment::class.java.simpleName, wallet.ecKeyPair.publicKey.toString())
                Log.e(this@StepOneFragment::class.java.simpleName, pref.getWalletInfo())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(this.context, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): StepOneFragment {
            return StepOneFragment()
        }
    }

}