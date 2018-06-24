package com.project.test.testtask

import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

class AdvertisingListener(private val adView: InterstitialAd)  {
    private var countClick = 0
    private val CLICK_FOR_SHOW = 4
    fun onTouch(v: View?, event: MotionEvent?): Boolean =
            when(event?.action){
                MotionEvent.ACTION_DOWN ->
                {
                    Log.wtf("kikong","mod ${((++countClick).rem(CLICK_FOR_SHOW))}")
                    if(((countClick).rem(CLICK_FOR_SHOW)) == 0){

                        Log.wtf("kikong","countClick $countClick")
                        adView.loadAd(AdRequest.Builder().build())
                    }
                    true
                }
                else -> {
                    false
                }
            }
}