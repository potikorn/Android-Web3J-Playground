package com.example.potikorn.web3jplayground

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.potikorn.web3jplayground.createaccount.adapter.CreateAccountViewPager
import kotlinx.android.synthetic.main.activity_new_account.*
import org.json.JSONObject
import org.web3j.crypto.Bip39Wallet
import org.web3j.crypto.CipherException
import org.web3j.crypto.Keys
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File
import java.lang.Exception
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class NewAccountActivity : AppCompatActivity(),
    CreateAccountViewPager.ViewPagerNavigationInterface {

    private val web3j: Web3j by lazy { Web3jFactory.build(HttpService("https://rinkeby.infura.io/v3/0281668706ef497bb4b6462de92a2128")) }
    private val pref: WalletSharePref by lazy { WalletSharePref(this) }
    private val createAccountViewPager: CreateAccountViewPager by lazy { CreateAccountViewPager(this.supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        btnGenerateNewAccount.setOnClickListener {
            checkPermission(listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
                onPermissionAllowed = {
                    generateNewWallet("123456")
//                    generateNewWallet("groceryjaguarguessglobenobleillbreezeangryprooffocusnosecrawl")
                },
                onPermissionDenied = {
                    Toast.makeText(this, "Error permission!", Toast.LENGTH_SHORT).show()
                })
        }

        vpCreateAccount.apply {
            adapter = createAccountViewPager.also { it.setCallbackNavigator(this@NewAccountActivity) }
        }
    }

    override fun onNextPage() {
        vpCreateAccount.currentItem = vpCreateAccount.currentItem.plus(1)
    }

    override fun onPrevious() {
        vpCreateAccount.currentItem = vpCreateAccount.currentItem.minus(1)
    }

    private fun generateNewWallet(password: String) {
        val etherPath =
            File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path + "/etherfile")

        if (!etherPath.exists())
            etherPath.mkdir()

        if (etherPath.listFiles().isEmpty()) {
//            val wallet = WalletUtils.generateFullNewWalletFile(
//                password,
//                etherPath
//            )
//            Log.e(this@NewAccountActivity::class.java.simpleName, wallet)

            val generateBip39Wallet = WalletUtils.generateBip39Wallet(
                password,
                etherPath
            )
            Log.e(this@NewAccountActivity::class.java.simpleName, generateBip39Wallet.filename)
            Log.e(this@NewAccountActivity::class.java.simpleName, generateBip39Wallet.mnemonic)
            val fileNameAndMnemonic =
                mapOf<String, String>(generateBip39Wallet.filename to generateBip39Wallet.mnemonic)
            pref.setWalletInfo(fileNameAndMnemonic.toString())
        } else {
//            try {
//                val wallet = WalletUtils.loadCredentials(password, etherPath.listFiles().first())
//                Log.e(this@NewAccountActivity::class.java.simpleName, wallet.address)
//                Log.e(this@NewAccountActivity::class.java.simpleName, wallet.ecKeyPair.privateKey.toString(16))
//                Log.e(this@NewAccountActivity::class.java.simpleName, wallet.ecKeyPair.publicKey.toString())
//                Log.e(this@NewAccountActivity::class.java.simpleName, pref.getWalletInfo())
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
//            }
            try {
                val walletBip = WalletUtils.loadBip39Credentials(
                    password,
                    "grocery jaguar guess globe noble ill breeze angry proof focus nose crawl"
                )
                Log.e(this@NewAccountActivity::class.java.simpleName, walletBip.address)
                Log.e(
                    this@NewAccountActivity::class.java.simpleName,
                    walletBip.ecKeyPair.privateKey.toString(16)
                )
                Log.e(
                    this@NewAccountActivity::class.java.simpleName,
                    walletBip.ecKeyPair.publicKey.toString()
                )
                Log.e(this@NewAccountActivity::class.java.simpleName, pref.getWalletInfo())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun process(seed: String): JSONObject {
        val processJson = JSONObject()

        try {
            val ecKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec = ecKeyPair.privateKey

            val sPrivateKeyInHex = privateKeyInDec.toString(16)

            val aWallet = Wallet.createLight(seed, ecKeyPair)

            val sAddress = aWallet.address

            Log.e(NewAccountActivity::class.java.simpleName, "ECKEYPAIR: " + ecKeyPair.toString())
            Log.e(NewAccountActivity::class.java.simpleName, "PublicKey: " + ecKeyPair.publicKey)
            Log.e(NewAccountActivity::class.java.simpleName, "DEC PrivateKey: $privateKeyInDec")
            Log.e(NewAccountActivity::class.java.simpleName, "HEX PrivateKey: $sPrivateKeyInHex")
            Log.e(NewAccountActivity::class.java.simpleName, "sAddress: $sAddress")

            processJson.put("address", "0x$sAddress")
            processJson.put("privatekeyd", privateKeyInDec)
            processJson.put("privatekey", sPrivateKeyInHex)
            processJson.put("publickey", ecKeyPair.publicKey)

//            generateNewWallet("")
        } catch (e: CipherException) {
            e.printStackTrace()
            Log.e(NewAccountActivity::class.java.simpleName, e.message)
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
            Log.e(NewAccountActivity::class.java.simpleName, e.message)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(NewAccountActivity::class.java.simpleName, e.message)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
            Log.e(NewAccountActivity::class.java.simpleName, e.message)
        }

        return processJson
    }
}