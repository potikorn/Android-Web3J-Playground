package com.example.potikorn.web3jplayground.createaccount.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.potikorn.web3jplayground.createaccount.fragment.StepOneFragment

class CreateAccountViewPager(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private var callback: ViewPagerNavigationInterface? = null

    override fun getItem(p0: Int): Fragment = when(p0) {
        0 -> StepOneFragment.newInstance().also { it.setCallBackInterac(callback) }
        else -> StepOneFragment.newInstance()
    }

    override fun getCount(): Int = 2

    interface ViewPagerNavigationInterface {
        fun onNextPage()
        fun onPrevious()
    }

    fun setCallbackNavigator(callback: ViewPagerNavigationInterface) {
        this.callback = callback
    }
}