package com.project.test.testtask

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    //Вью модель хранящая массив картинок и индекс массива
    private lateinit var imagesViewModel: ImagesViewModel
    //Массив картинок
    private var imagesArray: Array<Bitmap>? = null
    //Слушатель свайпов для firstImageView
    private lateinit var swipeImageListener: SwipeImageListener
    //Слушатель нажатий на firstImageView для показа рекламы
    private lateinit var advertisingListener: AdvertisingListener
    //Рекламный баннер
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Загрузка вьюмодели
        imagesViewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)
        //Инициализация обработчика свайпов
        swipeImageListener = SwipeImageListener(firstImageView, secondImageView, imagesArray, imagesViewModel.imageIndex)
        //Установка слушателя на изменение массива картинок
        imagesViewModel.imagesListLiveData.observe(this, Observer<Array<Bitmap>> { newValue ->
            imagesArray = newValue
            swipeImageListener.imagesArray = imagesArray
        })

        //Инициализация рекламы AdMob
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.banner_ad_unit_id)
        //Инициализация обработчика для показа рекламы на каждые 4 нажатия
        advertisingListener = AdvertisingListener(mInterstitialAd)
        //Показ рекламы по загрузке
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                mInterstitialAd.show()
            }
        }
        //Установка слушателей на  передний ImageView
        firstImageView.setOnTouchListener { v, event ->
            (swipeImageListener.onTouch(v, event)).or(advertisingListener.onTouch(v, event))
        }
    }
    //Загрузка меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    //Обработка выбора пункта меню
    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        //Выбран переход в галерею
        R.id.galleryMenuItem -> {
            val intent = Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivity(intent)
            true
        }
        //Выбран переход в гугл плей
        R.id.googlePlayMenuItem -> {
            try {
                //Открытие через гугл плей
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=${getString(R.string.gallery_market_id)}")
                startActivity(intent)
            } catch (exp: ActivityNotFoundException) {
                //Открытие через браузер
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://play.google.com/store/apps/details?id=${getString(R.string.gallery_market_id)}")
                startActivity(intent)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
