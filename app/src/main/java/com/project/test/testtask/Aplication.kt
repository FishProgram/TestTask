package com.project.test.testtask

import android.app.Application
import android.content.Context
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class Application : Application() {



    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        // Инициализация AppMetrica SDK
        val configBuilder = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_metrica_api_key))
        YandexMetrica.activate(applicationContext, configBuilder.build())
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this)

    }

    companion object {
        lateinit var appContext: Context
    }
}