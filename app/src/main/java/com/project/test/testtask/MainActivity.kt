package com.project.test.testtask

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var imagesViewModel: ImagesViewModel
    private var imagesArray: Array<Bitmap>? = null
    private lateinit var swipeImageListener: SwipeImageListener
    private lateinit var advertisingListener: AdvertisingListener
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagesViewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)

        swipeImageListener = SwipeImageListener(firstImageView, secondImageView, imagesArray, imagesViewModel.imageIndex)
        imagesViewModel.imagesListLiveData.observe(this, Observer<Array<Bitmap>> { newValue ->
            imagesArray = newValue
            swipeImageListener.imagesArray = imagesArray
        })



        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.banner_ad_unit_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        advertisingListener = AdvertisingListener(mInterstitialAd)
        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                mInterstitialAd.show()
            }
        }
        firstImageView.setOnTouchListener { v, event ->
            (swipeImageListener.onTouch(v, event)).or(advertisingListener.onTouch(v, event))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId){
        R.id.galleryMenuItem->{
            val intent = Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivity(intent)
            true
        }
        R.id.googlePlayMenuItem->{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=${getString(R.string.gallery_market_id)}")
            startActivity(intent)
            true
        }
        else-> super.onOptionsItemSelected(item)
    }
}
