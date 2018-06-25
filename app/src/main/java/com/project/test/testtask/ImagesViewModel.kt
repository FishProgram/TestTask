package com.project.test.testtask

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlin.jvm.internal.Ref
/*
ViewModel которая хранит массив картинок и индекс массива
 */
class ImagesViewModel : ViewModel() {
    //Репозиторий для получения картинок с сервера
    private val imagesRepository = ImagesRepository()
    //Прослушиваемый массив картинок
    val imagesListLiveData = MutableLiveData<Array<Bitmap>>()
    //Индекс массива картинок
    var imageIndex: Ref.IntRef = Ref.IntRef()

    init {
        async(CommonPool) {

            imagesListLiveData.postValue(imagesRepository.searchImages())

        }
    }
}