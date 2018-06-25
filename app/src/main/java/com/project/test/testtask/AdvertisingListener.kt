package com.project.test.testtask

import android.view.MotionEvent
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

/*
Слушатель, показывающий рекламу раз в CLICK_FOR_SHOW нажатий
 */
class AdvertisingListener(private val adView: InterstitialAd) {
    //Количество текущих кликов
    private var countClick = 0
    //Количество кликов для показа очередного баннера
    private val CLICK_FOR_SHOW = 4
    fun onTouch(v: View?, event: MotionEvent?): Boolean =
            when (event?.action) {
                //Нажатие на view
                MotionEvent.ACTION_DOWN -> {
                    if (((++countClick).rem(CLICK_FOR_SHOW)) == 0) {
                        //Загрузка рекламы
                        adView.loadAd(AdRequest.Builder().build())
                    }
                    true
                }
                else -> false
            }
}