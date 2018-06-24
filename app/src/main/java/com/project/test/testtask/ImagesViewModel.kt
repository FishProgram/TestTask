package com.project.test.testtask

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlin.jvm.internal.Ref

class ImagesViewModel : ViewModel() {
    private val imagesRepository = ImagesRepository()
    val imagesListLiveData = MutableLiveData<Array<Bitmap>>()
    var imageIndex: Ref.IntRef = Ref.IntRef()

    init {
        async(CommonPool) {

            imagesListLiveData.postValue(imagesRepository.searchImages())

        }
    }
}